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
//    private String firstImg;
    private GetProductImg getProductImg;
    private GetProductInfo getProductInfo;
//    private int chatCount;
//    private int attCount;
    private GetProductChatCount getProductChatCount;
    private GetProductAttCount getProductAttCount;
}