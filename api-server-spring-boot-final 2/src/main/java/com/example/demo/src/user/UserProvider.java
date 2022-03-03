package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.GetUserBadgeRes;
import com.example.demo.src.user.model.GetUserLikeStoreRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class UserProvider {
    private final UserDao userDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<GetUserBadgeRes> getUserBadges(int userId) throws BaseException  {
        try{
            List<GetUserBadgeRes> getUserBadgeRes = userDao.getUserBadges(userId);
            return getUserBadgeRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserLikeStoreRes> getUserLikeStores(int userId) throws BaseException {
        try{
            List<GetUserLikeStoreRes> getUserLikeStoreRes = userDao.getUserLikeStores(userId);
            return getUserLikeStoreRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
