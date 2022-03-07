package com.example.demo.src.member;



import com.example.demo.config.BaseException;
import com.example.demo.src.member.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service // 서비스로 선언
public class MemberService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MemberDao memberDao;
    private final MemberProvider memberProvider;
    private final JwtService jwtService;
    private final int FAIL = 0;

    @Autowired
    public MemberService(MemberDao memberDao, MemberProvider memberProvider, JwtService jwtService) {
        this.memberDao = memberDao;
        this.memberProvider = memberProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostMemberRes createUser(PostMemberReq postMemberReq) throws BaseException {
        //중복
        if(memberProvider.checkEmail(postMemberReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postMemberReq.getPassword());
            postMemberReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = memberDao.createUser(postMemberReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new PostMemberRes(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchMemberReq patchMemberReq) throws BaseException {
        try{
            int result = memberDao.modifyUserName(patchMemberReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
