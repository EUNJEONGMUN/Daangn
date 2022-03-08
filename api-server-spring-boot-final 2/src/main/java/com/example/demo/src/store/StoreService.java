package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.Req.PostNewsReq;
import com.example.demo.src.store.model.Res.PostNewsRes;
import com.example.demo.src.store.model.Req.PostStoreReq;
import com.example.demo.src.store.model.Res.PostStoreRes;
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
    private final int FAIL = 0;

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

    public void modifyStore(int storeId, PostStoreReq postStoreReq) throws BaseException {
        if (storeProvider.checkStoreUser(storeId, postStoreReq.getUserId()) == 0){
            throw new BaseException(PATCH_STORE_EMPTY_USER);
        }

        if(storeProvider.checkTopCategory(postStoreReq.getStoreCategoryId()) != 4){
            // 가게 카테고리는 refId = 4
            throw new BaseException(CATEGORY_RANGE_ERROR);
        }

        try {
            int result = storeDao.modifyStore(storeId, postStoreReq);
            if (result==0){
                throw new BaseException(FAIL_TO_STORE_MODIFY);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteStore(int storeId, int userId) throws BaseException {
        if (storeProvider.checkStoreUser(storeId, userId) == 0){
            throw new BaseException(PATCH_STORE_EMPTY_USER);
        }

        try {
            int result = storeDao.deleteStore(storeId, userId);
            if (result==0){
                throw new BaseException(FAIL_TO_STORE_DELETE);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public PostNewsRes createNews(PostNewsReq postNewsReq) throws BaseException {
        try {
            int newsId = storeDao.createNews(postNewsReq);
            return new PostNewsRes(newsId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public void modifyNews(int storeNewsId, PostNewsReq postNewsReq) throws BaseException  {
//        // 전달받은 postId의 storeId와 body의 storeId와 같아야함
//
//        if (storeProvider.checkNewsUser(storeNewsId, postNewsReq.getStoreId())==0){
//            throw new BaseException(PATCH_NEWS_NOT_CORRECT_USER);
//        }
//        try {
//            int result = storeDao.modifyNews(storeNewsId, postNewsReq);
//            if (result==0){
//                throw new BaseException(FAIL_TO_NEWS_MODIFY);
//            }
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }

    public void modifyNews(int storeNewsId, PostNewsReq postNewsReq) throws BaseException  {

        try {
            int result = storeDao.modifyNews(storeNewsId, postNewsReq);
            if (result==FAIL){
                throw new BaseException(FAIL_TO_NEWS_MODIFY);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

}
