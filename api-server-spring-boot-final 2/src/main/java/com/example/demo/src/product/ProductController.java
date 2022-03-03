package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.*;
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

    @ResponseBody
    @GetMapping("/home") // (GET) 127.0.0.1:9000/products/home
    public BaseResponse<List<GetProductRes>> getProducts(){
        try{
            List<GetProductRes> getProductsRes = productProvider.getProducts();

            return new BaseResponse<>(getProductsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/home/{categoryId}") // (GET) 127.0.0.1:9000/products/home/:categoryId
    public BaseResponse<List<GetProductRes>> getProduct(@PathVariable("categoryId") int categoryId){
        try{
            if (categoryId<=4 || categoryId>=22){
                return new BaseResponse<>(BaseResponseStatus.CATEGORY_RANGE_ERROR);
            }
            List<GetProductRes> getProductsRes = productProvider.getProduct(categoryId);
            return new BaseResponse<>(getProductsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

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
            if (postProductNewReq.getPrice() == 0){
                PostProductNewRes postProductNewRes = productService.createProductFree(postProductNewReq);
                return new BaseResponse<>(postProductNewRes);

            } else{
                PostProductNewRes postProductNewRes = productService.createProduct(postProductNewReq);
                return new BaseResponse<>(postProductNewRes);
            }


        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PutMapping("/{postId}/attention")
    public BaseResponse<String> modifyProductPostAtt(@PathVariable int postId, @RequestBody PutProductAttReq putProductAttReq){
        try{
            productService.modifyProductPostAtt(postId, putProductAttReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping ("/{postId}/deals")
    public BaseResponse<PostDealRes> createDeal(@PathVariable int postId, @RequestBody PostDealReq postDealReq) {
        try {
            if (postDealReq.getUserId() == 0) {
                return new BaseResponse<>(POST_DEALS_EMPTY_USERID);
            }

            PostDealRes postDealRes = productService.createDeal(postId, postDealReq);
            return new BaseResponse<>(postDealRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }

//    @ResponseBody
//    @DeleteMapping("/{postId}/deals")
//    public BaseResponse<String> deleteDeal(@PathVariable int postId, @RequestBody PostDealReq postDealReq){
//        try{
//            if (postDealReq.getUserId() == 0) {
//                return new BaseResponse<>(POST_DEALS_EMPTY_USERID);
//            }
//            productService.deleteDeal(postId, postDealReq);
//            String result = "";
//            return new BaseResponse<>(result);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }



}
