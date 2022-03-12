package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.UnAuth;
import com.example.demo.src.product.model.*;
import com.example.demo.src.product.model.Req.*;
import com.example.demo.src.product.model.Res.GetProductListRes;
import com.example.demo.src.product.model.Res.GetProductRes;
import com.example.demo.src.product.model.Res.PostProductNewRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /**
     * 홈 화면 조회 API
     * [GET] /products/home
     * @return BaseResponse<List<GetProductRes>>
     */
    @UnAuth
    @ResponseBody
    @GetMapping("/home") // (GET) 127.0.0.1:9000/products/home
    public BaseResponse<List<GetProductListRes>> getProducts(){
        try {
            List<GetProductListRes> getProductListRes = productProvider.getProducts();
            return new BaseResponse<>(getProductListRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
//    @ResponseBody
//    @GetMapping("/home") // (GET) 127.0.0.1:9000/products/home
//    public BaseResponse<List<GetProductPostRes>> getProducts(){
//        try {
//            GetProductPostRes getProductPostRes = (GetProductPostRes) productProvider.getProducts();
//            return new BaseResponse(getProductPostRes);
//        } catch (BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
    /**
     * 홈 화면 카테고리별 조회 API
     * [GET] /products/home/:categoryId
     * @return BaseResponse<List<GetProductRes>>
     */
    @UnAuth
    @ResponseBody
    @GetMapping("/home/{categoryId}") // (GET) 127.0.0.1:9000/products/home/:categoryId
    public BaseResponse<List<GetProductListRes>> getProduct(@PathVariable("categoryId") int categoryId){
        try{
            if (productProvider.checkTopCategory(categoryId) != 1){
                // 중고거래 refId=1
                return new BaseResponse<>(CATEGORY_RANGE_ERROR);
            }
            List<GetProductListRes> getProductsRes = productProvider.getProduct(categoryId);
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
    @PostMapping("/post/new")
    public BaseResponse<PostProductNewRes> createProduct(HttpServletRequest request, @Valid @RequestBody PostProductNew postProductNew){

        int userId = (int) request.getAttribute("userId");

        try{
            PostProductNewReq postProductNewReq = new PostProductNewReq(userId, postProductNew.getTitle(), postProductNew.getCategoryId(), postProductNew.getIsProposal(), postProductNew.getContent(), postProductNew.getPrice());
            PostProductNewRes postProductNewRes = productService.createProduct(postProductNewReq);
            return new BaseResponse<>(postProductNewRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 수정 API
     * [PATCH] /products/:postId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/post/{postId}")
    public BaseResponse<String> modifyProduct(HttpServletRequest request, @PathVariable int postId, @Valid @RequestBody ProductPost productPost) {
        try {

            int userId = (int) request.getAttribute("userId");

            if (productProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }

            PatchPostReq patchPostReq = new PatchPostReq(postId, userId, productPost.getTitle(), productPost.getCategoryId(),
                    productPost.getJusoCodeId(), productPost.getIsProposal(), productPost.getContent(), productPost.getPrice(),
                    productPost.getStatus(), productPost.getIsHidden(), productPost.getIsExistence());

            productService.modifyProduct(patchPostReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 관심 등록 API
     * [POST] /products/:postId/attention
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{postId}/attention")
    public BaseResponse<String> createProductAtt(HttpServletRequest request, @PathVariable int postId, @Valid @RequestBody PostProductAttReq postProductAttReq) {
        try {
            int userId = (int) request.getAttribute("userId");

            if (postProductAttReq.getStatus().equals("N")){
                return new BaseResponse<>(NOT_CORRECT_STATUS);
            }
            if (productProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }

            if (productProvider.checkAtt(postId, userId) != 0) {
                // 이미 관심 등록한 기록이 있다면
                if (productProvider.checkAttStatus(postId, userId).equals("Y")){
                    return new BaseResponse<>(POST_ATTENTION_DUPLICATED);
                }
                PatchProductAttReq patchProductAttReq = new PatchProductAttReq(postId, userId, postProductAttReq.getStatus());
                productService.modifyProductAtt(patchProductAttReq);
                String result = "";
                return new BaseResponse<>(result);

            } else {
                // 관심 등록한 기록이 없다면
                productService.createProductAtt(postId, userId, postProductAttReq.getStatus());
                String result = "";
                return new BaseResponse<>(result);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 관심 변경 API
     * [PATCH] /products/:postId/attention
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/attention/deletion")
    public BaseResponse<String> modifyProductAtt(HttpServletRequest request, @PathVariable int postId, @Valid @RequestBody PatchProductAtt patchProductAtt){
        try{

            int userId = (int) request.getAttribute("userId");

            if (productProvider.checkAtt(postId, userId) == 0){
                // 관심 등록한 기록이 없다면
                return new BaseResponse<>(POST_ATTENTION_EMPTY);
            }

            if (patchProductAtt.getStatus().equals("N") && productProvider.checkAttStatus(postId, userId).equals("N")){
                return new BaseResponse<>(POST_ATTENTION_DEL_DUPLICATED);
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
    @PostMapping ("/{postId}/deals")
    public BaseResponse<String> createDeal(HttpServletRequest request, @PathVariable int postId, @Valid @RequestBody PostDealReq postDealReq) {
        try {
            int userId = (int) request.getAttribute("userId");

            if (postDealReq.getStatus().equals("N")){
                return new BaseResponse<>(NOT_CORRECT_STATUS);
            }

            if (productProvider.checkDeal(postId) != 0) {
                // 이미 거래된 글이라면
                return new BaseResponse<>(POST_DEAL_DUPLICATE);
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
     * [PATCH] /products/:postId/deals/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping ("/{postId}/deals")
    public BaseResponse<String> deleteDeal(HttpServletRequest request, @PathVariable int postId, @Valid @RequestBody Deal deal){
        try{
            int userId = (int) request.getAttribute("userId");

            if (productProvider.checkDeal(postId) == 0) {
                return new BaseResponse<>(POST_DEAL_EMPTY);
            }

            if (productProvider.checkDealUser(postId, userId).equals(deal.getStatus())){
                return new BaseResponse<>(POST_DEAL_DUPLICATE_STATE);
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
    @GetMapping("/user-post")
    public BaseResponse<List<GetProductListRes>> getUserProductPost(HttpServletRequest request, @RequestParam(required = false) String status){
        try{

            int userId = (int) request.getAttribute("userId");

            if (status == null){
                List<GetProductListRes> getProductsRes = productProvider.getUserProductPost(userId, "doing");
                return new BaseResponse<>(getProductsRes);
            }

            if (!(status.equals("doing") || status.equals("finish") || status.equals("hidden"))){
                return new BaseResponse<>(NOT_CORRECT_STATUS);
            }
            List<GetProductListRes> getProductsRes = productProvider.getUserProductPost(userId, status);
            return new BaseResponse<>(getProductsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 구매 내역 조회 API
     * [GET] /products/buylist
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/buylist")
    public BaseResponse<List<GetProductListRes>> getUserBuyList(HttpServletRequest request){
        try{
            int userId = (int) request.getAttribute("userId");

            List<GetProductListRes> getProductsRes = productProvider.getUserBuyList(userId);
            return new BaseResponse<>(getProductsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 중고 거래 글 조회 API
     * [GET] /products/:postId
     * @return BaseResponse<GetProductRes>
     */
    @UnAuth
    @ResponseBody
    @GetMapping("/{postId}") // (GET) 127.0.0.1:9000/products/home
    public BaseResponse<GetProductRes> getPost(@PathVariable int postId){
        try {
            if(postId==0){
                return new BaseResponse<>(EMPTY_POSTID);
            }

            if (productProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }

            GetProductRes getProductRes = productProvider.getPost(postId);
            return new BaseResponse<>(getProductRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
