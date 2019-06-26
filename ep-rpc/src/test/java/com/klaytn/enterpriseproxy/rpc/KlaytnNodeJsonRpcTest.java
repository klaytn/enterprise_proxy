/*
 * Copyright 2019 Enterprise Proxy Authors
 *
 * Licensed under the Apache License, Version 2.0 (the “License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.klaytn.enterpriseproxy.rpc;

import com.klaytn.enterpriseproxy.rpc.common.handler.RpcHandler;
import com.klaytn.enterpriseproxy.rpc.common.model.RpcRequest;
import com.klaytn.enterpriseproxy.rpc.common.model.RpcResponse;
import com.klaytn.enterpriseproxy.rpc.common.transfer.RpcTransfer;
import com.klaytn.enterpriseproxy.utils.JsonUtil;
import feign.Request;
import feign.Retryer;
import feign.okhttp.OkHttpClient;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@SpringBootTest
@SpringBootConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class KlaytnNodeJsonRpcTest {
    private static final Logger logger = LoggerFactory.getLogger(KlaytnNodeJsonRpcTest.class);


    @Test
    public void JSONRPCTEST() throws Exception {
        RpcHandler handler = Feign.builder()
                .client(new OkHttpClient())
                .retryer(new Retryer.Default(300,300,3))
                .options(new Request.Options(3000,3000))
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger())
                .logLevel(feign.Logger.Level.FULL)
                .target(RpcHandler.class,"https://api.baobab.klaytn.net:8651");

        RpcRequest request = new RpcRequest();
        request.setId(1);
        request.setMethod("admin_datadir");

        RpcResponse response = handler.call(request);
        logger.info("@@@@ RESPONSE : " + JsonUtil.converToGson(response).toString() + " @@@@");
    }

    
    @Test
    public void JSONRPC_ERROR_HANDER_TEST() throws Exception {
        RpcRequest request = new RpcRequest();
        request.setId(1);
        request.setMethod("klay_protocolVersion");

        RpcTransfer transfer = new RpcTransfer("http://localhost:8551");
        RpcResponse response = transfer.call(request);
        logger.info("@@@@ RPCRESPONSE : " + response.toString() + " @@@@");

    }


    @Test
    public void OPENFEIGN_RETRYER_INTERVAL_TEST() throws Exception {
        long interval = (long)(5000 * Math.pow(1.5D, (double)(3 - 1)));
        logger.info("@@@@ INTERVAL : " + interval + " @@@@");
        logger.info("#### INTERVAL IS : " + (interval > 5000 ? 5000 : interval) + " ####");
    }
    

    @SpringBootApplication
    static class init {

    }
}
