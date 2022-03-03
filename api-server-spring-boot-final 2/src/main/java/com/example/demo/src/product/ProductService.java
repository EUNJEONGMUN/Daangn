package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;

    @Autowired
    private ProductService(ProductDao productDao, ProductProvider productProvider) {
        this.productDao = productDao;
        this.productProvider = productProvider;
    }

    // POST
    public PostProductNewRes createProduct(PostProductNewReq postProductNewReq) throws BaseException {

        try{
            int productPostId = productDao.createProduct(postProductNewReq);
            return new PostProductNewRes(productPostId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public PostProductNewRes createProductFree(PostProductNewReq postProductNewReq) throws BaseException {
        // 가격 없음
        try{
            int productPostId = productDao.createProductFree(postProductNewReq);
            return new PostProductNewRes(productPostId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public void modifyProductPostAtt(int postId, PutProductAttReq putProductAttReq) throws BaseException {
        try {
            if (productProvider.checkAtt(postId, putProductAttReq.getUserId()) == 0){
                // 관심 등록 하지 않은 게시글일 때
                try{
                    int attPostId = productDao.createProductAtt(postId, putProductAttReq);
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }
            } else {
                // 좋아요 한 댓글일 때
                try {
                    int result = productDao.modifyProductAtt(postId, putProductAttReq);
                    if (result == 0) {
                        throw new BaseException(MODIFY_FAIL_PRODUCT_ATTENTION);
                    }
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }

            }
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }

    }

    public PostDealRes createDeal(int postId, PostDealReq postDealReq) throws BaseException {
        try{
            int dealId = productDao.createDeal(postId, postDealReq);
            return new PostDealRes(dealId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteDeal(int postId, PostDealReq postDealReq) throws BaseException {
        try{
            if(productProvider.checkDeal(postId, postDealReq.getUserId()) == 0){
                throw new BaseException(FIND_FAIL_DEAL_USER);
            }
            int result = productDao.deleteDeal(postId, postDealReq.getUserId());
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_DEAL);
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
