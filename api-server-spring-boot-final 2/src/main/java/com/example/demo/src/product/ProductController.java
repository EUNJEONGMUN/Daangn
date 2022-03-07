package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.*;
import com.example.demo.src.product.model.Req.*;
import com.example.demo.src.product.model.Res.GetProductRes;
import com.example.demo.src.product.model.Res.PostProductNewRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;

    public ProductController(ProductProvider productProvider, ProductService productService){
        this.productProvider = productProvider;
        this.productService = productService;
    }

    /**
     * 홈 화면 조회 API
     * [GET] /products/home
     * @return BaseResponse<List<GetProductRes>>
     */

    @ResponseBody
    @GetMapping("/home") // (GET) 127.0.0.1:9000/products/home
    public BaseResponse<List<GetProductRes>> getProducts(){
        try {
            List<GetProductRes> getProductRes = productProvider.getProducts();
            return new BaseResponse<>(getProductRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 홈 화면 카테고리별 조회 API
     * [GET] /products/home/:categoryId
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/home/{categoryId}") // (GET) 127.0.0.1:9000/products/home/:categoryId
    public BaseResponse<List<GetProductRes>> getProduct(@PathVariable("categoryId") int categoryId){
        try{
            if (productProvider.checkTopCategory(categoryId) != 1){
                // 중고거래 refId=1
                return new BaseResponse<>(CATEGORY_RANGE_ERROR);
            }
            List<GetProductRes> getProductsRes = productProvider.getProduct(categoryId);
            return new BaseResponse<>(getProductsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 작성 API
     * [POST] /products/new
     * @return BaseResponse<PostProductNewRes>
     */
    @ResponseBody
    @PostMapping("/new") // (POST) 127.0.0.1:9000/products/new
    public BaseResponse<PostProductNewRes> createProduct(@RequestBody PostProductNewReq postProductNewReq){
        if (postProductNewReq.getTitle() == null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_TITLE);
        }

        if (postProductNewReq.getProductPostCategoryId() == 0){
            return new BaseResponse<>(BaseResponseStatus.EMPTY_CATEGORY);
        }
        if (postProductNewReq.getContent() == null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_CONTENT);
        }

        try{
                PostProductNewRes postProductNewRes = productService.createProduct(postProductNewReq);
                return new BaseResponse<>(postProductNewRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 수정 API
     * [PATCH] /products/:postId/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{userId}")
    public BaseResponse<String> modifyProduct(@PathVariable int postId, @PathVariable int userId, @RequestBody ProductPost productPost) {
        try {
            PatchPostReq patchPostReq = new PatchPostReq(postId, userId, productPost.getTitle(), productPost.getProductPostCategoryId(),
                    productPost.getProductPostLocation(), productPost.getIsProposal(), productPost.getContent(), productPost.getPrice(),
                    productPost.getState());

            productService.modifyProduct(patchPostReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 삭제 API
     * [PATCH] /products/:postId/:userId/deletion
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{userId}/deletion")
    public BaseResponse<String> deleteProduct(@PathVariable int postId, @PathVariable int userId, @RequestBody ProductPostDel productPostDel){
        try{
            PatchPostDelReq patchPostDelReq = new PatchPostDelReq(postId, userId, productPostDel.getIsExistence());
            productService.deleteProduct(patchPostDelReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 관심 등록 API
     * [POST] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{postId}/{userId}/attention")
    public BaseResponse<String> createProductAtt(@PathVariable int postId, @PathVariable int userId, @RequestBody PostProductAttReq postProductAttReq){
        try{
            if (postProductAttReq.getStatus() == null){
                return new BaseResponse<>(POST_ATTENTION_EMPTY_STATUS);
            }

            if (productProvider.checkAtt(postId, userId) != 0){
                // 이미 관심 등록한 기록이 있다면
                PatchProductAttReq patchProductAttReq = new PatchProductAttReq(postId, userId, postProductAttReq.getStatus());
                productService.modifyProductAtt(patchProductAttReq);
                String result = "";
                return new BaseResponse<>(result);

            } else {
                // 관심 등록한 기록이 없다면
                productService.createProductAtt(postId, userId, postProductAttReq);
                String result = "";
                return new BaseResponse<>(result);
            }

        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 관심 변경 API
     * [PATCH] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{userId}/attention")
    public BaseResponse<String> modifyProductAtt(@PathVariable int postId, @PathVariable int userId, @RequestBody PatchProductAtt patchProductAtt){
        try{
            if (patchProductAtt.getStatus() == null){
                return new BaseResponse<>(PATCH_ATTENTION_EMPTY_STATUS);
            }

            PatchProductAttReq patchProductAttReq = new PatchProductAttReq(postId, userId, patchProductAtt.getStatus());
            productService.modifyProductAtt(patchProductAttReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 중고 거래 성사 API
     * [POST] /products/:postId/:userId/deals
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping ("/{postId}/{userId}/deals")
    public BaseResponse<String> createDeal(@PathVariable int postId, @PathVariable int userId, @RequestBody PostDealReq postDealReq) {
        try {
            if (postDealReq.getStatus() == null) {
                return new BaseResponse<>(POST_DEAL_EMPTY_STATUS);
            }
            if (productProvider.checkDeal(postId, userId) != 0) {
                // 이미 거래 한 기록이 있다면
                PatchDealReq patchDealReq = new PatchDealReq(postId, userId, postDealReq.getStatus());
                productService.deleteDeal(patchDealReq);
                String result = "";
                return new BaseResponse<>(result);
            }
            productService.createDeal(postId, userId, postDealReq);
            String result = "";
            return new BaseResponse<>(result);


        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }

    /**
     * 중고 거래 취소 API
     * [PATCH] /products/:postId/:userId/deals/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping ("/{postId}/{userId}/deals/deletion")
    public BaseResponse<String> deleteDeal(@PathVariable int postId, @PathVariable int userId, @RequestBody Deal deal){
        try{

            if (productProvider.checkDeal(postId, userId) == 0) {
                return new BaseResponse<>(POST_DEAL_EMPTY);
            }

            PatchDealReq patchDealReq = new PatchDealReq(postId, userId, deal.getStatus());
            productService.deleteDeal(patchDealReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 판매 내역 상태별 조회 API
     * [GET] /products/user-post/:userId
     * /:userId?status=?
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/user-post/{userId}/{status}")
    public BaseResponse<List<GetProductRes>> getUserProductPost(@PathVariable int userId, @PathVariable int status){
        try{
            if (status == 0){
                List<GetProductRes> getProductsRes = productProvider.getUserProductPost(userId, 1);
                return new BaseResponse<>(getProductsRes);
            }
            List<GetProductRes> getProductsRes = productProvider.getUserProductPost(userId, status);
            return new BaseResponse<>(getProductsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 구매 내역 조회 API
     * [GET] /products/buylist/:userId
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/buylist/{userId}")
    public BaseResponse<List<GetProductRes>> getUserBuyList(@PathVariable int userId){
        try{
            List<GetProductRes> getProductsRes = productProvider.getUserBuyList(userId);
            return new BaseResponse<>(getProductsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
