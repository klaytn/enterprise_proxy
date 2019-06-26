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
package com.klaytn.enterpriseproxy.router;

import com.klaytn.enterpriseproxy.router.config.TestOpDatabaseConfig;
import com.klaytn.enterpriseproxy.router.model.ChainInfoBasic;
import com.klaytn.enterpriseproxy.router.scheduler.HealthCheckScheduler;
import com.klaytn.enterpriseproxy.router.service.ChainInfoServices;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@SpringBootTest
@SpringBootConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@Import(TestOpDatabaseConfig.class)
public class RouterDataBaseTest {
    private static final Logger logger = LoggerFactory.getLogger(RouterDataBaseTest.class);

    @Autowired
    private ChainInfoServices services;

    @Autowired
    private HealthCheckScheduler scheduler;


    @Test
    public void GET_CHAINBASICINFO_TEST() {
        ChainInfoBasic chainInfoBasic = services.getChainInfoBasicById(1);
        logger.info("@@@@ CHAIN INFO BASIC BY ID : " + chainInfoBasic.toString() + " @@@@");

        List<ChainInfoBasic> chainInfoBasicList = services.getChainInfoBasicByOwner("groundx");
        logger.info("@@@@ CHAIN INFO BASIC BY OWNER : " + chainInfoBasicList.toString() + " @@@@");

        List<ChainInfoBasic> chainInfoBasicAllList = services.getChainInfoBasicAll();
        logger.info("@@@@ CHAIN INFO BASIC ALL LIST : " + chainInfoBasicAllList.toString() + " @@@@");
    }


    @Test
    public void SCHEDULER_TEST() throws Exception {
        scheduler.start();
        Thread.sleep(5000000);
    }


    @SpringBootApplication
    static class init {
        
    }
}