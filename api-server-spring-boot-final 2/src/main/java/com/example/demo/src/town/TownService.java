package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.src.town.model.*;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
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

    //POST
    public PostTownNewRes createTown(PostTownNewReq postTownNewReq) throws BaseException {

        try {
            int townPostId = townDao.createTown(postTownNewReq);
            return new PostTownNewRes(townPostId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }


    }

    // Town Com POST
    public PostTownComRes createTownCom(int postId, PostTownComReq postTownComReq) throws BaseException {

        try {
            int townPostComId = townDao.createTownCom(postId, postTownComReq);
            return new PostTownComRes(townPostComId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostTownComRes createTownComCom(int postId, PostTownComReq postTownComReq) throws BaseException {

        try {
            int townPostComId = townDao.createTownComCom(postId, postTownComReq);
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
                // 좋아요 하지 않은 게시글일 때
                try {
                    int likeListId = townDao.createTownComLiked(postId, comId, putTownComLikedReq);
                } catch (Exception exception) {
                    throw new BaseException(DATABASE_ERROR);
                }


            } else {
                // 좋아요 한 게시글일 때
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
}
