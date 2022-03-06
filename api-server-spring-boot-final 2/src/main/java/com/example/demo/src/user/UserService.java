package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.town.model.PostTownComRes;
import com.example.demo.src.user.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider) {
        this.userDao = userDao;
        this.userProvider = userProvider;

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


    /**
     * 유저 개인 정보 수정 API
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


}
