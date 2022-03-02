package com.example.demo.src.around;

import com.example.demo.config.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.demo.config.BaseResponseStatus.*;
@Service
public class AroundProvider {

    private final AroundDao aroundDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AroundProvider(AroundDao aroundDao){
        this.aroundDao = aroundDao;
    }

    public int checkChat(int postId, int userId) throws BaseException {
        try{
            return aroundDao.checkChat(postId, userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    public int findPostUser(int postId) throws BaseException {
        try{
            return aroundDao.findPostUser(postId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int findChatListId(int postId, int userId) throws BaseException {
        try{
            return aroundDao.findChatListId(postId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
