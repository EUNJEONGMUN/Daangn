package com.example.demo.src.store.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostStoreReq {

    private int userId;
    private String storeName;
    private String storeInfo;
    private String storePhoneNumber;
    private int storeCategoryId;
//    private int storeLocationId;
    private String storeSiteUrl;
    private String storeProfileImage;
    private String juso;

    public PostStoreReq(){

    }
}
