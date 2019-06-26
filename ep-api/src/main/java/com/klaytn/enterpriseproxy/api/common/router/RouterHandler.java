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
package com.klaytn.enterpriseproxy.api.common.router;

import com.klaytn.enterpriseproxy.router.model.ChainInfoStatusCode;
import com.klaytn.enterpriseproxy.router.model.ServiceChainHost;
import com.klaytn.enterpriseproxy.router.model.ServiceChainInfo;
import com.klaytn.enterpriseproxy.router.model.ValidChainInfo;
import com.klaytn.enterpriseproxy.router.scheduler.HealthCheckScheduler;
import com.klaytn.enterpriseproxy.router.service.ChainInfoServices;
import com.klaytn.enterpriseproxy.utils.ObjectUtil;
import com.klaytn.enterpriseproxy.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;


@Component
public class RouterHandler {
    @Autowired
    private ChainInfoServices services;


    @Autowired
    private HealthCheckScheduler scheduler;




    /**
     * get service chain
     *  - If there is chainHost information, call it on the host.
     *  - If you use chainRouter, be sure to check serviceChainId and serviceChainHostId information.
     *
     * @param request
     * @return
     */
    public ServiceChainInfo getServiceChain(HttpServletRequest request) {
        ServiceChainInfo serviceChainInfo = new ServiceChainInfo();

        // 1. If there is targetHost information, check first
        String serviceChainHost = request.getParameter("targetHost");
        if (StringUtil.isNotEmpty(serviceChainHost)) {
            serviceChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.SUCCESS);
            serviceChainInfo.setChainHost(serviceChainHost);
            serviceChainInfo.setChainHostRpcType(1);
            return serviceChainInfo;
        }


        // 2. If you are using a Chain Router
        String serviceChainId = request.getParameter("serviceChainId");
        String serviceChainHostId = request.getParameter("serviceChainHostId");
        if (StringUtil.isEmpty(serviceChainId) || StringUtil.isEmpty(serviceChainHostId)) {
            return null;
        }

        long chainInfoId = Long.parseLong(serviceChainId);
        long chainHostInfoId = Long.parseLong(serviceChainHostId);
        ValidChainInfo validChainInfo = services.getValidChainInfo(chainInfoId,chainHostInfoId);

        ChainInfoStatusCode chainInfoStatusCode = validChainInfo.getChainInfoStatusCode();
        if (ObjectUtil.isEquals(ChainInfoStatusCode.SUCCESS,chainInfoStatusCode)) {
            ServiceChainHost chainHostInfo = validChainInfo.getChainHostInfo();
            String host = chainHostInfo.getHost() + ":" + chainHostInfo.getPort();
            int rpcType = chainHostInfo.getRpcType();

            serviceChainInfo.setChainHost(host);
            serviceChainInfo.setChainHostRpcType(rpcType);
        }

        serviceChainInfo.setChainInfoStatusCode(chainInfoStatusCode);
        return serviceChainInfo;
    }


    /**
     * health checker scheduler start
     *
     * @PostConstruct
     */
    @PostConstruct
    public void init() {
        scheduler.start();
    }


    /**
     * health checker scheduler stop
     * 
     * @PreDestory
     */
    @PreDestroy
    public void destroy() {
        scheduler.stop();
    }
}