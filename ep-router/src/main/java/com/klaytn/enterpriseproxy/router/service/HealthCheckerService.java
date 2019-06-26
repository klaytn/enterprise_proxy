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
package com.klaytn.enterpriseproxy.router.service;

import com.klaytn.enterpriseproxy.router.model.ServiceChainHost;
import com.klaytn.enterpriseproxy.router.model.ServiceChainHostHealthCheck;
import com.klaytn.enterpriseproxy.router.repository.HealthCheckerRepository;
import com.klaytn.enterpriseproxy.utils.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class HealthCheckerService {
    @Autowired
    private HealthCheckerRepository repository;




    /**
     * is not alive chain host bulk insert
     *
     * @param chainHostHealthCheckLogList
     */
    @Async("routerAsyncExecutor")
    public void bulkInsert(List<ServiceChainHostHealthCheck> chainHostHealthCheckLogList) {
        if (CollectionUtil.isEmpty(chainHostHealthCheckLogList)) {
            return;
        }
        
        repository.insertChainHostHealthCheckLog(chainHostHealthCheckLogList);
    }


    /**
     * chain host is alive value update
     *
     * @param chainHostInfoList
     * @param isAlive
     */
    @Async("routerAsyncExecutor")
    public void isAliveUpdateChainHost(List<ServiceChainHost> chainHostInfoList,int isAlive) {
        if (CollectionUtil.isEmpty(chainHostInfoList)) {
            return;
        }

        repository.updateIsAliveChainInfoHost(chainHostInfoList,isAlive);
    }


    /**
     * get health check log list
     *
     * @return
     */
    public List<ServiceChainHostHealthCheck> getHealthCheckList() {
        return repository.selectServiceChainHostHealthCheckList();
    }
}