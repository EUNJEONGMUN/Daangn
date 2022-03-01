package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Town {
    private int townPostId;
    private int userId;
    private int townPostCategoryId;
    private String content;
    private int townPostLocation;
    private Timestamp createdAt;

}
