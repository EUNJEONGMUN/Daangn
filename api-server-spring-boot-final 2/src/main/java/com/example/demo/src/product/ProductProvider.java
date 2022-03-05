package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductRes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.tokens.ScalarToken;

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

    /**
     * 홈 화면 카테고리별 조회 API
     * [GET] /products/home/:categoryId
     * @return BaseResponse<GetProductRes>
     */
    public List<GetProductRes> getProduct(int categoryId) throws BaseException {
        try {
            if (productDao.checkTopCategory(categoryId) != 2){
                // 동네 생활 카테고리는 refId = 2
                throw new BaseException(CATEGORY_RANGE_ERROR);
            }
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

    public int checkDeal(int postId, int userId) throws BaseException {
        try{
            return productDao.checkDeal(postId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
