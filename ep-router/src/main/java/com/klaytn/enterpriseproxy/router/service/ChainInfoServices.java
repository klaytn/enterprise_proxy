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

import com.klaytn.enterpriseproxy.router.model.*;
import com.klaytn.enterpriseproxy.utils.*;
import com.klaytn.enterpriseproxy.router.repository.ChainInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;


@Service
public class ChainInfoServices {
    private static final Logger logger = LoggerFactory.getLogger(ChainInfoServices.class);
    
    @Autowired
    private ChainInfoRepository repository;




    /**
     * Check if the chain is normal
     *
     * @param chainInfoId
     * @param chainHostInfoId
     * @return
     */
    public ValidChainInfo getValidChainInfo(long chainInfoId,long chainHostInfoId) {
        ValidChainInfo validChainInfo = new ValidChainInfo();

        // parameter valid
        if (chainInfoId <= 0L || chainHostInfoId <= 0L) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.PARAMETER_INVALID);
            return validChainInfo;
        }

        // Get Chain Information
        ChainInfoBasic chainInfoBasic = repository.selectChainInfoBasicByid(chainInfoId);
        if (ObjectUtil.isEmpty(chainInfoBasic)) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_IS_NOT_EXIST);
            return validChainInfo;
        }


        // If there is no registered host information
        List<ServiceChainHost> chainHostInfoList = chainInfoBasic.getChainHostInfoList();
        if (ArrayUtil.isEmpty(chainHostInfoList)) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_IS_NOT_EXIST);
            return validChainInfo;
        }


        // Check for actual use
        int isUse = chainInfoBasic.getIsUse();
        if (isUse == 0) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_IS_NOT_USE);
            return validChainInfo;
        }


        // Check chain availability date
        Date toDate = new Date();
        Date startDate = chainInfoBasic.getStartDate();
        Date endDate = chainInfoBasic.getEndDate();
        if (DateUtil.compareDate(toDate,startDate) == -1 && DateUtil.compareDate(endDate,toDate) == -1) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_EXPIRE);
            return validChainInfo;
        }


        // Check host information
        ServiceChainHost targetChainHostInfo = this._matchServiceChainHostById(chainHostInfoList,chainHostInfoId);
        if (ObjectUtil.isEmpty(targetChainHostInfo)) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_IS_NOT_EXIST);
            return validChainInfo;
        }

        int chainHostIsUse = targetChainHostInfo.getIsUse();
        if (chainHostIsUse == 0) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_IS_NOT_USE);
            return validChainInfo;
        }

        int chainHostIsAlive = targetChainHostInfo.getIsAlive();
        if (chainHostIsAlive == 0) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_IS_NOT_ALIVE);
            return validChainInfo;
        }


        // return
        ServiceChain chainInfo = new ServiceChain();
        chainInfo.setId(chainInfoBasic.getId());
        chainInfo.setIsUse(chainInfoBasic.getIsUse());
        chainInfo.setName(chainInfoBasic.getName());
        chainInfo.setStartDate(chainInfoBasic.getStartDate());
        chainInfo.setEndDate(chainInfoBasic.getEndDate());
        chainInfo.setOwner(chainInfoBasic.getOwner());

        validChainInfo.setChainInfo(chainInfo);
        validChainInfo.setChainHostInfo(targetChainHostInfo);
        validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.SUCCESS);
        return validChainInfo;
    }


    /**
     * list match by matchId
     *
     * @param chainHostInfoList
     * @param matchId
     * @return
     */
    private ServiceChainHost _matchServiceChainHostById(List<ServiceChainHost> chainHostInfoList,long matchId) {
        for (ServiceChainHost chainHostInfo : chainHostInfoList) {
            if (ObjectUtil.isEmpty(chainHostInfo)) {
                continue;
            }

            long hostId = chainHostInfo.getId();
            if (hostId == matchId) {
                logger.info("@@@@ SERVICE CHAIN HOST MATCH : " + chainHostInfo.toString() + " @@@@");
                return chainHostInfo;
            }
        }

        return null;
    }


    /**
     * Get operational Chain Host information by specific date
     *
     * @param toDate
     * @return
     */
    public List<ServiceChainHost> getChainHostByDate(Date toDate) {
        if (ObjectUtil.isEmpty(toDate)) {
            toDate = new Date();
        }

        ChainInfoBasic chainInfoBasic = repository.selectChainInfoBasicByDate(toDate);
        List<ServiceChainHost> chainHostInfoList = CollectionUtil.createArrayList();

        if (ObjectUtil.isEmpty(chainInfoBasic)) {
            return chainHostInfoList;
        }

        logger.info("@@@@ CHAIN INFO BASIC : " + chainInfoBasic.toString() + " @@@@");
        
        return chainInfoBasic.getChainHostInfoList();
    }

    
    /**
     * get chain info by id
     *
     * @param id
     * @return
     */
    public ServiceChain getChainInfoById(long id) {
        if (id <= 0L) {
            return null;
        }

        return repository.selectChainInfoById(id);
    }


    /**
     * get chain host by id
     *
     * @param id
     * @return
     */
    public ServiceChainHost getChainHostById(long id) {
        if (id <= 0L) {
            return null;
        }

        return repository.selectChainHostById(id);
    }


    /**
     * chain info basic by id
     *
     * @param id
     * @return
     */
    public ChainInfoBasic getChainInfoBasicById(long id) {
        if (id <= 0L) {
            return null;
        }

        return repository.selectChainInfoBasicByid(id);
    }


    /**
     * chain info basic by owner
     *
     * @param owner
     * @return
     */
    public List<ChainInfoBasic> getChainInfoBasicByOwner(String owner) {
        if (StringUtil.isEmpty(owner)) {
            return CollectionUtil.createArrayList();
        }

        return repository.selectChainInfoBasicByOwner(owner);
    }


    /**
     * chain info basic all list
     *
     * @return
     */
    public List<ChainInfoBasic> getChainInfoBasicAll() {
        return repository.selectChainInfoBasicAll();
    }


    /**
     * service chain create
     *
     * @param serviceChain
     * @return 
     */
    public ValidChainInfo addServiceChain(ServiceChain serviceChain) {
        ValidChainInfo validChainInfo = new ValidChainInfo();

        try {
            long id = repository.insertServiceChain(serviceChain);
            serviceChain.setId(id);
            validChainInfo.setChainInfo(serviceChain);
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.SUCCESS);
        }
        catch (Exception e) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_INSERT_FAIL);
            validChainInfo.setErrorMessage(e.getLocalizedMessage());
        }

        return validChainInfo;
    }


    /**
     * service chain modify
     *
     * @param serviceChain
     * @return
     */
    public ValidChainInfo modifyServiceChain(ServiceChain serviceChain) {
        ValidChainInfo validChainInfo = new ValidChainInfo();

        long serviceChainId = serviceChain.getId();
        if (ObjectUtil.isEmpty(serviceChainId)) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.PARAMETER_INVALID);
            return validChainInfo;
        }

        if (ObjectUtil.isEmpty(this.getChainInfoById(serviceChainId))) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_IS_NOT_EXIST);
            return validChainInfo;
        }

        try {
            repository.updateServiceChain(serviceChain);
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.SUCCESS);
        }
        catch (Exception e) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_UPDATE_FAIL);
            validChainInfo.setErrorMessage(e.getLocalizedMessage());
        }

        return validChainInfo;
    }


    /**
     * service chain del
     *
     * @param id
     * @return
     */
    public ValidChainInfo delServiceChain(long id) {
        ValidChainInfo validChainInfo = new ValidChainInfo();
        if (id <= 0L) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.PARAMETER_INVALID);
            return validChainInfo;
        }

        try {
            repository.deleteServiceChain(id);
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.SUCCESS);
        }
        catch (Exception e) {
            validChainInfo.setErrorMessage(e.getLocalizedMessage());
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_DELETE_FAIL);
        }

        return validChainInfo;
    }


    /**
     * service chain host create
     *
     * @param serviceChainHost
     * @return
     */
    public ValidChainInfo addServiceChainHost(ServiceChainHost serviceChainHost) {
        ValidChainInfo validChainInfo = new ValidChainInfo();
        if (ObjectUtil.isEmpty(serviceChainHost)) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.PARAMETER_INVALID);
            return validChainInfo;
        }

        long serviceChainId = serviceChainHost.getServiceChainId();
        ServiceChain serviceChain = this.getChainInfoById(serviceChainId);
        if (ObjectUtil.isEmpty(serviceChain)) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_IS_NOT_EXIST);
            return validChainInfo;
        }

        try {
            long serviceChainHostId = repository.insertServiceChainHost(serviceChainHost);
            serviceChainHost.setId(serviceChainHostId);
            validChainInfo.setChainHostInfo(serviceChainHost);
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.SUCCESS);
        }
        catch (Exception e) {
            validChainInfo.setErrorMessage(e.getLocalizedMessage());
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_INSERT_FAIL);
        }

        return validChainInfo;
    }


    /**
     * service chain host update
     *
     * @param serviceChainHost
     * @return
     */
    public ValidChainInfo modifyServiceChainHost(ServiceChainHost serviceChainHost) {
        ValidChainInfo validChainInfo = new ValidChainInfo();
        if (ObjectUtil.isEmpty(serviceChainHost)) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.PARAMETER_INVALID);
            return validChainInfo;
        }

        long serviceChainId = ObjectUtil.isEmpty(serviceChainHost.getServiceChainId()) ? 0 : serviceChainHost.getServiceChainId();
        if (ObjectUtil.isEmpty(this.getChainInfoById(serviceChainId))) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_IS_NOT_EXIST);
            return validChainInfo;
        }

        long serviceChainHostId = ObjectUtil.isEmpty(serviceChainHost.getId()) ? 0 : serviceChainHost.getId();
        if (ObjectUtil.isEmpty(this.getChainHostById(serviceChainHostId))) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_IS_NOT_EXIST);
            return validChainInfo;
        }

        try {
            repository.updateServiceChainHost(serviceChainHost);
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.SUCCESS);
        } catch (Exception e) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_UPDATE_FAIL);
            validChainInfo.setErrorMessage(e.getLocalizedMessage());
        }

        return validChainInfo;
    }


    /**
     * service chain host del
     *
     * @param id
     * @return
     */
    public ValidChainInfo delServiceChainHost(long id) {
        ValidChainInfo validChainInfo = new ValidChainInfo();
        if (id <= 0L) {
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.PARAMETER_INVALID);
            return validChainInfo;
        }

        try {
            repository.deleteServiceChainHost(id);
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.SUCCESS);
        }
        catch (Exception e) {
            validChainInfo.setErrorMessage(e.getLocalizedMessage());
            validChainInfo.setChainInfoStatusCode(ChainInfoStatusCode.CHAIN_HOST_DELETE_FAIL);
        }

        return validChainInfo;
    }
}