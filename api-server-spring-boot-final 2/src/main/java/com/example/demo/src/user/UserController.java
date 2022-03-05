package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.EMPTY_KEYWORD;


@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;

    public UserController(UserProvider userProvider, UserService userService){
        this.userProvider = userProvider;
        this.userService = userService;
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
     * 유저 판매 내역 조회 API
     * [GET] /users/:userId/saleslist
     * 판매 내역 상태별 조회 API
     * [GET] /salelists? State =
     * @return BaseResponse<List<GetUserSalesListRes>>
     */
    //Query String
//    @ResponseBody
//    @GetMapping("/:userId/saleslist") // (GET) 127.0.0.1:9000/app/users
//    public BaseResponse<List<GetUserSalesListRes>> getUserSalesList(@RequestParam(required = false) String State){
//        try {
//            List<GetUserSalesListRes> getUserSalesListRes = userProvider.getUserSalesList(State);
//            return new BaseResponse<>(getUserSalesListRes);
//        } catch(BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//
//        }
//    }

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
     * [PUT] /users/:userIdx/keywords
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PutMapping("/{userId}/keywords")
    public BaseResponse<String> setKeywords(@PathVariable int userId, @RequestBody PutUserKeywordsReq putUserKeywordsReq){
        if (putUserKeywordsReq.getKeyword() == null) {
            return new BaseResponse<>(EMPTY_KEYWORD);
        }
        try {
            userService.setKeywords(userId, putUserKeywordsReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 개인 정보 조회 API
     * [GET] /users/:userId
     * @return BaseResponse<GetUserInfoRes>
     */
    @ResponseBody
    @GetMapping("/{userId}")
     public BaseResponse<GetUserInfoRes> getUserInfo(@PathVariable int userId){
        try{
            GetUserInfoRes getUserInfoRes = userProvider.getUserInfo(userId);
            return new BaseResponse<>(getUserInfoRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 개인 정보 수정 API
     * [PATCH] /users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyMyInfo(@PathVariable int userId, @RequestBody User user){
        try {
            PatchMyInfoReq patchMyInfoReq = new PatchMyInfoReq(userId, user.getUserImg(), user.getName());
            userService.modifyMyInfo(patchMyInfoReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}
