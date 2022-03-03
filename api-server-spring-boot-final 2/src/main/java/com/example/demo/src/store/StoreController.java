package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.store.model.*;
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


    public StoreController(StoreProvider storeProvider, StoreService storeService){
        this.storeProvider = storeProvider;
        this.storeService = storeService;
    }

    @ResponseBody
    @PostMapping("/account")
    public BaseResponse<PostStoreRes> createStore(@RequestBody PostStoreReq postStoreReq){
        if (postStoreReq.getStoreName() == null){
            return new BaseResponse<>(POST_STORE_EMPTY_STORENAME);
        }
        if (postStoreReq.getStoreCategoryId() == 0){
            return new BaseResponse<>(EMPTY_CATEGORY);
        }

        try {
            PostStoreRes postStoreRes = storeService.createStore(postStoreReq);
            return new BaseResponse<>(postStoreRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
