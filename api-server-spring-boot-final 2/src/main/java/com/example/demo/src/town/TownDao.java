package com.example.demo.src.town;

import com.example.demo.src.town.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TownDao {

    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 동네 생활 전체 글 조회 API
     * [GET] /towns/home
     * @return BaseResponse<List<GetTownRes>>
     */
    public List<GetTownRes> getTowns(){
        String getTownsQuery = "select category.categoryName, TP.townPostId, TP.content, User.userName,\n" +
                "     case\n" +
                "           when TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, TP.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime\n" +
                "        , ComLike.likeCount, ComLike.comCount\n" +
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
                "order by TP.createdAt desc;";


        return this.jdbcTemplate.query(
                getTownsQuery,
                (rs, rowNum) -> new GetTownRes(
                        rs.getString("categoryName"),
                        rs.getInt("townPostId"),
                        rs.getString("content"),
                        rs.getString("userName"),
                        rs.getString("uploadTime"),
                        rs.getInt("likeCount"),
                        rs.getInt("comCount")
                        )
                );

    }

    /**
     * 동네 생활 카테고리별 조회 API
     * [GET] /towns/home/:categoryId
     * @return BaseResponse<List<GetTownRes>>
     */
    @ResponseBody
    public List<GetTownRes> getTown(int categoryId) {
        String getTownQuery = "select category.categoryName, TP.townPostId, TP.content, User.userName,\n" +
                "     case\n" +
                "           when TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, TP.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime\n" +
                "        , ComLike.likeCount, ComLike.comCount\n" +
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
                "where TP.townPostCategoryId = ?\n" +
                "order by TP.createdAt desc;";

        int getTownParams = categoryId;

        return this.jdbcTemplate.query(
                getTownQuery,
                (rs, rowNum) -> new GetTownRes(
                        rs.getString("categoryName"),
                        rs.getInt("townPostId"),
                        rs.getString("content"),
                        rs.getString("userName"),
                        rs.getString("uploadTime"),
                        rs.getInt("likeCount"),
                        rs.getInt("comCount")),
                getTownParams);
    }

    /**
     * 동네 생활 글 작성 API
     * [POST] /towns/new/:userId
     * @return BaseResponse<PostTownNewRes>
     */
    public int createTown(int userId, PostTownNewReq postTownNewReq) {
        String createTownQuery = "insert into TownPost (userId, townPostCategoryId, content, townPostLocation) VALUES (?,?,?,?)";
        Object[] createTownParams = new Object[]{userId, postTownNewReq.getTownPostCategoryId(), postTownNewReq.getContent(), postTownNewReq.getTownPostLocation()};
        this.jdbcTemplate.update(createTownQuery, createTownParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    /**
     * 동네 생활 글 수정 API
     * [PATCH] /towns/:postId/:userId
     * @return BaseResponse<String>
     */
    public int modifyTownPost(PatchTownPostReq patchTownPostReq) {
        String Query = "update TownPost TP\n" +
                "set TP.townPostCategoryId=?, TP.content=?, TP.townPostLocation=?\n" +
                "where TP.userId=? and TP.townPostId=?;";
        Object[] Params = new Object[]{patchTownPostReq.getTownPostCategoryId(), patchTownPostReq.getContent(), patchTownPostReq.getTownPostLocation(),
        patchTownPostReq.getUserId(), patchTownPostReq.getTownPostId()};
        return this.jdbcTemplate.update(Query, Params);
    }

    /**
     * 동네 생활 글 삭제 API
     * [PATCH] /towns/:postId/:userId/deletion
     * @return BaseResponse<String>
     */
    public int deleteTownPost(PatchTownPostDelReq patchTownPostDelReq) {
        String Query = "update TownPost TP\n" +
                "set TP.status =?\n" +
                "where TP.userId=? and TP.townPostId=?;\n";
        Object[] Params = new Object[]{patchTownPostDelReq.getStatus(), patchTownPostDelReq.getUserId(), patchTownPostDelReq.getPostId()};
        return this.jdbcTemplate.update(Query, Params);
    }

    /**
     * 동네 생활 댓글 작성 API
     * [POST] /towns/:postId/:userId/comment
     * @return BaseResponse<String>
     */
    public int createTownCom(int postId, int userId, PostTownComReq postTownComReq) {
        String createTownComQuery = "insert into TownPostCom (townPostId, userId, content, refId) VALUES (?,?,?,?)";
        Object[] createTownComParams = new Object[]{postId, userId, postTownComReq.getContent(), postTownComReq.getRefId()};
        this.jdbcTemplate.update(createTownComQuery, createTownComParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    /**
     * 동네 생활 댓글 작성 API - 대댓글
     * [POST] /towns/:postId/:userId/comment
     * @return BaseResponse<String>
     */
    public int createTownComCom(int postId, int userId, PostTownComReq postTownComReq) {
        String createTownComQuery = "insert into TownPostCom (townPostId, userId, content) VALUES (?,?,?)";
        Object[] createTownComParams = new Object[]{postId, userId, postTownComReq.getContent()};
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

    // 카테고리 범위 체크
    public int checkTopCategory(int categoryId) {
        String checkTopCategoryQuery = "select refId from Category where categoryId=?";
        int checkTopCategoryParam = categoryId;
        return this.jdbcTemplate.queryForObject(checkTopCategoryQuery,
                int.class,
                checkTopCategoryParam);
    }


}
