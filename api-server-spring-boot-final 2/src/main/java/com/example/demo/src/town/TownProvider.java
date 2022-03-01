package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.src.town.model.GetTownRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public List<GetTownRes> getTowns() throws BaseException{

        try{
            List<GetTownRes> getTownRes = townDao.getTowns();
            return getTownRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetTownRes> getTown(int categoryId) throws BaseException {

        try {
            List<GetTownRes> getTownRes = townDao.getTown(categoryId);
            return getTownRes;
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkLiked(int postId, int userId) throws BaseException {
        try{
            return townDao.checkLiked(postId, userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkComLiked(int postId, int comId, int userId) throws BaseException {
        try{
            return townDao.checkComLiked(postId, comId, userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    /*
    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
     */

}
