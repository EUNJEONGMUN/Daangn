package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.town.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/towns")
public class TownController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final TownProvider townProvider;
    @Autowired
    private final TownService townService;
    @Autowired


    public TownController(TownProvider townProvider, TownService townService){
        this.townProvider = townProvider;
        this.townService = townService;
    }

    @ResponseBody
    @GetMapping("/home") // (GET) 127.0.0.1:9000/towns/home
    public BaseResponse<List<GetTownRes>> getTowns(){
        try{
            List<GetTownRes> getTownsRes = townProvider.getTowns();

            return new BaseResponse<>(getTownsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/home/{categoryId}") // (GET) 127.0.0.1:9000/towns/home/:categoryId
    public BaseResponse<List<GetTownRes>> getTown(@PathVariable("categoryId") int categoryId){
        try{
            List<GetTownRes> getTownRes = townProvider.getTown(categoryId);
            return new BaseResponse<>(getTownRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/new") // (POST) 127.0.0.1:9000/towns/new
    public BaseResponse<PostTownNewRes> createTown(@RequestBody PostTownNewReq postTownNewReq){

        if (postTownNewReq.getTownPostCategoryId() == 0){
            return new BaseResponse<>(POST_TOWNS_EMPTY_CATEGORY);
        }
        if (postTownNewReq.getContent() == null){
            return new BaseResponse<>(POST_TOWNS_EMPTY_CONTENT);
        }

        try{
            PostTownNewRes postTownNewRes = townService.createTown(postTownNewReq);
            return new BaseResponse<>(postTownNewRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }


    }

}
