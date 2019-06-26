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
package com.klaytn.enterpriseproxy.txgw.repository;

import com.klaytn.enterpriseproxy.txgw.model.TxGatewayLog;
import com.klaytn.enterpriseproxy.utils.CollectionUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Repository
public class LogRepository {
    private static final String MAPPER = "com.klaytn.enterpriseproxy.mapper.txgw.";


    @Autowired
    @Qualifier("logDataBaseSessionTemplate")
    private SqlSessionTemplate template;




    /**
     * tx gateway log bulk insert
     *
     * @param txGatewayLogList
     */
    public void insertTxGatewayLog(List<TxGatewayLog> txGatewayLogList) throws SQLException {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("collector",txGatewayLogList);
        template.insert(MAPPER + "insertTxGatewayLog",params);
    }


    /**
     * tx gateway list
     *
     * @return
     */
    public List<TxGatewayLog> selectTxGateWayLogList() {
        return template.selectList(MAPPER + "selectTxGateWayLogList");
    }
}