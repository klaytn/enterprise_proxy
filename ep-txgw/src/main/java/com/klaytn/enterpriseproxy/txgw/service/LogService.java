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
package com.klaytn.enterpriseproxy.txgw.service;

import com.klaytn.enterpriseproxy.txgw.model.TxGatewayLog;
import com.klaytn.enterpriseproxy.txgw.repository.LogRepository;
import com.klaytn.enterpriseproxy.utils.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    @Autowired
    private LogRepository repository;




    /**
     * tx gateway log bulk insert
     *
     * @param txGatewayLogList
     */
    @Async("txGatewayAsyncExecutor")
    public void bulkInsertTxGatewayLog(List<TxGatewayLog> txGatewayLogList) {
        if (CollectionUtil.isEmpty(txGatewayLogList)) {
            return;
        }
        
        try {
            repository.insertTxGatewayLog(txGatewayLogList);
        }
        catch (Exception e) {
            logger.error("@@@@ BULK INSERT TX GATEWAY LOG : " + e.getLocalizedMessage() + " @@@@");
        }
    }
}