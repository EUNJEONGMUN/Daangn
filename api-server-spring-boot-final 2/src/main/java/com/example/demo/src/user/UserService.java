package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.PutUserKeywordsReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;

    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider) {
        this.userDao = userDao;
        this.userProvider = userProvider;

    }

    public void setKeywords(int userId, PutUserKeywordsReq putUserKeywordsReq) throws BaseException {
        if (userProvider.checkKeyword(userId, putUserKeywordsReq.getKeyword()) == 0){
            // 설정된 키워드가 없을 경우
            int result = userDao.setKeyWords(userId, putUserKeywordsReq.getKeyword());
            if (result == 0) {
                throw new BaseException(SET_FAIL_KEYWORDS);
            }
        } else {
            int result = userDao.modifyKeywords(userId, putUserKeywordsReq.getKeyword());
            if (result == 0) {
                throw new BaseException(SET_FAIL_KEYWORDS);
            }
        }
    }
}
