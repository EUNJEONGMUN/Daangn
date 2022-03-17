package com.example.demo.src.product.model.Res;

import com.example.demo.src.product.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductPostRes {

//    private String title;
//    private int price;
//    private String state;
//    private String uploadTime;

    private List<GetProductInfo> getProductInfo;
    private List<GetProductImg> getProductImg;
    private List<GetProductJuso> getProductJuso;
    private List<GetProductAttCount> getProductAttCount;
    private List<GetProductChatCount> getProductChatCount;

    //    private String firstImg;
//    private String jusoName;
//    private int attCount;
//    private int chatCount;


    // 나름 성공??
    //    private GetProductInfo getProductInfo;
//    private List<String> image;
//    private List<Integer> att;
//    private List<Integer> chat;
//    private List<String> juso;
////    public GetProductPostRes(GetProductInfo getProductInfo, List<String> image, List<Integer> att, List<Integer> chat, List<String> juso) {
//        this.getProductInfo = getProductInfo;
//        this.image = image;
//        this.att = att;
//        this.chat = chat;
//        this.juso = juso;
//
//    }
}
