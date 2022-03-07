package com.example.demo.src.around;

import com.example.demo.config.BaseException;
import com.example.demo.src.around.model.PostAroundChatReq;
import com.example.demo.src.around.model.PostAroundChatRes;
import com.example.demo.src.around.model.PostAroundNewReq;
import com.example.demo.src.around.model.PostAroundNewRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AroundService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AroundDao aroundDao;
    private final AroundProvider aroundProvider;

    @Autowired
    private AroundService(AroundDao aroundDao, AroundProvider aroundProvider) {
        this.aroundDao = aroundDao;
        this.aroundProvider = aroundProvider;
    }

    // Post
    public PostAroundChatRes postChat(int postId, PostAroundChatReq postAroundChatReq) throws BaseException  {
        try{
            if (aroundProvider.findPostUser(postId) == postAroundChatReq.getUserId()
                    && aroundProvider.checkChat(postId, postAroundChatReq.getUserId()) == 0){
                    // 작성자는 채팅을 먼저 시작할 수 없다.
                    throw new BaseException(POST_FAIL_AROUND_CHAT_SELF);
                }
            int chatListId;
            if (aroundProvider.checkChat(postId, postAroundChatReq.getUserId()) == 0) {
                // 채팅 한 기록이 없다면
                // 채팅 방을 만든다.
                chatListId = aroundDao.createChatList(postId, postAroundChatReq);
                if (chatListId == 0) {
                    throw new BaseException(POST_FAIL_AROUND_CHATLIST);
                }
            } else {
                // 채팅 한 기록이 있다면
                chatListId = aroundProvider.findChatListId(postId, postAroundChatReq.getUserId());
                if (chatListId == 0) {
                    throw new BaseException(POST_FAIL_AROUND_CHATLIST);
                }
            }

            // 채팅을 보낸다.
            if (postAroundChatReq.getEmotion() == null){
                // 글의 형태라면
                int chatId = aroundDao.postChat(chatListId, postAroundChatReq);
                return new PostAroundChatRes(chatId);
            } else{
                // 이모티콘의 형태라면
                int chatId = aroundDao.postChatEmotion(chatListId, postAroundChatReq);
                return new PostAroundChatRes(chatId);
                }

            } catch (Exception exception){
                throw new BaseException(POST_FAIL_AROUND_CHAT);
                }

    }

    public PostAroundNewRes createAround(PostAroundNewReq postAroundNewReq) throws BaseException {
        try {
            int aroundPostId = aroundDao.createAround(postAroundNewReq);
            return new PostAroundNewRes(aroundPostId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
