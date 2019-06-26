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
import com.klaytn.enterpriseproxy.utils.DateUtil;
import com.klaytn.enterpriseproxy.utils.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
@Import(TestOpDatabaseConfig.class)
public class ChainInfoTest {
    private static final Logger logger = LoggerFactory.getLogger(ChainInfoTest.class);



    
    @Test
    public void PARAMETER_TEST() throws Exception {
        String name = "";
        String owner = "test";

        if (StringUtil.isEmpty(name) && StringUtil.isEmpty(owner)) {
            logger.info("1");
        } else {
            logger.info("2");
        }
    }


    @Test
    public void DATE_COMPARE_TEST() throws Exception {
        Date toDate = new Date();
        String startDateStr = "2019-01-01 00:00:00";
        Date startDate = DateUtil.parseDate(startDateStr,DateUtil.FULL_KOREAN_PATTERN);
        String endDateStr = "2022-12-31 23:59:59";
        Date endDate = DateUtil.parseDate(endDateStr,DateUtil.FULL_KOREAN_PATTERN);

        logger.info("@@@@ TODATE, START DATE : " + DateUtil.compareDate(toDate,startDate) + " @@@@");
        logger.info("@@@@ TODATE, END DATE : " + DateUtil.compareDate(endDate,toDate) + " @@@@");
    }


    @SpringBootApplication
    static class init {

    }
}