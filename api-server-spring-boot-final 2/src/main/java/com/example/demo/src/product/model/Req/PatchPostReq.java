package com.example.demo.src.product.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchPostReq {
    private int postId;
    private int userId;
    private String title;
    private int productPostCategoryId;
    private int productPostLocation;
    private String isProposal;
    private String content;
    private int price;
    private String status;
    private String isHidden;
    private String isExistence;
}
