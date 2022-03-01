package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.town.model.*;
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
            if (categoryId<=21 || categoryId>=39){
                return new BaseResponse<>(TOWNS_CATEGORY_ERROR);
            }
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

    @ResponseBody
    @PostMapping("/{postId}/comment") // (POST) 127.0.0.1:9000/towns/:postId/comment
    public BaseResponse<PostTownComRes> createTownCom(@PathVariable("postId") int postId, @RequestBody PostTownComReq postTownComReq){

        if (postTownComReq.getContent() == null){
            return new BaseResponse<>(POST_TOWNS_EMPTY_CONTENT);
        }

        if (postTownComReq.getRefId() == 0){
            // 대댓글 일 때
            try{
                PostTownComRes postTownComRes = townService.createTownComCom(postId, postTownComReq);
                return new BaseResponse<>(postTownComRes);
            } catch (BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }

        } else {
            // 댓글 일 때
            try{
                PostTownComRes postTownComRes = townService.createTownCom(postId,postTownComReq);
                return new BaseResponse<>(postTownComRes);
            } catch (BaseException exception){
                return new BaseResponse<>((exception.getStatus()));
            }

        }



    }

    @ResponseBody
    @PutMapping("/{postId}/liked")
    public BaseResponse<String>  modifyTownPostLiked(@PathVariable("postId") int postId, @RequestBody PutTownLikedReq putTownLikedReq){

        try {
            townService.modifyTownPostLiked(postId, putTownLikedReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }
    }

    @ResponseBody
    @PutMapping("/{postId}/{comId}/liked")
    public BaseResponse<String> modifyTownComLiked(@PathVariable int postId, @PathVariable int comId, @RequestBody PutTownComLikedReq putTownComLikedReq){

        try {
            townService.modifyTownComLiked(postId, comId, putTownComLikedReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));

        }

    }



//    @ResponseBody
//    @PatchMapping("/{postId}")

//    public BaseResponse<String> modifyTownPost(@PathVariable("postId") int postId, @RequestBody Town town){
//        try{
//            // 접근한 유저가 같은지 확인
//        }
//        //같다면 글 네임 변경
//        PatchTownPostReq patchTownPostReq = new PatchTownPostReq()
//    }

}
