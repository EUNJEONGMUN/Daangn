package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.src.town.model.*;
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
            if (result == 0){
                throw new BaseException(MODIFY_FAIL_TOWN_POST);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 동네 생활 댓글 작성 API
     * [POST] /towns/:postId/:userId/comment
     * @return BaseResponse<String>
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
     * @return BaseResponse<String>
     */
    public PostTownComRes createTownComCom(int postId, int userId, PostTownComReq postTownComReq) throws BaseException {

        try {
            int townPostComId = townDao.createTownComCom(postId, userId, postTownComReq);
            return new PostTownComRes(townPostComId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyTownPostLiked(int postId, PutTownLikedReq putTownLikedReq) throws BaseException {
        try {
            if (townProvider.checkLiked(postId, putTownLikedReq.getUserId()) == 0) {
                // 좋아요 하지 않은 게시글일 때
                try {
                    int likeListId = townDao.createTownPostLiked(postId, putTownLikedReq);
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }

            } else {
                // 좋아요 한 게시글일 때
                try {
                    int result = townDao.modifyTownPostLiked(postId, putTownLikedReq);
                    if (result == 0) {
                        throw new BaseException(MODIFY_FAIL_TOWNPOSTLIKED);
                    }
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }


            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyTownComLiked(int postId, int comId, PutTownComLikedReq putTownComLikedReq) throws BaseException {

        try {
            if (townProvider.checkComLiked(postId, comId, putTownComLikedReq.getUserId()) == 0) {
                // 좋아요 하지 않은 댓글일 때
                try {
                    int likeListId = townDao.createTownComLiked(postId, comId, putTownComLikedReq);
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }


            } else {
                // 좋아요 한 댓글일 때
                try {
                    int result = townDao.modifyTownComLiked(postId, comId, putTownComLikedReq);
                    if (result == 0) {
                        throw new BaseException(MODIFY_FAIL_TOWNCOMLIKED);
                    }
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }

            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }


    }

    /**
     * 동네 생활 글 삭제 API
     * [PATCH] /towns/:postId/:userId/deletion
     * @return BaseResponse<String>
     */
    public void deleteTownPost(PatchTownPostDelReq patchTownPostDelReq) throws BaseException {
        try {
            int result = townDao.deleteTownPost(patchTownPostDelReq);
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_TOWN_POST);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 동네 생활 댓글 수정 API
     * [PATCH] /towns/:postId/comment/:comId/:userId
     * @return BaseResponse<String>
     */
    public void modifyTownCom(PatchTownPostComReq patchTownPostComReq)  throws BaseException {

    }
}
