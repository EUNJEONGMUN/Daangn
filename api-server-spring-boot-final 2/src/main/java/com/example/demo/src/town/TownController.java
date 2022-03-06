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


    public TownController(TownProvider townProvider, TownService townService) {
        this.townProvider = townProvider;
        this.townService = townService;
    }

    /**
     * 동네 생활 전체 글 조회 API
     * [GET] /towns/home
     *
     * @return BaseResponse<List < GetTownRes>>
     */
    @ResponseBody
    @GetMapping("/home") // (GET) 127.0.0.1:9000/towns/home
    public BaseResponse<List<GetTownRes>> getTowns() {
        try {
            List<GetTownRes> getTownsRes = townProvider.getTowns();

            return new BaseResponse<>(getTownsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 카테고리별 조회 API
     * [GET] /towns/home/:categoryId
     *
     * @return BaseResponse<List < GetTownRes>>
     */
    @ResponseBody
    @GetMapping("/home/{categoryId}") // (GET) 127.0.0.1:9000/towns/home/:categoryId
    public BaseResponse<List<GetTownRes>> getTown(@PathVariable("categoryId") int categoryId) {
        try {
            List<GetTownRes> getTownRes = townProvider.getTown(categoryId);
            return new BaseResponse<>(getTownRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 글 작성 API
     * [POST] /towns/new/:userId
     *
     * @return BaseResponse<PostTownNewRes>
     */
    @ResponseBody
    @PostMapping("/new/{userId}") // (POST) 127.0.0.1:9000/towns/new
    public BaseResponse<PostTownNewRes> createTown(@PathVariable int userId, @RequestBody PostTownNewReq postTownNewReq) {

        if (postTownNewReq.getTownPostCategoryId() == 0) {
            return new BaseResponse<>(EMPTY_CATEGORY);
        }
        if (postTownNewReq.getContent() == null) {
            return new BaseResponse<>(POST_TOWNS_EMPTY_CONTENT);
        }

        try {
            PostTownNewRes postTownNewRes = townService.createTown(userId, postTownNewReq);
            return new BaseResponse<>(postTownNewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 글 수정 API
     * [PATCH] /towns/:postId/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{userId}")
    public BaseResponse<String> modifyTownPost(@PathVariable int postId, @PathVariable int userId, @RequestBody Town town) {
        try {
            PatchTownPostReq patchTownPostReq = new PatchTownPostReq(postId, userId, town.getTownPostCategoryId(),
                    town.getContent(), town.getTownPostLocation());
            townService.modifyTownPost(patchTownPostReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 글 삭제 API
     * [PATCH] /towns/:postId/:userId/deletion
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{userId}/deletion")
    public BaseResponse<String> deleteTownPost(@PathVariable int postId, @PathVariable int userId, @RequestBody PatchTownPostDel patchTownPostDel) {
        try {
            PatchTownPostDelReq patchTownPostDelReq = new PatchTownPostDelReq(postId, userId, patchTownPostDel.getStatus());
            townService.deleteTownPost(patchTownPostDelReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 댓글 작성 API
     * [POST] /towns/:postId/:userId/comment
     * @return BaseResponse<PostTownComRes>
     */
    @ResponseBody
    @PostMapping("/{postId}/{userId}/comment") // (POST) 127.0.0.1:9000/towns/:postId/comment
    public BaseResponse<PostTownComRes> createTownCom(@PathVariable int postId, @PathVariable int userId, @RequestBody PostTownComReq postTownComReq) {

        if (postTownComReq.getContent() == null) {
            return new BaseResponse<>(POST_TOWNS_EMPTY_CONTENT);
        }

        if (postTownComReq.getRefId() == 0) {
            // 대댓글 일 때
            try {
                PostTownComRes postTownComRes = townService.createTownComCom(postId, userId, postTownComReq);
                return new BaseResponse<>(postTownComRes);
            } catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }
        } else {
            // 댓글 일 때
            try {
                PostTownComRes postTownComRes = townService.createTownCom(postId, userId, postTownComReq);
                return new BaseResponse<>(postTownComRes);
            } catch (BaseException exception) {
                return new BaseResponse<>((exception.getStatus()));
            }
        }
    }

    /**
     * 동네 생활 댓글 수정 API
     * [PATCH] /towns/:postId/comment/:comId/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/comment/{comId}/{userId}")
    public BaseResponse<String> modifyTownCom(@PathVariable int postId, @PathVariable int comId, @PathVariable int userId,
                                              @RequestBody TownPostCom townPostCom) {
        try {
            PatchTownPostComReq patchTownPostComReq = new PatchTownPostComReq(postId, comId, userId, townPostCom.getContent());
            townService.modifyTownCom(patchTownPostComReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 댓글 삭제 API
     * [PATCH] /towns/:postId/comment/:comId/:userId/deletion
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/comment/{comId}/{userId}/deletion")
    public BaseResponse<String> deleteTownCom(@PathVariable int postId, @PathVariable int comId, @PathVariable int userId,
                                              @RequestBody TownPostComDel townPostComDel) {
        try {
            PatchTownComDelReq patchTownComDelReq = new PatchTownComDelReq(postId, comId, userId, townPostComDel.getStatus());
            townService.deleteTownCom(patchTownComDelReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 동네 생활 글 좋아요 설정 API
     * [POST] /towns/:postId/liked/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{postId}/liked/{userId}")
    public BaseResponse<String> createTownPostLiked(@PathVariable int postId, @PathVariable int userId, @RequestBody PostTownLikedReq postTownLikedReq) {
        try {
            if (townProvider.checkLiked(postId, userId) == 0) {
                // 좋아요 하지 않은 게시글일 때
                townService.createTownPostLiked(postId, userId, postTownLikedReq);
                String result = "";
                return new BaseResponse<>(result);

            } else {
                // 좋아요 한 기록이 있는 게시글일 때
                PatchTownLikedReq patchTownLikedReq = new PatchTownLikedReq(postId, userId, postTownLikedReq.getStatus());
                townService.modifyTownPostLiked(patchTownLikedReq);
                String result = "";
                return new BaseResponse<>(result);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 글 좋아요 변경 API
     * [PATCH] /towns/:postId/liked/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/liked/{userId}")
    public BaseResponse<String> modifyTownPostLiked(@PathVariable int postId, @PathVariable int userId, @RequestBody TownLiked townLiked) {
        try {

            PatchTownLikedReq patchTownLikedReq = new PatchTownLikedReq(postId, userId, townLiked.getStatus());
            townService.modifyTownPostLiked(patchTownLikedReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 댓글 좋아요 설정 API
     * [POST] /towns/:postId/:comId/liked/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{postId}/{comId}/liked/{userId}")
    public BaseResponse<String> createTownComLiked(@PathVariable int postId, @PathVariable int comId, @PathVariable int userId,
                                                   @RequestBody PostTownComLikedReq postTownComLikedReq) {
        try {
            if (townProvider.checkComLiked(postId, comId, userId) == 0) {
                // 좋아요 하지 않은 댓글일 때
                townService.createTownComLiked(postId, comId, userId, postTownComLikedReq);
                String result = "";
                return new BaseResponse<>(result);

            } else {
                // 좋아요 한 기록이 있는 댓글일 떄
                PatchTownComLikedReq patchTownComLikedReq = new PatchTownComLikedReq(postId, comId, userId, postTownComLikedReq.getStatus());
                townService.modifyTownComLiked(patchTownComLikedReq);
                String result = "";
                return new BaseResponse<>(result);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 댓글 좋아요 변경 API
     * [PATCH] /towns/:postId/:comId/liked/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{comId}/liked/{userId}")
    public BaseResponse<String> modifyTownComLiked(@PathVariable int postId, @PathVariable int comId, @PathVariable int userId,
                                                   @RequestBody TownComLiked townComLiked) {
        try {
            PatchTownComLikedReq patchTownComLikedReq = new PatchTownComLikedReq(postId, comId, userId, townComLiked.getStatus());
            townService.modifyTownComLiked(patchTownComLikedReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 작성한 동네 생활 글 조회 API
     * [GET] /towns/user-post/:userId
     * @return BaseResponse<List<GetTownRes>>
     */
    @ResponseBody
    @GetMapping("/user-post/{userId}")
    public BaseResponse<List<GetTownRes>> getUserTownPosts(@PathVariable int userId) {
        try {
            List<GetTownRes> getTownsRes = townProvider.getUserTownPosts(userId);

            return new BaseResponse<>(getTownsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 작성한 동네 생활 댓글 조회 API
     * [GET] /towns/user-comment/:userId
     * @return BaseResponse<List<GetTownComRes>>
     */
    @ResponseBody
    @GetMapping("/user-comment/{userId}")
    public BaseResponse<List<GetTownComRes>> getUserTownComs(@PathVariable int userId) {
        try {
            List<GetTownComRes> getTownComRes = townProvider.getUserTownComs(userId);

            return new BaseResponse<>(getTownComRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}