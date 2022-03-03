package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.PostStoreReq;
import com.example.demo.src.store.model.PostStoreRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class StoreService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final StoreDao storeDao;
    private final StoreProvider storeProvider;

    @Autowired
    private StoreService(StoreDao storeDao, StoreProvider storeProvider) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
    }

    // POST
    public PostStoreRes createStore(PostStoreReq postStoreReq) throws BaseException {

        if(storeProvider.checkStoreCount(postStoreReq.getUserId())>=3){
            // 비즈 프로필은 유저당 최대 3개까지 생성 가능
            throw new BaseException(POST_STORE_NUMBER_EXCEEDED);
        }
        if(storeProvider.checkTopCategory(postStoreReq.getStoreCategoryId()) != 4){
            // 가게 카테고리는 refId = 4
            throw new BaseException(CATEGORY_RANGE_ERROR);
        }

        try{
            int storeId = storeDao.createStore(postStoreReq);
            return new PostStoreRes(storeId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }


    }
}
