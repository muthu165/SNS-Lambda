package com.terraform.sns.handler;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.serverless.proxy.spring.SpringBootProxyHandlerBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.terraform.sns.SnsApplication;

import java.time.Instant;

@SuppressWarnings({"unchecked","rawtypes","deprecation"})
public class StreamLambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static SpringBootLambdaContainerHandler<AwsProxyRequest,AwsProxyResponse> handler;
    static {
        try{
            handler = (SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse>)((SpringBootProxyHandlerBuilder) new SpringBootProxyHandlerBuilder()
                    .defaultProxy()
                    .asyncInit(Instant.now().toEpochMilli()))
                    .springBootApplication(SnsApplication.class)
                    .asyncInit()
                    .buildAndInitialize();
        } catch(ContainerInitializationException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize(load) the container......");
        }
    }
    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
        return handler.proxy(input, context);
    }
}
