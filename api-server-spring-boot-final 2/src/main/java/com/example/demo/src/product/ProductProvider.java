package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.Res.GetProductPostRes;
import com.example.demo.src.product.model.Res.GetProductRes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ProductProvider {

    private final ProductDao productDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao){
        this.productDao = productDao;
    }

    /**
     * 홈 화면 조회 API
     * [GET] /products/home
     * @return BaseResponse<GetProductRes>
     */
    public List<GetProductRes> getProducts() throws BaseException {
        try{
            List<GetProductRes> getProductRes = productDao.getProducts();
            return getProductRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
//    public List<GetProductPostRes> getProducts() throws BaseException {
//        try{
//            List<GetProductPostRes> getProductPostRes = productDao.getProducts();
//            return getProductPostRes;
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

    /**
     * 홈 화면 카테고리별 조회 API
     * [GET] /products/home/:categoryId
     * @return BaseResponse<GetProductRes>
     */
    public List<GetProductRes> getProduct(int categoryId) throws BaseException {
        try {
            List<GetProductRes> getProductRes = productDao.getProduct(categoryId);
            return getProductRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 중고 거래 관심 등록 확인
    public int checkAtt(int postId, int userId) throws BaseException {
        try{
            return productDao.checkAtt(postId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }
    // 사용자 관심 등록 확인
    public String checkAttStatus(int postId, int userId) throws BaseException  {
        try{
            return productDao.checkAttStatus(postId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 거래 기록 체크
    public int checkDeal(int postId) throws BaseException {
        try{
            return productDao.checkDeal(postId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 사용자의 거래 기록 상태
    public String checkDealUser(int postId, int userId) throws BaseException {
        try{
            return productDao.checkDealUser(postId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 판매 내역 상태별 조회 API
     * [GET] /products/user-post/:userId
     * /:userId?status=?
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductRes> getUserProductPost(int userId, String status) throws BaseException {
        try {
            List<GetProductRes> getProductRes = productDao.getUserProductPost(userId, status);
            return getProductRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkTopCategory(int categoryId) throws BaseException  {
        try {
            return productDao.checkTopCategory(categoryId);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 구매 내역 조회 API
     * [GET] /products/buylist/:userId
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductRes> getUserBuyList(int userId) throws BaseException {
        try {
            List<GetProductRes> getProductRes = productDao.getUserBuyList(userId);
            return getProductRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkPostExists(int postId) throws BaseException {
        try {
            return productDao.checkPostExists(postId);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
