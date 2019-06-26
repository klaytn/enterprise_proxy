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
package com.klaytn.enterpriseproxy.api.operation.service;

import com.klaytn.enterpriseproxy.api.common.model.ApiResponse;
import com.klaytn.enterpriseproxy.api.common.model.ApiResponseCode;
import com.klaytn.enterpriseproxy.api.common.model.ApiResponseTarget;
import com.klaytn.enterpriseproxy.api.common.util.ApiUtils;
import com.klaytn.enterpriseproxy.router.model.ChainInfoStatusCode;
import com.klaytn.enterpriseproxy.router.model.ServiceChain;
import com.klaytn.enterpriseproxy.router.model.ServiceChainHost;
import com.klaytn.enterpriseproxy.router.model.ValidChainInfo;
import com.klaytn.enterpriseproxy.router.service.ChainInfoServices;
import com.klaytn.enterpriseproxy.router.service.HealthCheckerService;
import com.klaytn.enterpriseproxy.utils.DateUtil;
import com.klaytn.enterpriseproxy.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;


@Service
public class ChainRouterService {
    @Autowired
    private ChainInfoServices chainInfoServices;

    @Autowired
    private HealthCheckerService healthCheckerService;




    /**
     * chain all list
     *
     * @return
     */
    public ApiResponse allChain() {
        return ApiUtils.onSuccess(chainInfoServices.getChainInfoBasicAll());
    }


