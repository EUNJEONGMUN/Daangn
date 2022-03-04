package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductRes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.tokens.ScalarToken;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ProductProvider {

    private final ProductDao productDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao){
        this.productDao = productDao;
    }

    public List<GetProductRes> getProducts() throws BaseException {

        try{
            List<GetProductRes> getProductRes = productDao.getProducts();
            return getProductRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public List<GetProductRes> getProduct(int categoryId) throws BaseException {
        try {
            List<GetProductRes> getProductRes = productDao.getProduct(categoryId);
            return getProductRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

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
