package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserLikeStoreRes {
    private int storeId;
    private String storeImage;
    private String storeName;
    private String storeInfo;
    private String categoryName;
    private int reviewCount;
    private int likeUserCount;

}
