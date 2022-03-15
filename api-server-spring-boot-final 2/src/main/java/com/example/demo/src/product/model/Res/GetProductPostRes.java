package com.example.demo.src.product.model.Res;

import com.example.demo.src.product.model.GetProductAttCount;
import com.example.demo.src.product.model.GetProductChatCount;
import com.example.demo.src.product.model.GetProductImg;
import com.example.demo.src.product.model.GetProductInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetProductPostRes {

    private String title;
    private int price;
    private String uploadTime;
    private String state;
    private String firstImg;
    private String jusoName;
    private int attCount;
    private int chatCount;



////    private String firstImg;
//    private GetProductImg getProductImg;
//    private GetProductInfo getProductInfo;
//////    private int chatCount;
//////    private int attCount;
//    private GetProductChatCount getProductChatCount;
//    private GetProductAttCount getProductAttCount;
//
//private List<String> imgList;
//private List<Integer> chatList;
//private List<Integer> attList;
//private List<GetProductInfo> getProductInfoList;
}
