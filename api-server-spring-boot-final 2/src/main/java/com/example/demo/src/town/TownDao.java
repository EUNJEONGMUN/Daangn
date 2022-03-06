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

        String getTownsQuery = "select C.categoryName, TP.townPostId, TP.content, User.userName,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, TP.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime, likeCom.likeCount, likeCom.comCount\n" +
                "from TownPost TP\n" +
                "    join User on TP.userId = User.userId\n" +
                "    join (select Category.categoryName, TP.townPostId\n" +
                "            from TownPost TP\n" +
                "            left join Category on Category.categoryId = TP.townPostCategoryId) C on C.townPostId = TP.townPostId\n" +
                "    left join (select TP.townPostId, count(TPcom.townPostId) as comCount, LC.likeCount\n" +
                "                from TownPost TP\n" +
                "                left join TownPostCom TPcom on TP.townPostId = TPcom.townPostId\n" +
                "                left join (select TP.townPostId, count(TPlike.postId) as likeCount\n" +
                "                            from TownPost TP\n" +
                "                            left join TownPostLike TPlike on TP.townPostId = TPlike.postId\n" +
                "                            where TPlike.status='Y'\n" +
                "                            group by TP.townPostId) LC on LC.townPostId = TP.townPostId\n" +
                "                where TPcom.status='Y'\n" +
                "                group by TP.townPostId) likeCom on likeCom.townPostId = TP.townPostId\n" +
                "order by TP.createdAt DESC;";

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
        String getTownQuery = "select C.categoryName, TP.townPostId, TP.content, User.userName,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, TP.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime, likeCom.likeCount, likeCom.comCount\n" +
                "from TownPost TP\n" +
                "    join User on TP.userId = User.userId\n" +
                "    join (select Category.categoryName, TP.townPostId\n" +
                "            from TownPost TP\n" +
                "            left join Category on Category.categoryId = TP.townPostCategoryId) C on C.townPostId = TP.townPostId\n" +
                "    left join (select TP.townPostId, count(TPcom.townPostId) as comCount, LC.likeCount\n" +
                "                from TownPost TP\n" +
                "                left join TownPostCom TPcom on TP.townPostId = TPcom.townPostId\n" +
                "                left join (select TP.townPostId, count(TPlike.postId) as likeCount\n" +
                "                            from TownPost TP\n" +
                "                            left join TownPostLike TPlike on TP.townPostId = TPlike.postId\n" +
                "                            where TPlike.status='Y'\n" +
                "                            group by TP.townPostId) LC on LC.townPostId = TP.townPostId\n" +
                "                where TPcom.status='Y'\n" +
                "                group by TP.townPostId) likeCom on likeCom.townPostId = TP.townPostId\n" +
                "where TP.townPostCategoryId = ?\n" +
                "order by TP.createdAt DESC;";

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


    // 카테고리 범위 체크
    public int checkTopCategory(int categoryId) {
        String checkTopCategoryQuery = "select refId from Category where categoryId=?";
        int checkTopCategoryParam = categoryId;
        return this.jdbcTemplate.queryForObject(checkTopCategoryQuery,
                int.class,
                checkTopCategoryParam);
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
     * @return BaseResponse<PostTownComRes>
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
     * @return BaseResponse<PostTownComRes>
     */
    public int createTownComCom(int postId, int userId, PostTownComReq postTownComReq) {
        String createTownComQuery = "insert into TownPostCom (townPostId, userId, content) VALUES (?,?,?)";
        Object[] createTownComParams = new Object[]{postId, userId, postTownComReq.getContent()};
        this.jdbcTemplate.update(createTownComQuery, createTownComParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    /**
     * 동네 생활 댓글 수정 API
     * [PATCH] /towns/:postId/comment/:comId/:userId
     * @return BaseResponse<String>
     */
    public int modifyTownCom(PatchTownPostComReq patchTownPostComReq) {
        String Query = "update TownPostCom TC\n" +
                "set TC.content = ?\n" +
                "where TC.userId=? and TC.townPostComId=?;";
        Object[] Params = new Object[]{patchTownPostComReq.getContent(), patchTownPostComReq.getUserId(), patchTownPostComReq.getComId()};
        return this.jdbcTemplate.update(Query, Params);

    }

    /**
     * 동네 생활 댓글 삭제 API
     * [PATCH] /towns/:postId/comment/:comId/:userId/deletion
     * @return BaseResponse<String>
     */
    public int deleteTownCom(PatchTownComDelReq patchTownComDelReq) {
        String Query = "update TownPostCom TC\n" +
                "set TC.status = ?\n" +
                "where TC.userId=? and TC.townPostComId=?;";
        Object[] Params = new Object[]{patchTownComDelReq.getStatus(), patchTownComDelReq.getUserId(), patchTownComDelReq.getComId()};
        return this.jdbcTemplate.update(Query, Params);

    }

    // 동네 생활 글 좋아요 체크
    public int checkLiked(int postId, int userId) {
        String Query = "select exists(select postId, userId from TownPostLike where postId=? and userId=?)";
        Object[] Params = new Object[]{postId, userId};
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);
    }

    /**
     * 동네 생활 글 좋아요 설정 API
     * [POST] /towns/:postId/liked/:userId
     * @return BaseResponse<String>
     */
    public int createTownPostLiked(int postId, int userId, PostTownLikedReq postTownLikedReq) {
        String Query = "insert into TownPostLike (postId, userId, status) values (?,?,?);";
        Object[] Params = new Object[]{postId, userId, postTownLikedReq.getStatus()};
        return this.jdbcTemplate.update(Query, Params);
    }

    /**
     * 동네 생활 글 좋아요 변경 API
     * [PATCH] /towns/:postId/liked/:userId
     * @return BaseResponse<String>
     */
    public int modifyTownPostLiked(PatchTownLikedReq patchTownLikedReq) {
        String Query = "update TownPostLike set status=? where userId=? and postId=?;";
        Object[] Params = new Object[]{patchTownLikedReq.getStatus(), patchTownLikedReq.getUserId(), patchTownLikedReq.getPostId()};
        return this.jdbcTemplate.update(Query, Params);
    }


    // 동네 생활 댓글 좋아요 체크
    public int checkComLiked(int postId, int comId, int userId) {
       String checkLikedQuery = "select exists(select postId, comId, userId from TownPostComLike where postId=? and comId =? and userId=?)";
       Object[] checkLikedParams = new Object[]{postId, comId, userId};
       return this.jdbcTemplate.queryForObject(checkLikedQuery,
               int.class,
               checkLikedParams);
    }

    /**
     * 동네 생활 댓글 좋아요 설정 API
     * [POST] /towns/:postId/:comId/liked/:userId
     * @return BaseResponse<String>
     */
    public int createTownComLiked(int postId, int comId, int userId, PostTownComLikedReq postTownComLikedReq) {
        String Query = "insert into TownPostComLike (postId, comId, userId, status) VALUES (?, ?, ?, ?)";
        Object[] Params = new Object[]{postId, comId, userId, postTownComLikedReq.getStatus()};
        return this.jdbcTemplate.update(Query, Params);
    }

    /**
     * 동네 생활 댓글 좋아요 변경 API
     * [PATCH] /towns/:postId/:comId/liked/:userId
     * @return BaseResponse<String>
     */
    public int modifyTownComLiked(PatchTownComLikedReq patchTownComLikedReq) {
        String Query = "update TownPostComLike set status=? where postId=? and comId=? and userId=?;";
        Object[] Params = new Object[]{patchTownComLikedReq.getStatus(), patchTownComLikedReq.getPostId(),
                patchTownComLikedReq.getComId(), patchTownComLikedReq.getUserId()};
        return this.jdbcTemplate.update(Query, Params);
    }

    /**
     * 작성한 동네 생활 글 조회 API
     * [GET] /towns/user-post/:userId
     * @return BaseResponse<List<GetTownRes>>
     */
    public List<GetTownRes> getUserTownPosts(int userId) {

        String Query = "select C.categoryName, TP.townPostId, TP.content, User.userName,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, TP.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime, likeCom.likeCount, likeCom.comCount\n" +
                "from TownPost TP\n" +
                "    join User on TP.userId = User.userId\n" +
                "    join (select Category.categoryName, TP.townPostId\n" +
                "            from TownPost TP\n" +
                "            left join Category on Category.categoryId = TP.townPostCategoryId) C on C.townPostId = TP.townPostId\n" +
                "    left join (select TP.townPostId, count(TPcom.townPostId) as comCount, LC.likeCount\n" +
                "                from TownPost TP\n" +
                "                left join TownPostCom TPcom on TP.townPostId = TPcom.townPostId\n" +
                "                left join (select TP.townPostId, count(TPlike.postId) as likeCount\n" +
                "                            from TownPost TP\n" +
                "                            left join TownPostLike TPlike on TP.townPostId = TPlike.postId\n" +
                "                            where TPlike.status='Y'\n" +
                "                            group by TP.townPostId) LC on LC.townPostId = TP.townPostId\n" +
                "                where TPcom.status='Y'\n" +
                "                group by TP.townPostId) likeCom on likeCom.townPostId = TP.townPostId\n" +
                "where TP.userId = ?\n" +
                "order by TP.createdAt DESC;";
        int Params = userId;
        return this.jdbcTemplate.query(
                Query,
                (rs, rowNum) -> new GetTownRes(
                        rs.getString("categoryName"),
                        rs.getInt("townPostId"),
                        rs.getString("content"),
                        rs.getString("userName"),
                        rs.getString("uploadTime"),
                        rs.getInt("likeCount"),
                        rs.getInt("comCount")),
                Params);
    }

    /**
     * 작성한 동네 생활 댓글 조회 API
     * [GET] /towns/user-comment/:userId
     * @return BaseResponse<List<GetTownComRes>>
     */
    public List<GetTownComRes> getUserTownComs(int userId) {
        String Query = "select TC.townPostComId,TC.content as comment, TP.content as postContent,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(SECOND, TC.createdAt, current_timestamp())<60\n" +
                "            then '1분 이하 전 작성'\n" +
                "           when TIMESTAMPDIFF(MINUTE, TC.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, TC.createdAt, current_timestamp()),'분 전 작성')\n" +
                "            when TIMESTAMPDIFF(HOUR, TC.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, TC.createdAt, current_timestamp()), '시간 전 작성')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, TC.createdAt, current_timestamp()), '일 전 작성')\n" +
                "        end as uploadTime\n" +
                "from TownPostCom TC join TownPost TP on TC.townPostId = TP.townPostId\n" +
                "where TC.userId = ?\n" +
                "order by TC.createdAt DESC;";

        int Params = userId;

        return this.jdbcTemplate.query(
                Query,
                (rs, rowNum) -> new GetTownComRes(
                        rs.getInt("townPostComId"),
                        rs.getString("comment"),
                        rs.getString("postContent"),
                        rs.getString("uploadTime")),
                Params);
    }

}
