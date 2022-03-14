package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.UnAuth;
import com.example.demo.src.product.model.Res.GetProductListRes;
import com.example.demo.src.user.model.*;
import com.example.demo.src.user.model.Req.*;
import com.example.demo.src.user.model.Res.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
     * 휴대폰 인증 API
     * [POST] /users/message
     * @return BaseResponse<String>
     */
    @UnAuth
    @ResponseBody
    @PostMapping("/message")
    public BaseResponse<String> messageUser(@Valid @RequestBody PostMessageReq postMessageReq) throws BaseException {

        String phoneNumber = postMessageReq.getPhoneNumber();

        //  난수 생성
        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numStr);

        userService.certifiedPhoneNumber(phoneNumber,numStr);
        String result = "";
        return new BaseResponse<>(result);

    }

    /**
     * 회원가입 API
     * [POST] /users/sign-up
     * @return BaseResponse<String>
     */
    @UnAuth
    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<String> createUser(@Valid @RequestBody PostUserReq postUserReq) throws BaseException {
        userService.createUser(postUserReq);
        String result = "";
        return new BaseResponse<>(result);

    }

    /**
     * 로그인 API
     * [POST] /users/sign-in
     * @return BaseResponse<PostSignInRes>
     */
    @UnAuth
    @ResponseBody
    @PostMapping("/sign-in")
    public BaseResponse<PostSignInRes> signIn(@Valid @RequestBody PostSignInReq postSignInReq) throws BaseException {
        PostSignInRes postSignInRes = userService.signIn(postSignInReq);
        return new BaseResponse<>(postSignInRes);
    }

    /**
     * 회원 탈퇴 API
     * [PATCH] /users/deletion
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/deletion")
    public BaseResponse<String> deleteUser(HttpServletRequest request, @Valid @RequestBody PatchUser patchUser) throws BaseException {

        int userId = (int) request.getAttribute("userId");

        if (userProvider.checkUser(userId)==0){
            return new BaseResponse<>(USER_NOT_EXISTS);
        }

        if (userProvider.checkUserState(userId).equals("N")){
            return new BaseResponse<>(USERS_SECESSION);
        }

        PatchUserReq patchUserReq = new PatchUserReq(userId, patchUser.getStatus());
        userService.deleteUser(patchUserReq);
        String result = "";
        return new BaseResponse<>(result);

    }


    /**
     * 프로필 조회 API
     * [GET] /users
     * @return BaseResponse<GetUserInfoRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetUserInfoRes> getUserInfo(HttpServletRequest request) throws BaseException {

        int userId = (int) request.getAttribute("userId");

        GetUserInfoRes getUserInfoRes = userProvider.getUserInfo(userId);
        return new BaseResponse<>(getUserInfoRes);

    }

    /**
     * 프로필 수정 API
     * [PATCH] /users
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<String> modifyMyInfo(HttpServletRequest request, @RequestBody Myinfo myinfo) throws BaseException {

        int userId = (int) request.getAttribute("userId");

        PatchMyInfoReq patchMyInfoReq = new PatchMyInfoReq(userId, myinfo.getProfileImg(), myinfo.getUserName(), myinfo.getJusoCodeId());
        userService.modifyMyInfo(patchMyInfoReq);

        String result = "";
        return new BaseResponse<>(result);

    }

    /**
     * 유저 배지 조회 API
     * [GET] /users/badge
     * @return BaseResponse<List<GetUserBadgeRes>>
     */
    @ResponseBody
    @GetMapping("/badge")
    public BaseResponse<List<GetUserBadgeRes>> getUserBadges(HttpServletRequest request) throws BaseException {
        int userId = (int) request.getAttribute("userId");

        List<GetUserBadgeRes> getUserBadgeRes = userProvider.getUserBadges(userId);
        return new BaseResponse<>(getUserBadgeRes);

    }

    /**
     * 유저 단골 가게 조회 API
     * [GET] /users/likestores
     * @return BaseResponse<List<GetUserLikeStoreRes>>
     */
    @ResponseBody
    @GetMapping("/likestores")
    public BaseResponse<List<GetUserLikeStoreRes>> GetUserLikeStoreRes(HttpServletRequest request) throws BaseException {
        int userId = (int) request.getAttribute("userId");

        List<GetUserLikeStoreRes> getUserLikeStoreRes = userProvider.getUserLikeStores(userId);
        return new BaseResponse<>(getUserLikeStoreRes);

    }

    /**
     * 보유 쿠폰 상태별 조회 API
     * [GET] /users/coupons
     * [GET] /coupons?status=?
     * @return BaseResponse<List<GetUserCouponRes>>
     */
    @ResponseBody
    @GetMapping("/coupons")
    public BaseResponse<List<GetUserCouponRes>> getAttention(HttpServletRequest request, @RequestParam(required = false) String status) throws BaseException {
        int userId = (int) request.getAttribute("userId");

        if(status == null){
            List<GetUserCouponRes> getUserCouponRes = userProvider.getCoupon(userId, "Y");
            return new BaseResponse<>(getUserCouponRes);
        }
        List<GetUserCouponRes> getUserCouponRes = userProvider.getCoupon(userId, status);
        return new BaseResponse<>(getUserCouponRes);
    }

    /**
     * 관심 목록 전체 조회 API
     * [GET] /users/attention
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/attention")
    public BaseResponse<List<GetProductListRes>> getAttention(HttpServletRequest request) throws BaseException {
        int userId = (int) request.getAttribute("userId");

        List<GetProductListRes> getProductListRes = userProvider.getAttention(userId);
        return new BaseResponse<>(getProductListRes);

    }

    /**
     * 받은 매너 평가 조회 API
     * [GET] /users/manner
     * @return BaseResponse<List<GetUserMannerRes>>
     */
    @ResponseBody
    @GetMapping("/manner")
    public BaseResponse<List<GetUserMannerRes>> getUserManner(HttpServletRequest request) throws BaseException {
        int userId = (int) request.getAttribute("userId");

        List<GetUserMannerRes> getUserMannerRes = userProvider.getUserManner(userId);
        return new BaseResponse<>(getUserMannerRes);

    }


    /**
     * 키워드 알림설정 API
     * [POST] /users/keywords
     * @return BaseResponse<PostUserKeywordsRes>
     */
    @ResponseBody
    @PostMapping("/keywords")
    public BaseResponse<PostUserKeywordsRes> createKeywords(HttpServletRequest request, @Valid @RequestBody PostUserKeywordsReq postUserKeywordsReq) throws BaseException {

        int userId = (int) request.getAttribute("userId");

        PostUserKeywordsRes postUserKeywords = userService.createKeywords(userId, postUserKeywordsReq.getKeyword());
        return new BaseResponse<>(postUserKeywords);

    }

    /**
     * 키워드 알림해제 API
     * [DELETE] /users/keywords
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/keywords")
    public BaseResponse<String> deleteKeywords(HttpServletRequest request, @Valid @RequestBody DeleteKeyword deleteKeyword) throws BaseException {
        int userId = (int) request.getAttribute("userId");

        DeleteKeywordReq deleteKeywordReq = new DeleteKeywordReq(userId, deleteKeyword.getKeyword());
        userService.deleteKeywords(deleteKeywordReq);
        String result = "";
        return new BaseResponse<>(result);

    }


}