    /**
     * service chain add
     *
     * @param request
     * @param name
     * @param owner
     * @param startDate
     * @param endDate
     * @return
     */
    public ApiResponse addChain(HttpServletRequest request,String name,String owner,String startDate,String endDate) {
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(owner) || StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)) {
            return ApiUtils.onError(ApiResponseCode.PARAMETER_INVALID);
        }


        ServiceChain serviceChain = new ServiceChain();
        serviceChain.setName(name);
        serviceChain.setOwner(owner);
        serviceChain.setStartDate(DateUtil.parseDate(startDate,DateUtil.FULL_KOREAN_PATTERN));
        serviceChain.setEndDate(DateUtil.parseDate(endDate,DateUtil.FULL_KOREAN_PATTERN));
        serviceChain.setIpAddr(ApiUtils.getIpAddress(request));

        ValidChainInfo validChainInfo = chainInfoServices.addServiceChain(serviceChain);
        
        ChainInfoStatusCode chainInfoStatusCode = validChainInfo.getChainInfoStatusCode();
        if (ChainInfoStatusCode.SUCCESS.equals(chainInfoStatusCode)) {
            serviceChain.setId(validChainInfo.getChainInfo().getId());
            return ApiUtils.onSuccess(serviceChain);

        }

        return ApiUtils.onError(ApiResponseTarget.ROUTER,ApiResponseCode.DB_ERROR,validChainInfo.getErrorMessage());
    }


    /**
     * service chain modify
     *
     * @param request
     * @param id
     * @param name
     * @param owner
     * @param isUse
     * @param startDate
     * @param endDate
     * @return
     */
    public ApiResponse modifyChain(HttpServletRequest request,long id,String name,String owner,int isUse,String startDate,String endDate) {
        if (id <= 0L) {
            return ApiUtils.onError(ApiResponseCode.PARAMETER_INVALID);
        }

        ServiceChain serviceChain = new ServiceChain();
        serviceChain.setId(id);
        serviceChain.setName(name);
        serviceChain.setOwner(owner);
        serviceChain.setIsUse(isUse);
        serviceChain.setStartDate(DateUtil.parseDate(startDate,DateUtil.FULL_KOREAN_PATTERN));
        serviceChain.setEndDate(DateUtil.parseDate(endDate,DateUtil.FULL_KOREAN_PATTERN));
        serviceChain.setIpAddr(ApiUtils.getIpAddress(request));

        ValidChainInfo validChainInfo = chainInfoServices.modifyServiceChain(serviceChain);
        
        ChainInfoStatusCode chainInfoStatusCode = validChainInfo.getChainInfoStatusCode();
        return (ChainInfoStatusCode.SUCCESS.equals(chainInfoStatusCode)) ?
                ApiUtils.onSuccess(ApiResponseCode.SUCCESS) :
                ApiUtils.onError(
                        ApiResponseTarget.ROUTER,
                        ApiResponseCode.DB_ERROR,
                        validChainInfo.getErrorMessage()
                );
    }


    /**
     * service chain del
     *
     * @param id
     * @return
     */
    public ApiResponse delChain(long id) {
        if (id <= 0L) {
            return ApiUtils.onError(ApiResponseCode.PARAMETER_INVALID);
        }

        ValidChainInfo validChainInfo = chainInfoServices.delServiceChain(id);
        return (ChainInfoStatusCode.SUCCESS.equals(validChainInfo.getChainInfoStatusCode())) ?
                ApiUtils.onSuccess(ApiResponseCode.SUCCESS) :
                ApiUtils.onError(
                        ApiResponseTarget.ROUTER,
                        ApiResponseCode.DB_ERROR,
                        validChainInfo.getErrorMessage()
                );
    }


    /**
     * service chain host add
     *
     * @param request
     * @param serviceChainId
     * @param category
     * @param host
     * @param port
     * @return
     */
    public ApiResponse addChainHost(HttpServletRequest request,long serviceChainId,String category,String host,int port) {
        if (serviceChainId <= 0L || (StringUtil.isEmpty(host)) || port <= 0) {
            return ApiUtils.onError(ApiResponseCode.PARAMETER_INVALID);
        }

        ServiceChainHost serviceChainHost = new ServiceChainHost();
        serviceChainHost.setServiceChainId(serviceChainId);
        serviceChainHost.setCategory(category);
        serviceChainHost.setHost(host);
        serviceChainHost.setPort(port);
        serviceChainHost.setIpAddr(ApiUtils.getIpAddress(request));

        ValidChainInfo validChainInfo = chainInfoServices.addServiceChainHost(serviceChainHost);

        ChainInfoStatusCode chainInfoStatusCode = validChainInfo.getChainInfoStatusCode();
        if (ChainInfoStatusCode.SUCCESS.equals(chainInfoStatusCode)) {
            serviceChainHost.setId(validChainInfo.getChainHostInfo().getId());
            return ApiUtils.onSuccess(serviceChainHost);
        }

        return ApiUtils.onError(ApiResponseTarget.ROUTER,ApiResponseCode.DB_ERROR,validChainInfo.getErrorMessage());
    }


    /**
     * service chain host modify
     *
     * @param request
     * @param id
     * @param serviceChainId
     * @param host
     * @param port
     * @param isUse
     * @param category
     * @param rpcType
     * @param isAlive
     * @return
     */
    public ApiResponse modifyChainHost(HttpServletRequest request,long id,long serviceChainId,String host,int port,int isUse,String category,int rpcType,int isAlive) {
        if (id <= 0L) {
            return ApiUtils.onError(ApiResponseCode.PARAMETER_INVALID);
        }

        ServiceChainHost serviceChainHost = new ServiceChainHost();
        serviceChainHost.setId(id);
        serviceChainHost.setHost(host);
        serviceChainHost.setPort(port);
        serviceChainHost.setIsUse(isUse);
        serviceChainHost.setRpcType(rpcType);
        serviceChainHost.setIsAlive(isAlive);
        serviceChainHost.setCategory(category);
        serviceChainHost.setServiceChainId(serviceChainId);
        serviceChainHost.setIpAddr(ApiUtils.getIpAddress(request));

        ValidChainInfo validChainInfo = chainInfoServices.modifyServiceChainHost(serviceChainHost);

        ChainInfoStatusCode chainInfoStatusCode = validChainInfo.getChainInfoStatusCode();
        return (ChainInfoStatusCode.SUCCESS.equals(chainInfoStatusCode)) ?
                ApiUtils.onSuccess(ApiResponseCode.SUCCESS) :
                ApiUtils.onError(
                        ApiResponseTarget.ROUTER,
                        ApiResponseCode.DB_ERROR,
                        validChainInfo.getErrorMessage()
                );
    }


    /**
     * service chain host del
     *
     * @param id
     * @return
     */
    public ApiResponse delChainHost(long id) {
        if (id <= 0L) {
            return ApiUtils.onError(ApiResponseCode.PARAMETER_INVALID);
        }

        ValidChainInfo validChainInfo = chainInfoServices.delServiceChainHost(id);
        return (ChainInfoStatusCode.SUCCESS.equals(validChainInfo.getChainInfoStatusCode())) ?
                ApiUtils.onSuccess(ApiResponseCode.SUCCESS) :
                ApiUtils.onError(
                        ApiResponseTarget.ROUTER,
                        ApiResponseCode.DB_ERROR,
                        validChainInfo.getErrorMessage()
                );
    }


    /**
     * all health check log list
     *
     * @return
     */
    public ApiResponse allHealthCheck() {
        return ApiUtils.onSuccess(healthCheckerService.getHealthCheckList());
    }
}