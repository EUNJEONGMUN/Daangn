package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
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

    /**
     * 유저 개인 정보 조회 API
     * [GET] /users/:userId
     * @return BaseResponse<GetUserInfoRes>
     */
    public GetUserInfoRes getUserInfo(int userId) throws BaseException {
        try {
            GetUserInfoRes getUserInfoRes = userDao.getUserInfo(userId);
            return getUserInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

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

    // 키워드 체크
    public int checkKeyword(int userId, String keyword) throws BaseException {
        try{
            return userDao.checkKeyword(userId, keyword);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 받은 매너 평가 조회 API
     * [GET] /users/:userIdx/manner
     * @return BaseResponse<List<GetUserMannerRes>>
     */
    public List<GetUserMannerRes> getUserManner(int userId) throws BaseException {
        try{
            List<GetUserMannerRes> getUserMannerRes = userDao.getUserManner(userId);
            return getUserMannerRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 관심 목록 전체 조회 API
     * [GET] /users/:userId/attention
     * @return BaseResponse<List<GetUserAttentionRes>>
     */
    public List<GetUserAttentionRes> getAttention(int userId) throws BaseException {
    try {
            List<GetUserAttentionRes> getUserAttentionRes = userDao.getAttention(userId);
            return getUserAttentionRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /**
     * 보유 쿠폰 상태별 조회 API
     * [GET] /users/:userId/coupons
     * [GET] /coupons?status=?
     * @return BaseResponse<List<GetUserCouponRes>>
     */
    public List<GetUserCouponRes> getCoupon(int userId, String status) throws BaseException {
        try {
            List<GetUserCouponRes> getUserCouponRes = userDao.getCoupon(userId, status);
            return getUserCouponRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
