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

    /**
     * 중고 거래 글 작성 API
     * [POST] /products/new
     * @return BaseResponse<PostProductNewRes>
     */
    public PostProductNewRes createProduct(PostProductNewReq postProductNewReq) throws BaseException {

        try{
            int productPostId = productDao.createProduct(postProductNewReq);
            return new PostProductNewRes(productPostId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /**
     * 중고 거래 글 수정 API
     * [PATCH] /products/:postId/:userId
     * @return BaseResponse<String>
     */
    public void modifyProduct(PatchPostReq patchPostReq) throws BaseException {
        try {
            int result = productDao.modifyProduct(patchPostReq);
            if (result == 0){
                throw new BaseException(MODIFY_FAIL_PRODUCT_POST);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 중고 거래 글 삭제 API
     * [PATCH] /products/:postId/:userId/status
     * @return BaseResponse<String>
     */
    public void deleteProduct(PatchPostDelReq patchPostDelReq) throws BaseException {
        try {
            int result = productDao.deleteProduct(patchPostDelReq);
            if (result == 0){
                throw new BaseException(DELETE_FAIL_PRODUCT_POST);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 중고 거래 글 관심 등록 API
     * [POST] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    public void createProductAtt(int postId, int userId, PostProductAttReq postProductAttReq) throws BaseException {
        try {
            int result = productDao.createProductAtt(postId, userId, postProductAttReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_PRODUCT_ATTENTION);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /**
     * 중고 거래 글 관심 변경 API
     * [PATCH] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    public void modifyProductAtt(PatchProductAttReq patchProductAttReq) throws BaseException {
        try {
            int result = productDao.modifyProductAtt(patchProductAttReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_PRODUCT_ATTENTION);
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }


    /**
     * 중고 거래 성사 API
     * [POST] /products/:postId/:userId/deals
     * @return BaseResponse<PostDealRes>
     */
    public void createDeal(int postId, int userId, PostDealReq postDealReq) throws BaseException {
        try{
            int result = productDao.createDeal(postId, userId, postDealReq);
            if (result==0){
                throw new BaseException(CREATE_FAIL_DEAL);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 중고 거래 취소 API
     * [PATCH] /products/:postId/:userId/status
     * @return BaseResponse<String>
     */
    public void deleteDeal(PatchDealReq patchDealReq) throws BaseException {
        try{
            int result = productDao.deleteDeal(patchDealReq);
            if (result==0){
                throw new BaseException(DELETE_FAIL_DEAL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
