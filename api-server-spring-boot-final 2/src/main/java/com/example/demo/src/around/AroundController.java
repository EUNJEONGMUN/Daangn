package com.example.demo.src.around;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;

import com.example.demo.src.around.model.*;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/around")
public class AroundController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private final AroundProvider aroundProvider;
    @Autowired
    private final AroundService aroundService;

    public AroundController(AroundProvider aroundProvider, AroundService aroundService){
        this.aroundProvider = aroundProvider;
        this.aroundService = aroundService;
    }

    @ResponseBody
    @PostMapping("/new")  // (POST) 127.0.0.1:9000/around/new
    public BaseResponse<PostAroundNewRes> createAround(@RequestBody PostAroundNewReq postAroundNewReq){
        if (postAroundNewReq.getTitle() == null){
            return new BaseResponse<>(POST_AROUND_EMPTY_TITLE);
        }
        if(postAroundNewReq.getContent() == null){
            return new BaseResponse<>(POST_AROUND_EMPTY_CONTENT);
        }

        try{
            PostAroundNewRes postAroundNewRes = aroundService.createAround(postAroundNewReq);
            return new BaseResponse<>(postAroundNewRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("/{postId}/chat")
    public BaseResponse<PostAroundChatRes> postChat(@PathVariable int postId, @RequestBody PostAroundChatReq postAroundChatReq){

        if (postAroundChatReq.getContent() == null && postAroundChatReq.getEmotion()==null){
            return new BaseResponse<>(POST_AROUND_CHAT_EMPTY_CONTENT);
        }

        try{
            PostAroundChatRes postAroundChatRes = aroundService.postChat(postId, postAroundChatReq);
            return new BaseResponse<>(postAroundChatRes);
        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }



    }
}
