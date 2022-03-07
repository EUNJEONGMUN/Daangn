package com.example.demo.src.product.model.Req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchProductAttReq {
    private int postId;
    private int userId;
    private String status;

    public PatchProductAttReq(){

    }
}
