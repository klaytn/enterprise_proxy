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
package com.klaytn.enterpriseproxy.txgw.scheduler;

import com.klaytn.enterpriseproxy.txgw.model.TxGatewayLog;
import com.klaytn.enterpriseproxy.txgw.util.TxgwUtils;
import com.klaytn.enterpriseproxy.queue.local.Local;
import com.klaytn.enterpriseproxy.txgw.service.LogService;
import com.klaytn.enterpriseproxy.utils.CollectionUtil;
import com.klaytn.enterpriseproxy.utils.ObjectUtil;
import com.klaytn.enterpriseproxy.utils.SchedulerAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class LocalQueueScheduler extends SchedulerAbstract {
    private static final Logger logger = LoggerFactory.getLogger(LocalQueueScheduler.class);

    /**
     * local queue
     * @type HttpServletRequest
     */
    private Local<TxGatewayLog> localQueue;


    @Autowired
    private LogService service;




    /**
     * do not editable
     *  - ep local queue key
     */
    @PostConstruct
    public void init() {
        localQueue = Local.build(TxgwUtils.TXGW_QUEUE_KEY);
    }

    
    @Override
    public void runner() {
        logger.info("@@@@ LOCAL QUEUE SCHEDULER RUNNER [" + Thread.currentThread().getName() + "] @@@@");

        long localQueueCount = localQueue.getQueueSize();
        if (localQueueCount == 0) {
            return;
        }

        // txgw log insert
        if (localQueueCount >= 50) {
            List<TxGatewayLog> queueList = localQueue.getQueueList();
            if (CollectionUtil.isEmpty(queueList)) {
                return;
            }

            List<TxGatewayLog> insertLogList = CollectionUtil.createArrayList();
            for (TxGatewayLog txGatewayLog : queueList) {
                if (ObjectUtil.isEmpty(txGatewayLog)) {
                    continue;
                }

                insertLogList.add(txGatewayLog);
            }

            if (CollectionUtil.isEmpty(insertLogList)) {
                return;
            }

            localQueue.clearQueue();

            try {
                service.bulkInsertTxGatewayLog(insertLogList);
            }
            catch (Exception e) {
                logger.info("@@@@ LOCAL QUEUE DB INSERT ERROR : " + e.getLocalizedMessage() + " @@@@");
            }
        }
    }


    @Override
    public Trigger getTrigger() {
        return new PeriodicTrigger(5,TimeUnit.SECONDS);
    }
}