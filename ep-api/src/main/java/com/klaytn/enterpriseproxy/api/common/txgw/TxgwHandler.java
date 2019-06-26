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
package com.klaytn.enterpriseproxy.api.common.txgw;

import com.klaytn.enterpriseproxy.txgw.scheduler.LocalQueueScheduler;
import com.klaytn.enterpriseproxy.txgw.service.TxgwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;


@Component
public class TxgwHandler {
    @Autowired
    private TxgwService service;

    
    @Autowired
    private LocalQueueScheduler scheduler;




    /**
     * http request log insert
     *
     * @param request
     */
    @Async
    public void add(HttpServletRequest request) {
        service.execution(request);
    }


    /**
     * scheduler start
     * 
     * @PostConstruct
     */
    @PostConstruct
    public void init() {
        scheduler.start();
    }


    /**
     * scheduler stop
     *
     * @PreDestory
     */
    @PreDestroy
    public void destroy() {
        scheduler.stop();
    }
}