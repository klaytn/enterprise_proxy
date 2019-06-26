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
package com.klaytn.enterpriseproxy.router.scheduler;

import com.klaytn.enterpriseproxy.router.model.ServiceChainHost;
import com.klaytn.enterpriseproxy.router.model.ServiceChainHostHealthCheck;
import com.klaytn.enterpriseproxy.router.service.ChainInfoServices;
import com.klaytn.enterpriseproxy.router.service.HealthCheckerService;
import com.klaytn.enterpriseproxy.rpc.common.model.RpcMethods;
import com.klaytn.enterpriseproxy.rpc.common.model.RpcResponse;
import com.klaytn.enterpriseproxy.rpc.common.transfer.RpcTransfer;
import com.klaytn.enterpriseproxy.rpc.common.util.RpcUtils;
import com.klaytn.enterpriseproxy.utils.CollectionUtil;
import com.klaytn.enterpriseproxy.utils.ObjectUtil;
import com.klaytn.enterpriseproxy.utils.SchedulerAbstract;
import com.klaytn.enterpriseproxy.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class HealthCheckScheduler extends SchedulerAbstract {
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckScheduler.class);

    @Autowired
    private ChainInfoServices chainInfoServices;

    @Autowired
    private HealthCheckerService healthCheckerService;



    
    /**
     * schedule runner
     * 
     * @override
     */
    @Override
    public void runner() {
        logger.info("@@@@ HEALTH CHECK SCHEDULER RUNNER [" + Thread.currentThread().getName() + "] @@@@");

        // Chain info host get list
        List<ServiceChainHost> chainInfoList = chainInfoServices.getChainHostByDate(new Date());
        if (CollectionUtil.isEmpty(chainInfoList)) {
            logger.info("@@@@ HEALTH CHECKER LIST NULL @@@@");
            return;
        }


        // chain host is alive check
        List<ServiceChainHost> aliveChainHostList = new ArrayList<>();
        List<ServiceChainHost> deadChainHostList = new ArrayList<>();
        List<ServiceChainHostHealthCheck> healthCheckLogList = new ArrayList<>();

        for (ServiceChainHost chainHostInfo : chainInfoList) {
            if (ObjectUtil.isEmpty(chainHostInfo)) {
                continue;
            }


            // Call each host to check their status
            String host = chainHostInfo.getHost();
            int port = chainHostInfo.getPort();
            int isAlive = chainHostInfo.getIsAlive();


            // dead host status add
            String rpcHost = StringUtil.trim(host + ":" + port);
            RpcTransfer transfer = new RpcTransfer(rpcHost);
            RpcResponse response = transfer.call(RpcUtils.madeRequest(RpcMethods.KLAY_BLOCKNUMBER));
            if (ObjectUtil.isNotEmpty(response.getError())) {
                if (isAlive == 1) {
                    deadChainHostList.add(chainHostInfo);
                }

                continue;
            }


            // alive host add
            aliveChainHostList.add(chainHostInfo);


            // service chain host health check log add
            ServiceChainHostHealthCheck chainHostHealthCheckLog = new ServiceChainHostHealthCheck();
            chainHostHealthCheckLog.setChainHostId(chainHostInfo.getId());
            chainHostHealthCheckLog.setCheckType(1);

            String comment = (String) ((ObjectUtil.isNotEmpty(response.getError())) ? response.getError() : response.getResult());
            chainHostHealthCheckLog.setComment(comment);
            healthCheckLogList.add(chainHostHealthCheckLog);
        }


        // chain health check log bulk insert
        healthCheckerService.bulkInsert(healthCheckLogList);

        // is alive --> dead update
        healthCheckerService.isAliveUpdateChainHost(deadChainHostList,0);

        // dead --> is_alive
        healthCheckerService.isAliveUpdateChainHost(aliveChainHostList,1);
    }


    /**
     * schedule execute duration
     * - default : 3sec
     * @return
     */
    @Override
    public Trigger getTrigger() {
        return new PeriodicTrigger(3,TimeUnit.SECONDS);
    }
}