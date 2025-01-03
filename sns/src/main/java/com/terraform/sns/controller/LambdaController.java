package com.terraform.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/")
public class LambdaController {
    @GetMapping("test")
    public ResponseEntity<String> testMethod(){
        try {
//            create sns client
            SnsClient client = SnsClient.builder().build();
//            create message
            Map<String,String> message = new HashMap<String, String>();
            message.put("name","civilzen");
            message.put("email","muthu165murugesan@gmail.com");
            message.put("website","www.google.com");
            ObjectMapper mapper = new ObjectMapper();
            String msg = mapper.writeValueAsString(message);
//            get topic arn and make publish request
            String topicArn = System.getenv("TOPIC_ARN");
            PublishRequest publishRequest = PublishRequest.builder()
                    .topicArn(topicArn)
                    .subject("civilzen-info")
                    .message(msg)
                    .build();
//            publish message
            PublishResponse publishResponse = client.publish(publishRequest);

        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("SNS failed!!!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("success!!!", HttpStatus.OK);
    }

}
