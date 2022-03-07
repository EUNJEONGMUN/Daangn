package com.example.demo.src.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class MessageController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    // 휴대폰 인증
    @ResponseBody
    @GetMapping("/check/sendSMS")
    public String sendSMS(@RequestBody AuthMessageReq authMessageReq) {
        String phoneNumber = authMessageReq.getPhoneNumber();
        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numStr);
        messageService.certifiedPhoneNumber(phoneNumber,numStr);
        return numStr;
    }

}
