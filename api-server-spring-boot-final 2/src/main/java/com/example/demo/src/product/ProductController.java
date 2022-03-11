package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
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
     * [POST] /products/new/:userId
     * @return BaseResponse<PostProductNewRes>
     */
    @ResponseBody
    @PostMapping("/new/{userId}")
    public BaseResponse<PostProductNewRes> createProduct(@PathVariable int userId, @RequestBody PostProductNew postProductNew){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

        if (postProductNew.getTitle() == null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_TITLE);
        }

        if (postProductNew.getCategoryId() == 0){
            return new BaseResponse<>(EMPTY_CATEGORY);
        }
        if (postProductNew.getContent() == null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_CONTENT);
        }

        try{
            PostProductNewReq postProductNewReq = new PostProductNewReq(userId, postProductNew.getTitle(), postProductNew.getCategoryId(), postProductNew.getIsProposal(), postProductNew.getContent(), postProductNew.getPrice());
            PostProductNewRes postProductNewRes = productService.createProduct(postProductNewReq);
            return new BaseResponse<>(postProductNewRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
//    @ResponseBody
//    @PostMapping("/new") // (POST) 127.0.0.1:9000/products/new
//    public BaseResponse<PostProductNewRes> createProduct(@RequestBody String postProductNewReq){
//        String title = "";
//        String categoryId = "";
//        String content = "";
//        try {
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObj = (JSONObject)jsonParser.parse(postProductNewReq);
//            title = (String)jsonObj.get("title");
//            categoryId = (String)jsonObj.get("categoryId");
//            content = (String)jsonObj.get("content");
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (title == null){
//            return new BaseResponse<>(POST_PRODUCTS_EMPTY_TITLE);
//        }
//
//        if (categoryId == null){
//            return new BaseResponse<>(BaseResponseStatus.EMPTY_CATEGORY);
//        }
//        if (content == null){
//            return new BaseResponse<>(POST_PRODUCTS_EMPTY_CONTENT);
//        }
//
//        try {
//            JSONParser jsonParser = new JSONParser();
//            JSONObject jsonObj = (JSONObject)jsonParser.parse(postProductNewReq);
//            PostProductNewRes postProductNewRes = productService.createProduct(jsonObj);
//            return new BaseResponse<>(postProductNewRes);
//        } catch (ParseException | BaseException e) {
//            e.printStackTrace();
//        }
//
//        return new BaseResponse<>(DATABASE_ERROR);
//    }
//



    /**
     * 중고 거래 글 수정 API
     * [PATCH] /products/:postId/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{userId}")
    public BaseResponse<String> modifyProduct(@PathVariable int postId, @PathVariable int userId, @RequestBody ProductPost productPost) {
        try {

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if (productPost.getTitle() == null){
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_TITLE);
            }

            if (productPost.getCategoryId() == 0){
                return new BaseResponse<>(BaseResponseStatus.EMPTY_CATEGORY);
            }
            if (productPost.getContent() == null){
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_CONTENT);
            }

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
     * 중고 거래 성사 API
     * [POST] /products/:postId/:userId/deals
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping ("/{postId}/{userId}/deals")
    public BaseResponse<String> createDeal(@PathVariable int postId, @PathVariable int userId, @RequestBody PostDealReq postDealReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (postDealReq.getStatus() == null) {
                return new BaseResponse<>(POST_DEAL_EMPTY_STATUS);
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
     * [PATCH] /products/:postId/:userId/deals/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping ("/{postId}/{userId}/deals")
    public BaseResponse<String> deleteDeal(@PathVariable int postId, @PathVariable int userId, @RequestBody Deal deal){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            System.out.println(deal.getStatus());
            if (deal.getStatus()==null) {
                return new BaseResponse<>(POST_DEAL_EMPTY_STATUS);
            }

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
     * 중고 거래 글 관심 등록 API
     * [POST] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{postId}/{userId}/attention")
    public BaseResponse<String> createProductAtt(@PathVariable int postId, @PathVariable int userId, @RequestBody PostProductAttReq postProductAttReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (postProductAttReq.getStatus() == null) {
                return new BaseResponse<>(POST_ATTENTION_EMPTY_STATUS);
            }
            if (postProductAttReq.getStatus().equals("N")){
                return new BaseResponse<>(NOT_CORRECT_STATUS);
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
     * [PATCH] /products/:postId/:userId/attention
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{userId}/attention")
    public BaseResponse<String> modifyProductAtt(@PathVariable int postId, @PathVariable int userId, @RequestBody PatchProductAtt patchProductAtt){
        try{

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (patchProductAtt.getStatus() == null){
                return new BaseResponse<>(PATCH_ATTENTION_EMPTY_STATUS);
            }
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
     * 판매 내역 상태별 조회 API
     * [GET] /products/user-post/:userId
     * /:userId?status=?
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/user-post/{userId}")
    public BaseResponse<List<GetProductListRes>> getUserProductPost(@PathVariable int userId, @RequestParam(required = false) String status){
        try{

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

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
     * [GET] /products/buylist/:userId
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/buylist/{userId}")
    public BaseResponse<List<GetProductListRes>> getUserBuyList(@PathVariable int userId){
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userId != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

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



    /**
     * 로그 테스트 API
     * [GET] /test/log
     * @return String
     */
    @ResponseBody
    @GetMapping("/log")
    public String getAll() {
        System.out.println("테스트");
//        trace, debug 레벨은 Console X, 파일 로깅 X
//        logger.trace("TRACE Level 테스트");
//        logger.debug("DEBUG Level 테스트");

//        info 레벨은 Console 로깅 O, 파일 로깅 X
        logger.info("INFO Level 테스트");
//        warn 레벨은 Console 로깅 O, 파일 로깅 O
        logger.warn("Warn Level 테스트");
//        error 레벨은 Console 로깅 O, 파일 로깅 O (app.log 뿐만 아니라 error.log 에도 로깅 됨)
//        app.log 와 error.log 는 날짜가 바뀌면 자동으로 *.gz 으로 압축 백업됨
        logger.error("ERROR Level 테스트");

        return "Success Test";
    }


}
