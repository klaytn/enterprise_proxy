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
package com.klaytn.enterpriseproxy.router.repository;

import com.klaytn.enterpriseproxy.router.model.ServiceChainHost;
import com.klaytn.enterpriseproxy.router.model.ServiceChainHostHealthCheck;
import com.klaytn.enterpriseproxy.utils.CollectionUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class HealthCheckerRepository {
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckerRepository.class);
    private static final String MAPPER = "com.klaytn.enterpriseproxy.mapper.healthchecker.";

    @Autowired
    @Qualifier("opDataBaseSessionTemplate")
    private SqlSessionTemplate template;




    /**
     * insert chain host health check log
     *
     * @param chainHostHealthCheckLogList
     */
    public void insertChainHostHealthCheckLog(List<ServiceChainHostHealthCheck> chainHostHealthCheckLogList) {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("collector",chainHostHealthCheckLogList);
        template.insert(MAPPER + "insertChainHostHealthCheckLog",params);
        logger.info("@@@@ INSERT CHAIN HOST HEALTH CHECK LOG @@@@");
    }


    /**
     * chain host is alive value update
     *
     * @param chainHostInfoList
     * @param isAlive
     */
    public void updateIsAliveChainInfoHost(List<ServiceChainHost> chainHostInfoList,int isAlive) {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("collector",chainHostInfoList);
        params.put("isAlive",isAlive);
        template.update(MAPPER + "updateIsAliveChainInfoHost",params);
        logger.info("@@@@ UPDATE IS ALIVE CHAIN HOST @@@@");
    }


    /**
     * select health check log
     *  - 임시적으로 500개만 가져오는것으로
     *
     * @return
     */
    public List<ServiceChainHostHealthCheck> selectServiceChainHostHealthCheckList() {
        return template.selectList(MAPPER + "selectServiceChainHostHealthCheckList");
    }
}