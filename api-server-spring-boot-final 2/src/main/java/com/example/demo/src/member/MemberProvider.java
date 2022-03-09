package com.example.demo.src.member;


import com.example.demo.config.BaseException;
import com.example.demo.src.member.model.GetMemberRes;
import com.example.demo.src.member.model.PostLoginReq;
import com.example.demo.src.member.model.PostLoginRes;
import com.example.demo.src.member.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class MemberProvider {

    private final MemberDao memberDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MemberProvider(MemberDao memberDao, JwtService jwtService) {
        this.memberDao = memberDao;
        this.jwtService = jwtService;
    }

    public List<GetMemberRes> getUsers() throws BaseException{
        try{
            List<GetMemberRes> getMemberRes = memberDao.getUsers();
            return getMemberRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetMemberRes> getUsersByEmail(String email) throws BaseException{
        try{
            List<GetMemberRes> getUsersRes = memberDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
                    }


    public GetMemberRes getUser(int userIdx) throws BaseException {
        try {
            GetMemberRes getMemberRes = memberDao.getUser(userIdx);
            return getMemberRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return memberDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = memberDao.getPwd(postLoginReq); // DB로부터 암호화되어있는 PW를 가져옴
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){ // 바디값으로 갖고 온 PW와 DB에서 복호화환 PW가 같다면
            int userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx); // jwt를 만드는 함수에 userIdx를 넣음
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

}
