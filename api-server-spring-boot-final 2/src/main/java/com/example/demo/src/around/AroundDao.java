package com.example.demo.src.around;

import com.example.demo.src.around.model.PostAroundChatReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AroundDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkChat(int postId, int userId) {
         String checkChatQuery = "select exists (select * " +
                 "from AroundPostChatList" +
                 "where postId = ? and  userId =?)";

         Object[] checkChatParams = new Object[]{postId, userId};
         return this.jdbcTemplate.queryForObject(checkChatQuery,
                 int.class,
                 checkChatParams);
    }

    public int createChatList(int postId, PostAroundChatReq postAroundChatReq) {
        String createChatQuery = "insert into AroundPostChatList (postId, userId) values (?, ?)";
        Object[] createChatParams = new Object[]{postId, postAroundChatReq.getUserId()};
        this.jdbcTemplate.queryForObject(createChatQuery,
                int.class, createChatParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }


    public int findPostUser(int postId) {
        String findPostUserQuery = "select userId from AroundPost where aroundPostId = ?";
        int findPostUserParam = postId;

        return this.jdbcTemplate.queryForObject(findPostUserQuery,
                int.class,
                findPostUserParam);
    }

    public int postChat(int chatListIdx, PostAroundChatReq postAroundChatReq) {
        String postChatQuery = "insert into AroundPostChat (chatRoomId, userId, content) value (?, ?, ?)";
        Object[] postChatParams = new Object[]{chatListIdx, postAroundChatReq.getUserId(), postAroundChatReq.getContent()};
        this.jdbcTemplate.queryForObject(postChatQuery,
                int.class,
                postChatParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int postChatEmotion(int chatListIdx, PostAroundChatReq postAroundChatReq) {
        String postChatQuery = "insert into AroundPostChat (chatRoomId, userId, emotion) value (?, ?, ?)";
        Object[] postChatParams = new Object[]{chatListIdx, postAroundChatReq.getUserId(), postAroundChatReq.getEmotion()};
        this.jdbcTemplate.queryForObject(postChatQuery,
                int.class,
                postChatParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int findChatListId(int postId, int userId) {
        String findChatListIdQuery = "select chatListId from AroundPostChatList where postId=? and userId=?";
        Object[] findChatListIdParams = new Object[]{postId, userId};
        return this.jdbcTemplate.queryForObject(findChatListIdQuery,
                int.class,
                findChatListIdParams);
    }
}
