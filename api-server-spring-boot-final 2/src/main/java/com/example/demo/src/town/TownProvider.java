package com.example.demo.src.town;

import com.example.demo.config.BaseException;
import com.example.demo.src.town.model.GetTownRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
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
}
