package com.example.demo.src.user;

import com.example.demo.config.BaseException;

import com.example.demo.src.user.model.Req.PatchMyInfoReq;
import com.example.demo.src.user.model.Req.PostSignInReq;
import com.example.demo.src.user.model.Req.PostUserKeywordsReq;
import com.example.demo.src.user.model.Req.PostUserReq;
import com.example.demo.src.user.model.Res.PostSignInRes;
import com.example.demo.src.user.model.Res.PostUserKeywordsRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.secret.MessageKey.*;

@Service
public class UserService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }


    // 휴대폰 인증
    public String certifiedPhoneNumber(String phoneNumber, String numStr) {
        String api_key = API_KEY;
        String api_secret = API_SECRET;
        net.nurigo.java_sdk.api.Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", PHONE_NUMBER);    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "회원가입 인증 메시지 : 인증번호는" + "["+numStr+"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
            return "success";
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
            return "fail";
        }
    }

    /**
     * 회원가입 API
     * [POST] /users/sign-up
     * @return BaseResponse<String>
     */
    public void createUser(PostUserReq postUserReq) throws BaseException {
        // 중복 확인
        if(userProvider.checkPhoneNumber(postUserReq.getPhoneNumber()) == 1){
            throw new BaseException(POST_USERS_EXISTS_PHONE_NUMBER);
        }
        String phone;
        try {
            phone = new SHA256().encrypt(postUserReq.getPhoneNumber());
            postUserReq.setPhoneNumber(phone);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try {
            int result = userDao.createUser(postUserReq);
            if (result == 0){
                throw new BaseException(POST_FAIL_SIGNUP);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 로그인 API
     * [POST] /users/sign-in
     * @return BaseResponse<PostSignInRes>
     */
    public PostSignInRes signIn(PostSignInReq postSignInReq) throws BaseException {
        String encryptPhone;
        try {
            // 사용자에게 바디값을 받은 전화번호 암호화
            encryptPhone = new SHA256().encrypt(postSignInReq.getPhoneNumber());
            postSignInReq.setPhoneNumber(encryptPhone);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        // 사용자가 존재하는지 확인
        // 존재하지 않을 때
        if(userProvider.checkPhoneNumber(encryptPhone) != 1){
            throw new BaseException(POST_USERS_NOT_EXISTS_PHONENUMBER);
        }

        // 삭제된 계정일 때
        if(userProvider.checkExistsUser(encryptPhone) == 'N'){
            throw new BaseException(POST_USERS_SECESSION);
        }

        // DB로부터 정보를 가져옴
        User user = userDao.getPhoneNumber(postSignInReq);

        try {
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostSignInRes(userId,jwt);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_LOGIN);
        }


    }

    /**
     * 프로필 수정 API
     * [PATCH] /users/:userId
     * @return BaseResponse<String>
     */
    public void modifyMyInfo(PatchMyInfoReq patchMyInfoReq) throws BaseException {
        try{
            int result = userDao.modifyMyInfo(patchMyInfoReq);
            if (result == 0){
                throw new BaseException(PATCH_FAIL_MYINFO);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 키워드 알림설정 API
     * [POST] /users/:userIdx/keywords
     * @return BaseResponse<PostUserKeywordsRes>
     */
    public PostUserKeywordsRes createKeywords(int userId, PostUserKeywordsReq postUserKeywordsReq) throws BaseException {
        try{
            if (userProvider.checkKeyword(userId, postUserKeywordsReq.getKeyword()) != 0){
                // 요청한 키워드가 있을 경우
                throw new BaseException(DUPLICATED_KEYWORDS);
            } else {
                // 요청한 키워드가 없을 경우
                int keywordsId = userDao.createKeywords(userId, postUserKeywordsReq);
                return new PostUserKeywordsRes(keywordsId);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /**
     * 키워드 알림해제 API
     * [DELETE] /users/:userIdx/keywords/:keywordsId/deletion
     * @return BaseResponse<String>
     */
    public void deleteKeywords(int userId, int keywordsId) throws BaseException {
        try {
            int result = userDao.deleteKeywords(userId, keywordsId);
            if (result == 0){
                throw new BaseException(DELETE_FAIL_KEYWORDS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }





}
