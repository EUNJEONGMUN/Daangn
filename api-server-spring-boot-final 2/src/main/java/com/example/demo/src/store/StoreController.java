package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.Req.PostNewsReq;
import com.example.demo.src.store.model.Req.PostStoreReq;
import com.example.demo.src.store.model.Res.PostNewsRes;
import com.example.demo.src.store.model.Res.PostStoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/stores")
public class StoreController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoreProvider storeProvider;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService;


    public StoreController(StoreProvider storeProvider, StoreService storeService, JwtService jwtService){
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.jwtService = jwtService;
    }

    /**
     * 비즈 프로필 생성 API
     * [POST] /stores/account/:userId
     * @return BaseResponse<PostStoreRes>
     */
    @ResponseBody
    @PostMapping("/account/{userId}")
    public BaseResponse<PostStoreRes> createStore(@PathVariable int userId, @RequestBody PostStoreReq postStoreReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (postStoreReq.getStoreName() == null){
                return new BaseResponse<>(POST_STORE_EMPTY_STORENAME);
            }
            if (postStoreReq.getStoreCategoryId() == 0){
                return new BaseResponse<>(EMPTY_CATEGORY);
            }

            PostStoreRes postStoreRes = storeService.createStore(postStoreReq);
            return new BaseResponse<>(postStoreRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 비즈 프로필 수정 API
     * [PATCH] /stores/:storeId/account
     * @return BaseResponse<String>
     */
    //Query String
    @ResponseBody
    @PatchMapping("/{storeId}/account")
    public BaseResponse<String> modifyStore(@PathVariable int storeId, @RequestBody PostStoreReq postStoreReq){

        try{
            storeService.modifyStore(storeId, postStoreReq);
            String result = "";
            return new BaseResponse<>(result);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 비즈 프로필 삭제 API
     * [DELETE] /stores/account
     * [DELETE] /account? StoreId =? & UserId =?
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/account")
    public BaseResponse<String> deleteStore(@RequestParam("StoreId") int StoreId,
                                            @RequestParam("UserId") int UserId) {

        try{
            storeService.deleteStore(StoreId, UserId);
            String result = "";
            return new BaseResponse<>(result);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 가게 소식 작성 API
     * [POST] /stores/news/new
     * @return BaseResponse<PostNewsRes>
     */
    @ResponseBody
    @PostMapping("/news/new")
    public BaseResponse<PostNewsRes> createNews(@RequestBody PostNewsReq postNewsReq){
        if (postNewsReq.getStoreId() == 0){
            return new BaseResponse<>(POST_NEWS_EMPTY_STOREID);
        }
        if (postNewsReq.getTitle() == null){
            return new BaseResponse<>(POST_NEWS_EMPTY_TITLE);
        }
        if (postNewsReq.getContent() == null){
            return new BaseResponse<>(POST_NEWS_EMPTY_CONTENT);
        }
        try{

            PostNewsRes postNewsRes = storeService.createNews(postNewsReq);
            return new BaseResponse<>(postNewsRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 가게 소식 수정 API
     * [PATCH] /stores/news
     * [PATCH] /news?StoreNewsId =
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/news/{storeNewsId}")
    public BaseResponse<String> modifyNews(@PathVariable int storeNewsId, @RequestBody PostNewsReq postNewsReq) {
        if (postNewsReq.getStoreId() == 0) {
            return new BaseResponse<>(POST_NEWS_EMPTY_STOREID);
        }
        if (postNewsReq.getTitle() == null) {
            return new BaseResponse<>(POST_NEWS_EMPTY_TITLE);
        }
        if (postNewsReq.getContent() == null) {
            return new BaseResponse<>(POST_NEWS_EMPTY_CONTENT);
        }

        try {
            storeService.modifyNews(storeNewsId, postNewsReq);
            String result = "";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

//    @ResponseBody
//    @PutMapping("/news")
//    public BaseResponse<String> modifyNews(@RequestParam("PostId") int PostId, PostNewsReq postNewsReq) {
//
//        if (postNewsReq.getTitle() == null) {
//            return new BaseResponse<>(POST_NEWS_EMPTY_TITLE);
//        }
//        if (postNewsReq.getContent() == null) {
//            return new BaseResponse<>(POST_NEWS_EMPTY_CONTENT);
//        }
//
//        try {
//            storeService.modifyNews(PostId, postNewsReq);
//            String result = "";
//            return new BaseResponse<>(result);
//
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//
//    }
}

