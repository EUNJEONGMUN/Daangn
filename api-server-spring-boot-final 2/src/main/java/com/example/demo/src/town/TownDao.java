package com.example.demo.src.town;

import com.example.demo.src.town.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TownDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetTownRes> getTowns(){
        String getTownsQuery = "select  category.categoryName, TP.townPostId, TP.content, User.userName, ComLike.likeCount, ComLike.comCount\n" +
                "from TownPost TP\n" +
                "    inner join User on TP.userId = User.userId\n" +
                "    inner join (select TP.townPostId, Category.categoryName\n" +
                "                from TownPost TP\n" +
                "                left join Category on Category.categoryId = TP.townPostCategoryId) as category on category.townPostId = TP.townPostId\n" +
                "    inner join (select TP.townPostId, count(TPcom.townPostId) as comCount, likecount.likeCount\n" +
                "                from TownPost TP\n" +
                "                left join TownPostCom TPcom on TP.townPostId = TPcom.townPostId\n" +
                "                inner join (select TP.townPostId, count(TPlike.postId) as likeCount\n" +
                "                from TownPost TP\n" +
                "                left join TownPostLike TPlike on TP.townPostId = TPlike.postId\n" +
                "                group by TP.townPostId) as likecount on likecount.townPostId = TP.townPostId\n" +
                "group by TP.townPostId) as ComLike on ComLike.townPostId = TP.townPostId";


        return this.jdbcTemplate.query(
                getTownsQuery,
                (rs, rowNum) -> new GetTownRes(
                        rs.getString("categoryName"),
                        rs.getInt("townPostId"),
                        rs.getString("content"),
                        rs.getString("userName"),
                        rs.getInt("comCount"),
                        rs.getInt("likeCount")
                        )
                );

    }


    public List<GetTownRes> getTown(int categoryId) {
        String getTownQuery ="select  category.categoryName, TP.townPostId, TP.content, User.userName, ComLike.likeCount, ComLike.comCount\n" +
                "from TownPost TP\n" +
                "    inner join User on TP.userId = User.userId\n" +
                "    inner join (select TP.townPostId, Category.categoryName\n" +
                "                from TownPost TP\n" +
                "                left join Category on Category.categoryId = TP.townPostCategoryId) as category on category.townPostId = TP.townPostId\n" +
                "    inner join (select TP.townPostId, count(TPcom.townPostId) as comCount, likecount.likeCount\n" +
                "                from TownPost TP\n" +
                "                left join TownPostCom TPcom on TP.townPostId = TPcom.townPostId\n" +
                "                inner join (select TP.townPostId, count(TPlike.postId) as likeCount\n" +
                "                from TownPost TP\n" +
                "                left join TownPostLike TPlike on TP.townPostId = TPlike.postId\n" +
                "                group by TP.townPostId) as likecount on likecount.townPostId = TP.townPostId\n" +
                "group by TP.townPostId) as ComLike on ComLike.townPostId = TP.townPostId\n" +
                "where townPostCategoryId = ?";

        int getTownParams = categoryId;
        return this.jdbcTemplate.query(
                getTownQuery,
                (rs, rowNum) -> new GetTownRes(
                        rs.getString("categoryName"),
                        rs.getInt("townPostId"),
                        rs.getString("content"),
                        rs.getString("userName"),
                        rs.getInt("comCount"),
                        rs.getInt("likeCount")
                ),
                getTownParams);
    }

    public int createTown(PostTownNewReq postTownNewReq) {
        String createTownQuery = "insert into TownPost (userId, townPostCategoryId, content, townPostLocation) VALUES (?,?,?,?)";
        Object[] createTownParams = new Object[]{postTownNewReq.getUserId(), postTownNewReq.getTownPostCategoryId(), postTownNewReq.getContent(), postTownNewReq.getTownPostLocation()};
        this.jdbcTemplate.update(createTownQuery, createTownParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


    public int createTownCom(int postId, PostTownComReq postTownComReq) {
        String createTownComQuery = "insert into TownPostCom (townPostId, userId, content, refId) VALUES (?,?,?,?)";
        Object[] createTownComParams = new Object[]{postId, postTownComReq.getUserId(), postTownComReq.getContent(), postTownComReq.getRefId()};
        this.jdbcTemplate.update(createTownComQuery, createTownComParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int createTownComCom(int postId, PostTownComReq postTownComReq) {
        String createTownComQuery = "insert into TownPostCom (townPostId, userId, content) VALUES (?,?,?)";
        Object[] createTownComParams = new Object[]{postId, postTownComReq.getUserId(), postTownComReq.getContent()};
        this.jdbcTemplate.update(createTownComQuery, createTownComParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkLiked(int postId, int userId) {
        String checkLikedQuery = "select exists(select postId, userId from TownPostLike where postId=? and userId=?)";
        Object[] checkLikedParams = new Object[]{postId, userId};
        int result = this.jdbcTemplate.queryForObject(checkLikedQuery,
                    int.class,
                    checkLikedParams);

        if (result == 0){
            return result; // result 0 -> 존재하지 않음
        } else{
            String checkLikedStatusQuery = "select status from TownPostLike where postId=? and userId=?";
            Object[] checkLikedStatusParams = new Object[]{postId, userId};
            return this.jdbcTemplate.queryForObject(checkLikedStatusQuery,
                    char.class,
                    checkLikedStatusParams);

        }
    }

    public int createTownPostLiked(int postId, PutTownLikedReq putTownLikedReq) {
        String createTownLikedQuery = "insert into TownPostLike (postId, userId) VALUES (?, ?)";
        Object[] createTownLikedParams = new Object[]{postId, putTownLikedReq.getUserId()};
        return this.jdbcTemplate.update(createTownLikedQuery, createTownLikedParams);

    }

    public int modifyTownPostLiked(int postId, PutTownLikedReq putTownLikedReq) {
        String modifyTownLikedQuery = "UPDATE TownPostLike SET status = IF(status='Y', 'N', 'Y') where postId=? and userId=?";
        Object[] modifyTownLikedParams = new Object[]{postId, putTownLikedReq.getUserId()};
        return this.jdbcTemplate.update(modifyTownLikedQuery, modifyTownLikedParams);
    }

    public int checkComLiked(int postId, int comId, int userId) {
        String checkLikedQuery = "select exists(select postId, comId, userId from TownPostComLike where postId=? and comId =? and userId=?)";
        Object[] checkLikedParams = new Object[]{postId, comId, userId};
        int result = this.jdbcTemplate.queryForObject(checkLikedQuery,
                int.class,
                checkLikedParams);

        if (result == 0){
            return result; // result 0 -> 존재하지 않음
        } else{
            String checkLikedStatusQuery = "select status from TownPostComLike where postId=? and comId =? and userId=?";
            Object[] checkLikedStatusParams = new Object[]{postId, comId, userId};
            return this.jdbcTemplate.queryForObject(checkLikedStatusQuery,
                    char.class,
                    checkLikedStatusParams);

        }
    }

    public int createTownComLiked(int postId, int comId, PutTownComLikedReq putTownComLikedReq) {
        String createTownLikedQuery = "insert into TownPostComLike (postId, comId, userId) VALUES (?, ?, ?)";
        Object[] createTownLikedParams = new Object[]{postId, comId, putTownComLikedReq.getUserId()};
        return this.jdbcTemplate.update(createTownLikedQuery, createTownLikedParams);
    }

    public int modifyTownComLiked(int postId, int comId, PutTownComLikedReq putTownComLikedReq) {
        String modifyTownLikedQuery = "UPDATE TownPostComLike SET status = IF(status='Y', 'N', 'Y') where postId=? and comId =? and userId=?";
        Object[] modifyTownLikedParams = new Object[]{postId, comId, putTownComLikedReq.getUserId()};
        return this.jdbcTemplate.update(modifyTownLikedQuery, modifyTownLikedParams);
    }
}
