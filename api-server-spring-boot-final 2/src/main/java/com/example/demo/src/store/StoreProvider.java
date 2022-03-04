package com.example.demo.src.store;

import com.example.demo.config.BaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class StoreProvider {

    private final StoreDao storeDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public StoreProvider(StoreDao storeDao){
        this.storeDao = storeDao;
    }


    public int checkStoreCount(int userId) throws BaseException {
        try{
            return storeDao.checkStoreCount(userId);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTopCategory(int storeCategoryId) throws BaseException {
        try{
            return storeDao.checkTopCategory(storeCategoryId);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkStoreUser(int storeId, int userId) throws BaseException {
        try {
            return storeDao.checkStoreUser(storeId, userId);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkNewsUser(int postId, int storeId) throws BaseException {
        try{
            return storeDao.checkNewsUser(postId, storeId);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }


    }
}
