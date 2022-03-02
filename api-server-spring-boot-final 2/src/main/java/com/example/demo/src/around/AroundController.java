package com.example.demo.src.around;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.around.model.Around;
import com.example.demo.src.around.model.PostAroundChatReq;
import com.example.demo.src.around.model.PostAroundChatRes;
import com.example.demo.src.town.model.PostTownComRes;
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
