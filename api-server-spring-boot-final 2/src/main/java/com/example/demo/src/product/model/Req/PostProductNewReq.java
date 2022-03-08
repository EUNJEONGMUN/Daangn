package com.example.demo.src.product.model.Req;

import com.example.demo.src.product.model.PostProductImg;
import com.example.demo.src.product.model.PostProductNew;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// 중고 거래 글을 쓸 때 필요한 것들
public class PostProductNewReq {
    private int userId;
    private String title;
    private int categoryId;
    private int jusoCodeId;
    private String isProposal;
    private String content;
    private int price;
//    private PostProductNew postProductNew;
//    private List<PostProductImg> postProductImgList;
}
