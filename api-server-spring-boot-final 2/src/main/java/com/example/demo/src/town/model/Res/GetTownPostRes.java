package com.example.demo.src.town.model.Res;

import com.example.demo.src.town.model.TownCom;
import com.example.demo.src.town.model.TownComCom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetTownPostRes {
    private String categoryName;
    private int townPostId;
    private String content;
    private String userName;
    private String uploadTime;
    private int likeCount;
    private int comCount;
    private List<TownCom> townCom;
}
