package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PostProductImg;
import com.example.demo.src.product.model.PostProductNew;
import com.example.demo.src.product.model.Req.*;
import com.example.demo.src.product.model.Res.PostProductNewRes;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final int FAIL = 0;

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
//    public PostProductNewRes createProduct(JSONObject jsonObj) throws BaseException {
//
//        try{
//            int productPostId = productDao.createProduct(jsonObj);
//            return new PostProductNewRes(productPostId);
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }

//    }
    /**
     * 중고 거래 글 수정 API
     * [PATCH] /products/:postId/:userId
     * @return BaseResponse<String>
     */
    public void modifyProduct(PatchPostReq patchPostReq) throws BaseException {
        try {
            int result = productDao.modifyProduct(patchPostReq);
            if (result == FAIL){
                throw new BaseException(MODIFY_FAIL_PRODUCT_POST);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    /**
//     * 중고 거래 글 삭제 API
//     * [PATCH] /products/:postId/:userId/status
//     * @return BaseResponse<String>
//     */
//    public void deleteProduct(PatchPostDelReq patchPostDelReq) throws BaseException {
//        try {
//            int result = productDao.deleteProduct(patchPostDelReq);
//            if (result == 0){
//                throw new BaseException(DELETE_FAIL_PRODUCT_POST);
//            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }


    /**
     * 중고 거래 글 관심 등록 API
     * [POST] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    public void createProductAtt(int postId, int userId, String status) throws BaseException {
        try {
            int result = productDao.createProductAtt(postId, userId, status);
            if (result == FAIL) {
                throw new BaseException(CREATE_FAIL_PRODUCT_ATTENTION);
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
            if(result == FAIL){
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
            if (result==FAIL){
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
            if (result==FAIL){
                throw new BaseException(DELETE_FAIL_DEAL);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
