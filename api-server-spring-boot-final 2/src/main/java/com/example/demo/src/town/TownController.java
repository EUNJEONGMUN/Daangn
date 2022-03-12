package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.UnAuth;
import com.example.demo.src.town.model.*;
import com.example.demo.src.town.model.Req.*;
import com.example.demo.src.town.model.Res.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    private final JwtService jwtService;


    public TownController(TownProvider townProvider, TownService townService, JwtService jwtService) {
        this.townProvider = townProvider;
        this.townService = townService;
        this.jwtService = jwtService;
    }

    /**
     * 동네 생활 전체 글 조회 API
     * [GET] /towns/home
     *
     * @return BaseResponse<List <GetTownRes>>
     */
    @UnAuth
    @ResponseBody
    @GetMapping("/home") // (GET) 127.0.0.1:9000/towns/home
    public BaseResponse<List<GetTownListRes>> getTowns() {
        try {
            List<GetTownListRes> getTownsRes = townProvider.getTowns();

            return new BaseResponse<>(getTownsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 카테고리별 조회 API
     * [GET] /towns/home/:categoryId
     *
     * @return BaseResponse<List<GetTownListRes>>
     */
    @UnAuth
    @ResponseBody
    @GetMapping("/home/{categoryId}") // (GET) 127.0.0.1:9000/towns/home/:categoryId
    public BaseResponse<List<GetTownListRes>> getTown(@PathVariable("categoryId") int categoryId) {
        try {
            if (townProvider.checkTopCategory(categoryId)!=2){
                return new BaseResponse<>(CATEGORY_RANGE_ERROR);
            }

            List<GetTownListRes> getTownListRes = townProvider.getTown(categoryId);
            return new BaseResponse<>(getTownListRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 글 개별 조회 API
     * [GET] /towns/:postId
     * @return BaseResponse<GetTownPostRes>
     */
    @UnAuth
    @ResponseBody
    @GetMapping("/{postId}")
    public BaseResponse<GetTownPostRes> getTownPost(@PathVariable int postId) {
        try {
            if (postId == 0){
                return new BaseResponse<>(EMPTY_POSTID);
            }

            if (townProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }

            GetTownPostRes getTownPostRes = townProvider.getTownPost(postId);
            return new BaseResponse<>(getTownPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 동네 생활 글 작성 API
     * [POST] /towns/post/new
     * @return BaseResponse<PostTownNewRes>
     */
    @ResponseBody
    @PostMapping("/post/new")
    public BaseResponse<PostTownNewRes> createTown(HttpServletRequest request, @Valid @RequestBody PostTownNew postTownNew) {
        try {

            int userId = (int) request.getAttribute("userId");

            if (townProvider.checkTopCategory(postTownNew.getTownPostCategoryId())!=2){
                return new BaseResponse<>(CATEGORY_RANGE_ERROR);
            }
            PostTownNewReq postTownNewReq = new PostTownNewReq(userId, postTownNew.getTownPostCategoryId(), postTownNew.getContent());
            PostTownNewRes postTownNewRes = townService.createTown(postTownNewReq);
            return new BaseResponse<>(postTownNewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 글 수정 API
     * [PATCH] /towns/post/:postId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/post/{postId}")
    public BaseResponse<String> modifyTownPost(HttpServletRequest request, @PathVariable int postId, @Valid @RequestBody Town town) {
        try {
            int userId = (int) request.getAttribute("userId");
            // 글 존재여부 확인
            if (townProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }
            // 자신의 글인지 확인
            if(townProvider.checkPostUser(postId)!=userId){
                return new BaseResponse<>(INVALID_USER_POST);
            }

            if (townProvider.checkTopCategory(town.getTownPostCategoryId())!=2){
                return new BaseResponse<>(CATEGORY_RANGE_ERROR);
            }

            System.out.println(postId);
            PatchTownPostReq patchTownPostReq = new PatchTownPostReq(postId, userId, town.getTownPostCategoryId(),
                    town.getContent(), town.getStatus());
            townService.modifyTownPost(patchTownPostReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 동네 생활 댓글 작성 API
     * [POST] /towns/:postId/comment/:userId
     * @return BaseResponse<PostTownComRes>
     */
    @ResponseBody
    @PostMapping("/{postId}/comment") // (POST) 127.0.0.1:9000/towns/:postId/comment
    public BaseResponse<PostTownComRes> createTownCom(HttpServletRequest request, @PathVariable int postId, @Valid @RequestBody PostTownComReq postTownComReq) {

        try{
            int userId = (int) request.getAttribute("userId");

            // 글 존재여부 확인
            if (townProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }
            // 대댓글일 때, 상위 댓글 존재 여부 확인
            if (postTownComReq.getRefId() != 0 && townProvider.checkComExists(postTownComReq.getRefId())==0){
                return new BaseResponse<>(POST_COM_NOT_EXISTS);
            }

            PostTownComRes postTownComRes = townService.createTownCom(postId, userId, postTownComReq);
            return new BaseResponse<>(postTownComRes);

        } catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 동네 생활 댓글 수정 API
     * [PATCH] /towns/:postId/comment/:comId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/comment/{comId}")
    public BaseResponse<String> modifyTownCom(HttpServletRequest request, @PathVariable int postId, @PathVariable int comId,
                                              @Valid @RequestBody TownPostCom townPostCom) {
        try {
            int userId = (int) request.getAttribute("userId");


            // 글 존재여부 확인
            if (townProvider.checkPostExists(postId)==0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }

            // 댓글 존재여부 확인
            if (townProvider.checkComExists(comId)==0){
                return new BaseResponse<>(POST_COM_NOT_EXISTS);
            }

            // 자신의 댓글인지 확인
            if(townProvider.checkComUser(comId)!=userId){
                return new BaseResponse<>(INVALID_USER_POST);
            }


            PatchTownPostComReq patchTownPostComReq = new PatchTownPostComReq(postId, comId, userId, townPostCom.getContent(), townPostCom.getStatus());
            townService.modifyTownCom(patchTownPostComReq);
            String result = "";
            return new BaseResponse<>(result);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 동네 생활 글 좋아요 설정 API
     * [POST] /towns/:postId/liked
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{postId}/liked")
    public BaseResponse<String> createTownPostLiked(HttpServletRequest request, @PathVariable int postId) {
        try {
            int userId = (int) request.getAttribute("userId");

            // 글 존재여부 확인
            if (townProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }

            if (townProvider.checkLiked(postId, userId) == 0) {
                // 좋아요 하지 않은 게시글일 때
                townService.createTownPostLiked(postId, userId);
                String result = "";
                return new BaseResponse<>(result);

            } else {
                if (townProvider.checkPostLikeStatus(postId, userId).equals("Y")){
                    return new BaseResponse<>(POST_LIKE_DUPLICATED);
                }
                // 좋아요 한 기록이 있는 게시글일 때
                PatchTownLikedReq patchTownLikedReq = new PatchTownLikedReq(postId, userId,"Y");
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
     * [PATCH] /towns/:postId/liked
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/liked")
    public BaseResponse<String> modifyTownPostLiked(HttpServletRequest request, @PathVariable int postId, @RequestBody TownLiked townLiked) {
        try {
            int userId = (int) request.getAttribute("userId");

            // 글 존재여부 확인
            if (townProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }
            // 좋아요 한 기록이 있는지 여부 확인
            if (townProvider.checkLiked(postId, userId) == 0){
                return new BaseResponse<>(POST_LIKE_NOT_EXISTS);
            }
            if (townProvider.checkPostLikeStatus(postId, userId).equals(townLiked.getStatus())){
                if (townLiked.getStatus().equals("Y")){
                    return new BaseResponse<>(POST_LIKE_DUPLICATED);
                }
                return new BaseResponse<>(POST_NOT_LIKE_DUPLICATED);
            }

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
     * [POST] /towns/:postId/:comId/liked
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{postId}/{comId}/liked")
    public BaseResponse<String> createTownComLiked(HttpServletRequest request, @PathVariable int postId, @PathVariable int comId) {
        try {
            int userId = (int) request.getAttribute("userId");

            // 글 존재여부 확인
            if (townProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }

            // 댓글 존재여부 확인
            if (townProvider.checkComExists(comId)==0){
                return new BaseResponse<>(POST_COM_NOT_EXISTS);
            }


            if (townProvider.checkComLiked(postId, comId, userId) == 0) {
                // 좋아요 하지 않은 댓글일 때
                townService.createTownComLiked(postId, comId, userId);
                String result = "";
                return new BaseResponse<>(result);

            } else {
                if (townProvider.checkPostComLikeStatus(comId, userId).equals("Y")){
                    return new BaseResponse<>(POST_LIKE_DUPLICATED);
                }
                // 좋아요 한 기록이 있는 댓글일 떄
                PatchTownComLikedReq patchTownComLikedReq = new PatchTownComLikedReq(postId, comId, userId, "Y");
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
     * [PATCH] /towns/:postId/:comId/liked
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postId}/{comId}/liked")
    public BaseResponse<String> modifyTownComLiked(HttpServletRequest request, @PathVariable int postId, @PathVariable int comId,
                                                   @Valid @RequestBody TownComLiked townComLiked) {
        try {
            int userId = (int) request.getAttribute("userId");

            // 글 존재여부 확인
            if (townProvider.checkPostExists(postId) == 0){
                return new BaseResponse<>(POST_NOT_EXISTS);
            }
            // 댓글 존재여부 확인
            if (townProvider.checkComExists(comId)==0){
                return new BaseResponse<>(POST_COM_NOT_EXISTS);
            }

            // 좋아요 한 기록이 있는지 여부 확인
            if (townProvider.checkComLiked(postId, comId, userId) == 0){
                return new BaseResponse<>(POST_COM_LIKE_NOT_EXISTS);
            }
            if (townProvider.checkPostComLikeStatus(comId, userId).equals(townComLiked.getStatus())){
                if (townComLiked.getStatus().equals("Y")){
                    return new BaseResponse<>(POST_LIKE_DUPLICATED);
                }
                return new BaseResponse<>(POST_NOT_LIKE_DUPLICATED);
            }
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
     * [GET] /towns/user-post
     * @return BaseResponse<List<GetTownRes>>
     */
    @ResponseBody
    @GetMapping("/user-post")
    public BaseResponse<List<GetTownListRes>> getUserTownPosts(HttpServletRequest request) {
        try {
            int userId = (int) request.getAttribute("userId");

            List<GetTownListRes> getTownsRes = townProvider.getUserTownPosts(userId);

            return new BaseResponse<>(getTownsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 작성한 동네 생활 댓글 조회 API
     * [GET] /towns/user-comment
     * @return BaseResponse<List<GetTownComRes>>
     */
    @ResponseBody
    @GetMapping("/user-comment")
    public BaseResponse<List<GetTownComRes>> getUserTownComs(HttpServletRequest request) {
        try {

            int userId = (int) request.getAttribute("userId");

            List<GetTownComRes> getTownComRes = townProvider.getUserTownComs(userId);

            return new BaseResponse<>(getTownComRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}