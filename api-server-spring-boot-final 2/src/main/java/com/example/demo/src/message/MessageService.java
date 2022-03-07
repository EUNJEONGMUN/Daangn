package com.example.demo.src.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import org.json.simple.JSONObject;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import static com.example.demo.config.secret.MessageKey.*;

@Service
public class MessageService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    // 휴대폰 인증
    public void certifiedPhoneNumber(String phoneNumber, String numStr) {
        String api_key = API_KEY;
        String api_secret = API_SECRET;
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", PHONE_NUMBER);    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "회원가입 인증 메시지 : 인증번호는" + "["+numStr+"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
