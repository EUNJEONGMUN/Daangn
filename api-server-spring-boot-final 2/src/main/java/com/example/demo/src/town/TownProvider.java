package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.src.town.model.Res.GetTownComRes;
import com.example.demo.src.town.model.Res.GetTownRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class TownProvider {

    private final TownDao townDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public TownProvider(TownDao townDao){
        this.townDao = townDao;
    }

    /**
     * 동네 생활 전체 글 조회 API
     * [GET] /towns/home
     * @return BaseResponse<List<GetTownRes>>
     */
    public List<GetTownRes> getTowns() throws BaseException{

        try{
            List<GetTownRes> getTownRes = townDao.getTowns();
            return getTownRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 동네 생활 카테고리별 조회 API
     * [GET] /towns/home/:categoryId
     * @return BaseResponse<List<GetTownRes>>
     */
    @ResponseBody
    public List<GetTownRes> getTown(int categoryId) throws BaseException {
        try {
            List<GetTownRes> getTownRes = townDao.getTown(categoryId);
            return getTownRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 동네 생활 글 좋아요 체크
    public int checkLiked(int postId, int userId) throws BaseException {
        try{
            return townDao.checkLiked(postId, userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 동네 생활 댓글 좋아요 체크
    public int checkComLiked(int postId, int comId, int userId) throws BaseException {
        try{
            return townDao.checkComLiked(postId, comId, userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 작성한 동네 생활 글 조회 API
     * [GET] /towns/user-post/:userId
     * @return BaseResponse<List<GetTownRes>>
     */
    public List<GetTownRes> getUserTownPosts(int userId) throws BaseException {
        try{
            List<GetTownRes> getTownRes = townDao.getUserTownPosts(userId);
            return getTownRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    /**
     * 작성한 동네 생활 댓글 조회 API
     * [GET] /towns/user-comment/:userId
     * @return BaseResponse<List<GetTownComRes>>
     */
    public List<GetTownComRes> getUserTownComs(int userId) throws BaseException  {
        try{
            List<GetTownComRes> getTownComRes = townDao.getUserTownComs(userId);
            return getTownComRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 카테고리 범위 체크
    public int checkTopCategory(int townPostCategoryId) throws BaseException {
        try {
            return townDao.checkTopCategory(townPostCategoryId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
