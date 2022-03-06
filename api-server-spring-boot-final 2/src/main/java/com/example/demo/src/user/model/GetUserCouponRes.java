package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserCouponRes {
    private String storeProfileImage;
    private String storeName;
    private String categoryName;
    private int couponListId;
    private String couponName;
    private String endDate;
    private String time;
}
