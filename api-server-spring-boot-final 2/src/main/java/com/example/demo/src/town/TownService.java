package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.src.town.model.Req.*;
import com.example.demo.src.town.model.Res.PostTownComRes;
import com.example.demo.src.town.model.Res.PostTownNewRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class TownService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TownDao townDao;
    private final TownProvider townProvider;
    private final int FAIL = 0;

    @Autowired
    private TownService(TownDao townDao, TownProvider townProvider) {
        this.townDao = townDao;
        this.townProvider = townProvider;
    }

    /**
     * 동네 생활 글 작성 API
     * [POST] /towns/new/:userId
     * @return BaseResponse<PostTownNewRes>
     */
    public PostTownNewRes createTown(int userId, PostTownNewReq postTownNewReq) throws BaseException {

        try {
            if (townDao.checkTopCategory(postTownNewReq.getTownPostCategoryId()) != 2){
                // 동네생활 카테고리 refId = 2
                throw new BaseException(CATEGORY_RANGE_ERROR);
            }
            int townPostId = townDao.createTown(userId, postTownNewReq);
            return new PostTownNewRes(townPostId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 동네 생활 글 수정 API
     * [PATCH] /towns/:postId/:userId
     * @return BaseResponse<String>
     */
    public void modifyTownPost(PatchTownPostReq patchTownPostReq) throws BaseException {
        try{
            int result = townDao.modifyTownPost(patchTownPostReq);
            if (result == FAIL){
                throw new BaseException(MODIFY_FAIL_TOWN_POST);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


//    /**
//     * 동네 생활 글 삭제 API
//     * [PATCH] /towns/:postId/:userId/deletion
//     * @return BaseResponse<String>
//     */
//    public void deleteTownPost(PatchTownPostDelReq patchTownPostDelReq) throws BaseException {
//        try {
//            int result = townDao.deleteTownPost(patchTownPostDelReq);
//            if (result == FAIL) {
//                throw new BaseException(DELETE_FAIL_TOWN_POST);
//            }
//        } catch(Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

    /**
     * 동네 생활 댓글 작성 API
     * [POST] /towns/:postId/:userId/comment
     * @return BaseResponse<PostTownComRes>
     */
    public PostTownComRes createTownCom(int postId, int userId, PostTownComReq postTownComReq) throws BaseException {

        try {
            int townPostComId = townDao.createTownCom(postId, userId, postTownComReq);
            return new PostTownComRes(townPostComId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 동네 생활 댓글 작성 API - 대댓글
     * [POST] /towns/:postId/:userId/comment
     * @return BaseResponse<PostTownComRes>
     */
    public PostTownComRes createTownComCom(int postId, int userId, PostTownComReq postTownComReq) throws BaseException {

        try {
            int townPostComId = townDao.createTownComCom(postId, userId, postTownComReq);
            return new PostTownComRes(townPostComId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 동네 생활 댓글 수정 API
     * [PATCH] /towns/:postId/comment/:comId/:userId
     * @return BaseResponse<String>
     */
    public void modifyTownCom(PatchTownPostComReq patchTownPostComReq) throws BaseException {
        try{
            int result = townDao.modifyTownCom(patchTownPostComReq);
            if (result == FAIL){
                throw new BaseException(MODIFY_FAIL_TOWN_COM);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

//
//    /**
//     * 동네 생활 댓글 삭제 API
//     * [PATCH] /towns/:postId/comment/:comId/:userId/deletion
//     * @return BaseResponse<String>
//     */
//    public void deleteTownCom(PatchTownComDelReq patchTownComDelReq) throws BaseException {
//        try {
//            int result = townDao.deleteTownCom(patchTownComDelReq);
//            if (result == FAIL){
//                throw new BaseException(DELETE_FAIL_TOWN_COM);
//            }
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }


    /**
     * 동네 생활 글 좋아요 설정 API
     * [POST] /towns/:postId/liked/:userId
     * @return BaseResponse<String>
     */
    public void createTownPostLiked(int postId, int userId, PostTownLikedReq postTownLikedReq) throws BaseException {
        try {
            int result = townDao.createTownPostLiked(postId, userId, postTownLikedReq);
            if (result == FAIL){
                throw new BaseException(CREATE_FAIL_TOWN_POST_LIKED);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 동네 생활 글 좋아요 변경 API
     * [PATCH] /towns/:postId/liked/:userId
     * @return BaseResponse<String>
     */
    public void modifyTownPostLiked(PatchTownLikedReq patchTownLikedReq) throws BaseException {
        try {
            int result = townDao.modifyTownPostLiked(patchTownLikedReq);
            if (result == FAIL) {
                throw new BaseException(MODIFY_FAIL_TOWN_POST_LIKED);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 동네 생활 댓글 좋아요 설정 API
     * [POST] /towns/:postId/:comId/liked/:userId
     *
     * @return BaseResponse<String>
     */
    public void createTownComLiked(int postId, int comId, int userId,
                                   PostTownComLikedReq postTownComLikedReq) throws BaseException {
        try {
            int result = townDao.createTownComLiked(postId, comId, userId, postTownComLikedReq);
            if (result == FAIL){
                throw new BaseException(CREATE_FAIL_TOWN_POST_COM_LIKED);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 동네 생활 댓글 좋아요 변경 API
     * [PATCH] /towns/:postId/:comId/liked/:userId
     *
     * @return BaseResponse<String>
     */
    public void modifyTownComLiked(PatchTownComLikedReq patchTownComLikedReq) throws BaseException {
        try{
            int result = townDao.modifyTownComLiked(patchTownComLikedReq);
            if (result == FAIL) {
                throw new BaseException(MODIFY_FAIL_TOWN_POST_COM_LIKED);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
