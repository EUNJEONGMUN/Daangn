package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.Res.GetProductListRes;

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
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductListRes> getProducts() throws BaseException {
        try{
            List<GetProductListRes> getProductListRes = productDao.getProducts();
            return getProductListRes;
        } catch (Exception exception){
            System.out.println("home provider -> "+exception);
            exception.getStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 홈 화면 카테고리별 조회 API
     * [GET] /products/home/:categoryId
     * @return BaseResponse<List<GetProductRes>>
     */
    public List<GetProductListRes> getProduct(int categoryId) throws BaseException {
        try {
            List<GetProductListRes> getProductListRes = productDao.getProduct(categoryId);
            return getProductListRes;
        } catch (Exception exception){
            System.out.println("home category provider -> "+exception);
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
    public List<GetProductListRes> getUserProductPost(int userId, String status) throws BaseException {
        try {
            List<GetProductListRes> getProductListRes = productDao.getUserProductPost(userId, status);
            return getProductListRes;
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
    public List<GetProductListRes> getUserBuyList(int userId) throws BaseException {
        try {
            List<GetProductListRes> getProductListRes = productDao.getUserBuyList(userId);
            return getProductListRes;
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

    /**
     * 중고 거래 글 조회 API
     * [GET] /products/:postId
     * @return BaseResponse<GetProductRes>
     */
    public GetProductRes getPost(int postId) throws BaseException {
        try{
            GetProductRes getProductRes = productDao.getPost(postId);
            return getProductRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
