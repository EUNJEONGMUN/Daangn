package com.example.demo.src.town.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TownCom {
    private String content;
    private String userName;
    private String userImg;
    private String uploadTime;
    private String jusoName;
    private List<TownComCom> townComCom;
}
