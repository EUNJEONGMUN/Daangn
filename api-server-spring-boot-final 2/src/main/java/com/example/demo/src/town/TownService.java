package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.src.town.model.*;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class TownService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TownDao townDao;
    private final TownProvider townProvider;
    @Autowired
    private TownService(TownDao townDao, TownProvider townProvider){
        this.townDao = townDao;
        this.townProvider = townProvider;
    }

    //POST
    public PostTownNewRes createTown(PostTownNewReq postTownNewReq) throws BaseException {

        try{
            int townPostId = townDao.createTown(postTownNewReq);
            return new PostTownNewRes(townPostId);
        } catch(Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }


    }
}
