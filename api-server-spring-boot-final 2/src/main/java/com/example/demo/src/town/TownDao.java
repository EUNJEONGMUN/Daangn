package com.example.demo.src.town;

import com.example.demo.src.town.model.Req.*;
import com.example.demo.src.town.model.Res.GetTownComRes;
import com.example.demo.src.town.model.Res.GetTownListRes;
import com.example.demo.src.town.model.Res.GetTownPostRes;
import com.example.demo.src.town.model.TownCom;
import com.example.demo.src.town.model.TownComCom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class TownDao {

    private JdbcTemplate jdbcTemplate;
    private GetTownListRes getTownListRes;
    private List<TownCom> townCom;
    private List<TownComCom> townComCom;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 동네 생활 전체 글 조회 API
     * [GET] /towns/home
     * @return BaseResponse<List<GetTownRes>>
     */
    public List<GetTownListRes> getTowns(){

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
                "where TP.status = 'Y'\n" +
                "order by TP.createdAt DESC;";

        return this.jdbcTemplate.query(
                getTownsQuery,
                (rs, rowNum) -> new GetTownListRes(
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
    public List<GetTownListRes> getTown(int categoryId) {
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
                "where TP.townPostCategoryId = ? and TP.status='Y'\n" +
                "order by TP.createdAt DESC;";

        int getTownParams = categoryId;

        return this.jdbcTemplate.query(
                getTownQuery,
                (rs, rowNum) -> new GetTownListRes(
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
    public int createTown(int jusoCodeId, PostTownNewReq postTownNewReq) {
        String createTownQuery = "insert into TownPost (userId, townPostCategoryId, content, townPostLocation) VALUES (?,?,?,?)";
        Object[] createTownParams = new Object[]{postTownNewReq.getUserId(), postTownNewReq.getTownPostCategoryId(), postTownNewReq.getContent(), jusoCodeId};
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
        System.out.println("modify 진입");
        String Query = "update TownPost TP\n" +
                "set TP.townPostCategoryId=?, TP.content=?, TP.status=?\n" +
                "where TP.userId=? and TP.townPostId=?;";
        Object[] Params = new Object[]{patchTownPostReq.getTownPostCategoryId(), patchTownPostReq.getContent(), patchTownPostReq.getStatus(),
        patchTownPostReq.getUserId(), patchTownPostReq.getTownPostId()};
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
     * 동네 생활 댓글 수정 API
     * [PATCH] /towns/:postId/comment/:comId/:userId
     * @return BaseResponse<String>
     */
    public int modifyTownCom(PatchTownPostComReq patchTownPostComReq) {
        String Query = "update TownPostCom TC\n" +
                "set TC.content = ?, TC.status=?\n" +
                "where TC.townPostComId=?;";
        Object[] Params = new Object[]{patchTownPostComReq.getContent(), patchTownPostComReq.getStatus(), patchTownPostComReq.getComId()};
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
    public int createTownPostLiked(int postId, int userId) {
        String Query = "insert into TownPostLike (postId, userId) values (?,?);";
        Object[] Params = new Object[]{postId, userId};
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
       String checkLikedQuery = "select exists(select postId, comId, userId from TownPostComLike where postId=? and comId =? and userId=?);";
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
    public int createTownComLiked(int postId, int comId, int userId) {
        String Query = "insert into TownPostComLike (postId, comId, userId) VALUES (?, ?, ?);";
        Object[] Params = new Object[]{postId, comId, userId};
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
    public List<GetTownListRes> getUserTownPosts(int userId) {

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
                "where TP.userId = ? and TP.status='Y'\n" +
                "order by TP.createdAt DESC;";
        int Params = userId;
        return this.jdbcTemplate.query(
                Query,
                (rs, rowNum) -> new GetTownListRes(
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
                "where TC.userId = ? and TC.status='Y'\n" +
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

    public int checkPostUser(int postId) {
        String Query = "select userId from TownPost where townPostId=?";
        int Params = postId;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);
    }

    // 사용자 주소 코드 찾기
    public int findUserJusoCodeId(int userId) {
        String Query = "select jusoCodeId from UserArea where userId=?;";
        int Params = userId;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);
    }

    // 게시글 존재 확인
    public int checkPostExists(int postId) {
        String Query = "select exists(select * from TownPost where townPostId=? and status='Y');";
        int Params = postId;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);
    }

    // 댓글 존재 확인
    public int checkComExists(int refId) {
        String Query = "select exists(select * from TownPostCom where townPostComId=? and status='Y');";
        int Params = refId;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);
    }

    public int checkComUser(int comId) {
        String Query = "select userId from TownPostCom where townPostComId=?";
        int Params = comId;
        return this.jdbcTemplate.queryForObject(Query,
                int.class,
                Params);
    }

    public String checkPostLikeStatus(int postId, int userId) {
        String Query = "select status from TownPostLike where postId=? and userId=?";
        Object[] Params = new Object[]{postId, userId};
        return this.jdbcTemplate.queryForObject(Query,
                String.class,
                Params);
    }

    public String checkPostComLikeStatus(int comId, int userId) {
        String Query = "select status from TownPostComLike where comId=? and userId=?";
        Object[] Params = new Object[]{comId, userId};
        return this.jdbcTemplate.queryForObject(Query,
                String.class,
                Params);
    }

    /**
     * 동네 생활 글 개별 API
     * [GET] /towns/:postId
     * @return BaseResponse<GetTownPostRes>
     */
    public GetTownPostRes getTownPost(int postId) {
        String Query1 = "select C.categoryName, TP.townPostId, TP.content, User.userName,\n" +
                "                       case\n" +
                "                           when TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp())<60\n" +
                "                            then concat(TIMESTAMPDIFF(SECOND, TP.createdAt, current_timestamp()),'초 전')\n" +
                "                           when TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp())<60\n" +
                "                            then concat(TIMESTAMPDIFF(MINUTE, TP.createdAt, current_timestamp()),'분 전')\n" +
                "                            when TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp())<24\n" +
                "                            then concat(TIMESTAMPDIFF(HOUR, TP.createdAt, current_timestamp()), '시간 전')\n" +
                "                            else concat(TIMESTAMPDIFF(DAY, TP.createdAt, current_timestamp()), '일 전')\n" +
                "                        end as uploadTime, likeCom.likeCount, likeCom.comCount\n" +
                "                from TownPost TP\n" +
                "                    join User on TP.userId = User.userId\n" +
                "                    join (select Category.categoryName, TP.townPostId\n" +
                "                            from TownPost TP\n" +
                "                            left join Category on Category.categoryId = TP.townPostCategoryId) C on C.townPostId = TP.townPostId\n" +
                "                    left join (select TP.townPostId, count(TPcom.townPostId) as comCount, LC.likeCount\n" +
                "                                from TownPost TP\n" +
                "                                left join TownPostCom TPcom on TP.townPostId = TPcom.townPostId\n" +
                "                                left join (select TP.townPostId, count(TPlike.postId) as likeCount\n" +
                "                                            from TownPost TP\n" +
                "                                            left join TownPostLike TPlike on TP.townPostId = TPlike.postId\n" +
                "                                            where TPlike.status='Y'\n" +
                "                                            group by TP.townPostId) LC on LC.townPostId = TP.townPostId\n" +
                "                                where TPcom.status='Y'\n" +
                "                                group by TP.townPostId) likeCom on likeCom.townPostId = TP.townPostId\n" +
                "                where TP.status='Y' and TP.townPostId=?\n" +
                "                order by TP.createdAt DESC;";

        String Query2 = "select T.townPostComId, T.content, U.userName, U.userProfileImageUrl as userImg,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(SECOND, T.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, T.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, T.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, T.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, T.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, T.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, T.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime, juso.jusoName\n" +
                "from TownPostCom T join User U on T.userId = U.userId\n" +
                "join (\n" +
                "    select User.userId, JC.jusoName\n" +
                "    from UserArea UA\n" +
                "        inner join JusoCode as JC on UA.jusoCodeId = JC.jusoCodeId\n" +
                "        inner join User on UA.userId = User.userId\n" +
                "    ) juso on juso.userId = T.userId\n" +
                "where T.townPostId=? and T.refId=0 and T.status='Y'\n" +
                "order by T.createdAt DESC;";

        String Query3 = "select T.content, U.userName, U.userProfileImageUrl as userImg,\n" +
                "       case\n" +
                "           when TIMESTAMPDIFF(SECOND, T.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(SECOND, T.createdAt, current_timestamp()),'초 전')\n" +
                "           when TIMESTAMPDIFF(MINUTE, T.createdAt, current_timestamp())<60\n" +
                "            then concat(TIMESTAMPDIFF(MINUTE, T.createdAt, current_timestamp()),'분 전')\n" +
                "            when TIMESTAMPDIFF(HOUR, T.createdAt, current_timestamp())<24\n" +
                "            then concat(TIMESTAMPDIFF(HOUR, T.createdAt, current_timestamp()), '시간 전')\n" +
                "            else concat(TIMESTAMPDIFF(DAY, T.createdAt, current_timestamp()), '일 전')\n" +
                "        end as uploadTime, juso.jusoName\n" +
                "from TownPostCom T join User U on T.userId = U.userId\n" +
                "join (\n" +
                "    select User.userId, JC.jusoName\n" +
                "    from UserArea UA\n" +
                "        inner join JusoCode as JC on UA.jusoCodeId = JC.jusoCodeId\n" +
                "        inner join User on UA.userId = User.userId\n" +
                "    ) juso on juso.userId = T.userId\n" +
                "where T.refId=? and T.status='Y'\n" +
                "order by T.createdAt DESC;";

        int Params = postId;

        return this.jdbcTemplate.queryForObject(Query1,
                        (rs1, rowNum1) -> new GetTownPostRes(
                                rs1.getString("categoryName"),
                                rs1.getInt("townPostId"),
                                rs1.getString("content"),
                                rs1.getString("userName"),
                                rs1.getString("uploadTime"),
                                rs1.getInt("likeCount"),
                                rs1.getInt("comCount"),
                                this.jdbcTemplate.query(Query2,
                                        (rs2, nowNum2) -> new TownCom(
                                                rs2.getString("content"),
                                                rs2.getString("userName"),
                                                rs2.getString("userImg"),
                                                rs2.getString("uploadTime"),
                                                rs2.getString("jusoName"),
                                                this.jdbcTemplate.query(Query3,
                                                        (rs3, nowNum3) -> new TownComCom(
                                                                rs3.getString("content"),
                                                                rs3.getString("userName"),
                                                                rs3.getString("userImg"),
                                                                rs3.getString("uploadTime"),
                                                                rs3.getString("jusoName"))
                                                , rs2.getInt("townPostComId")
                                                ))
                                        , Params)
                        ), Params);

    }
}
