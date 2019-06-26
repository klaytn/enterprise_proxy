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

import com.klaytn.enterpriseproxy.router.model.ChainInfoBasic;
import com.klaytn.enterpriseproxy.router.model.ServiceChain;
import com.klaytn.enterpriseproxy.router.model.ServiceChainHost;
import com.klaytn.enterpriseproxy.utils.CollectionUtil;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ChainInfoRepository {
    private static final String MAPPER = "com.klaytn.enterpriseproxy.mapper.servicechain.";


    @Autowired
    @Qualifier("opDataBaseSessionTemplate")
    private SqlSessionTemplate template;




    /**
     * Service Chain by id
     *
     * @param id
     * @return
     */
    public ServiceChain selectChainInfoById(long id) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",id);
        return template.selectOne(MAPPER + "selectChainInfoById",params);
    }


    /**
     * Service Chain Host by id
     *
     * @param id
     * @return
     */
    public ServiceChainHost selectChainHostById(long id) {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("id",id);
        return template.selectOne(MAPPER + "selectChainHostById",params);
    }


    /**
     * chain info by id, date
     *
     * @param id
     * @param date
     * @return
     */
    public ServiceChain selectChainInfoByIdAndDate(long id,Date date) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",id);
        params.put("date",date);
        return template.selectOne(MAPPER + "selectChainInfoByIdAndDate",params);
    }


    /**
     * chain info basic by id
     *
     * @param id
     * @return
     */
    public ChainInfoBasic selectChainInfoBasicByid(long id) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",id);
        return template.selectOne(MAPPER + "selectChainInfoBasicByid",params);
    }


    /**
     * 특정 날짜 기준으로 현재 운영중인 체인 리스트 정보 가져오기
     *
     * @param toDate
     * @return
     */
    public ChainInfoBasic selectChainInfoBasicByDate(Date toDate) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("toDate",toDate);
        return template.selectOne(MAPPER + "selectChainInfoBasicByDate",params);
    }


    /**
     * chain info basic by owner
     *
     * @param owner
     * @return
     */
    public List<ChainInfoBasic> selectChainInfoBasicByOwner(String owner) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("owner",owner);
        return template.selectList(MAPPER + "selectChainInfoBasicByOwner",params);
    }


    /**
     * all chain info basic list
     *
     * @return
     */
    public List<ChainInfoBasic> selectChainInfoBasicAll() {
        return template.selectList(MAPPER + "selectChainInfoBasicAll",null);
    }


    /**
     * service chain insert
     *
     * @param serviceChain
     * @return
     * @throws SQLException
     */
    public long insertServiceChain(ServiceChain serviceChain) throws SQLException {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("name",serviceChain.getName());
        params.put("owner",serviceChain.getOwner());
        params.put("startDate",serviceChain.getStartDate());
        params.put("endDate",serviceChain.getEndDate());
        params.put("ipAddr",serviceChain.getIpAddr());
        long id = template.insert(MAPPER + "insertServiceChain",params);
        return (long) params.get("id");
    }


    /**
     * service chain update
     *
     * @param serviceChain
     * @throws SQLException
     */
    public void updateServiceChain(ServiceChain serviceChain) throws SQLException {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("id",serviceChain.getId());
        params.put("name",serviceChain.getName());
        params.put("owner",serviceChain.getOwner());
        params.put("isUse",serviceChain.getIsUse());
        params.put("startDate",serviceChain.getStartDate());
        params.put("endDate",serviceChain.getEndDate());
        params.put("ipAddr",serviceChain.getIpAddr());
        template.update(MAPPER + "updateServiceChain",params);
    }


    /**
     * service chain delete
     *
     * @param id
     * @throws SQLException
     */
    public void deleteServiceChain(long id) throws SQLException {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("id",id);
        template.delete(MAPPER + "deleteServiceChain",params);
    }


    /**
     * service chain host insert
     *
     * @param serviceChainHost
     * @return
     * @throws SQLException
     */
    public long insertServiceChainHost(ServiceChainHost serviceChainHost) throws SQLException {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("serviceChainId",serviceChainHost.getServiceChainId());
        params.put("host",serviceChainHost.getHost());
        params.put("port",serviceChainHost.getPort());
        params.put("category",serviceChainHost.getCategory());
        params.put("ipAddr",serviceChainHost.getIpAddr());
        long id = template.insert(MAPPER + "insertServiceChainHost",params);
        return (long) params.get("id");
    }


    /**
     * service chain host update
     *
     * @param serviceChainHost
     * @throws SQLException
     */
    public void updateServiceChainHost(ServiceChainHost serviceChainHost) throws SQLException {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("serviceChainId",serviceChainHost.getServiceChainId());
        params.put("host",serviceChainHost.getHost());
        params.put("port",serviceChainHost.getPort());
        params.put("isUse",serviceChainHost.getIsUse());
        params.put("category",serviceChainHost.getCategory());
        params.put("rpcType",serviceChainHost.getRpcType());
        params.put("isAlive",serviceChainHost.getIsAlive());
        params.put("ipAddr",serviceChainHost.getIpAddr());
        template.update(MAPPER + "updateServiceChainHost",params);
    }


    /**
     * service chain host delete
     *
     * @param id
     * @throws SQLException
     */
    public void deleteServiceChainHost(long id) throws SQLException {
        Map<String,Object> params = CollectionUtil.createHashMap();
        params.put("id",id);
        template.delete(MAPPER + "deleteServiceChainHost",params);
    }
}
