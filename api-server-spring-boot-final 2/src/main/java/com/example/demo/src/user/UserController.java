package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.model.Req.PatchMyInfoReq;
import com.example.demo.src.user.model.Req.PostSignInReq;
import com.example.demo.src.user.model.Req.PostUserKeywordsReq;
import com.example.demo.src.user.model.Req.PostUserReq;
import com.example.demo.src.user.model.Res.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;


@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     * [POST] /users/sign-up
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<String> createUser(@RequestBody PostUserReq postUserReq){
        if (postUserReq.getPhoneNumber() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
        }
        // 휴대폰 번호 정규표현
        if (!isRegexPhoneNumber(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONEMUNBER);
        }

        /**
         * 휴대폰 인증 요금 발생주의
         */
        // 휴대폰 인증
//        System.out.println("휴대폰 인증 시작");
//        String phoneNumber = postUserReq.getPhoneNumber();
//        Random rand  = new Random();
//        String numStr = "";
//        for(int i=0; i<4; i++) {
//            String ran = Integer.toString(rand.nextInt(10));
//            numStr+=ran;
//        }
//
//        System.out.println("수신자 번호 : " + phoneNumber);
//        System.out.println("인증번호 : " + numStr);
//
//        System.out.println("휴대폰 인증으로 넘어감");
//        String success = userService.certifiedPhoneNumber(phoneNumber,numStr);
//        System.out.println("휴대폰 인증에서 나옴");
//
//        if (success == "fail"){
//            return new BaseResponse<>(FAIL_MESSAGE_AUTH);
//        }
//        try {
//            System.out.println("계정 생성으로 넘어감");
//            userService.createUser(postUserReq);
//            System.out.println("계정 생성에서 나옴");
//            String result = "";
//            return new BaseResponse<>(result);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }


        try {
            userService.createUser(postUserReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/sign-in
     * @return BaseResponse<PostSignInRes>
     */
    @ResponseBody
    @PostMapping("/sign-in")
    public BaseResponse<PostSignInRes> signIn(@RequestBody PostSignInReq postSignInReq){
        if (postSignInReq.getPhoneNumber() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
        }
        // 휴대폰 번호 정규표현
        if (!isRegexPhoneNumber(postSignInReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONEMUNBER);
        }
        try {
            PostSignInRes postSignInRes = userService.signIn(postSignInReq);
            return new BaseResponse<>(postSignInRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 프로필 조회 API
     * [GET] /users/:userId
     * @return BaseResponse<GetUserInfoRes>
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable int userId){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetUserInfoRes getUserInfoRes = userProvider.getUserInfo(userId);
            return new BaseResponse<>(getUserInfoRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필 수정 API
     * [PATCH] /users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyMyInfo(@PathVariable int userId, @RequestBody Myinfo myinfo){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchMyInfoReq patchMyInfoReq = new PatchMyInfoReq(userId, myinfo.getProfileImg(), myinfo.getUserName());
            userService.modifyMyInfo(patchMyInfoReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 배지 조회 API
     * [GET] /users/:userIdx/badge
     * @return BaseResponse<List<GetUserBadgeRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}/badge")
    public BaseResponse<List<GetUserBadgeRes>> getUserBadges(@PathVariable int userId){
        try{
            List<GetUserBadgeRes> getUserBadgeRes = userProvider.getUserBadges(userId);
            return new BaseResponse<>(getUserBadgeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 유저 단골 가게 조회 API
     * [GET] /users/:userIdx/likestores
     * @return BaseResponse<List<GetUserLikeStoreRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}/likestores")
    public BaseResponse<List<GetUserLikeStoreRes>> GetUserLikeStoreRes(@PathVariable int userId){
        try{
            List<GetUserLikeStoreRes> getUserLikeStoreRes = userProvider.getUserLikeStores(userId);
            return new BaseResponse<>(getUserLikeStoreRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 받은 매너 평가 조회 API
     * [GET] /users/:userIdx/manner
     * @return BaseResponse<List<GetUserMannerRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}/manner")
    public BaseResponse<List<GetUserMannerRes>> getUserManner(@PathVariable int userId){
        try{
            List<GetUserMannerRes> getUserMannerRes = userProvider.getUserManner(userId);
            return new BaseResponse<>(getUserMannerRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 키워드 알림설정 API
     * [POST] /users/:userIdx/keywords
     * @return BaseResponse<PostUserKeywordsRes>
     */
    @ResponseBody
    @PostMapping("/{userId}/keywords")
    public BaseResponse<PostUserKeywordsRes> createKeywords(@PathVariable int userId, @RequestBody PostUserKeywordsReq postUserKeywordsReq){
        if (postUserKeywordsReq.getKeyword() == null) {
            return new BaseResponse<>(EMPTY_KEYWORD);
        }
        try {
            PostUserKeywordsRes postUserKeywords = userService.createKeywords(userId, postUserKeywordsReq);
            return new BaseResponse<>(postUserKeywords);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 키워드 알림해제 API
     * [DELETE] /users/:userIdx/keywords/:keywordsId/deletion
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{userId}/keywords/{keywordsId}/deletion")
    public BaseResponse<String> deleteKeywords(@PathVariable int userId, @PathVariable int keywordsId){
        try {
            userService.deleteKeywords(userId, keywordsId);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 관심 목록 전체 조회 API
     * [GET] /users/:userId/attention
     * @return BaseResponse<List<GetUserAttentionRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}/attention")
    public BaseResponse<List<GetUserAttentionRes>> getAttention(@PathVariable int userId){
        try{
            List<GetUserAttentionRes> getUserAttentionRes = userProvider.getAttention(userId);
            return new BaseResponse<>(getUserAttentionRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 보유 쿠폰 상태별 조회 API
     * [GET] /users/:userId/coupons
     * [GET] /coupons?status=?
     * @return BaseResponse<List<GetUserCouponRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}/coupons")
    public BaseResponse<List<GetUserCouponRes>> getAttention(@PathVariable int userId, @RequestParam(required = false) String status){
        try{
            if(status == null){
                List<GetUserCouponRes> getUserCouponRes = userProvider.getCoupon(userId, "Y");
                return new BaseResponse<>(getUserCouponRes);
            }
            List<GetUserCouponRes> getUserCouponRes = userProvider.getCoupon(userId, status);
            return new BaseResponse<>(getUserCouponRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }






}
