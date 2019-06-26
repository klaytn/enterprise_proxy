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
package com.klaytn.enterpriseproxy.api.common.interceptor;

import com.klaytn.enterpriseproxy.api.common.model.ApiResponse;
import com.klaytn.enterpriseproxy.api.common.router.RouterHandler;
import com.klaytn.enterpriseproxy.api.common.rpc.RpcHandler;
import com.klaytn.enterpriseproxy.api.common.txgw.TxgwHandler;
import com.klaytn.enterpriseproxy.api.common.util.ApiUtils;
import com.klaytn.enterpriseproxy.router.model.ChainInfoStatusCode;
import com.klaytn.enterpriseproxy.router.model.ServiceChainInfo;
import com.klaytn.enterpriseproxy.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class ApiRequestInterceptor implements HandlerInterceptor {
    @Autowired
    private TxgwHandler txgwHandler;

    @Autowired
    private RouterHandler routerHandler;

    @Autowired
    private RpcHandler rpcHandler;



    
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception {
        this._txgwRequestLogInsert(request);
        
        ChainInfoStatusCode chainInfoStatusCode = this._isValidServiceChainInfo(request);
        if (!ChainInfoStatusCode.SUCCESS.equals(chainInfoStatusCode)) {
            ApiUtils.serviceChainHostErrorResponse(response,chainInfoStatusCode);
            return false;
        }

        ApiResponse apiResponse = this._validateApiInterface(request);
        if (ObjectUtil.isNotNull(apiResponse)) {
            ApiUtils.apisInterfaceErrorResponse(response,apiResponse);
            return false;
        }

        return true;
    }


    /**
     * Meta information used in API call TXGW processing log storage
     * 
     * @param request
     */
    private void _txgwRequestLogInsert(HttpServletRequest request) {
        txgwHandler.add(request);
    }


    /**
     * service chain is valid
     *
     * @param request
     * @return
     */
    private ChainInfoStatusCode _isValidServiceChainInfo(HttpServletRequest request) {
        ServiceChainInfo serviceChainInfo = routerHandler.getServiceChain(request);
        if (ObjectUtil.isEmpty(serviceChainInfo)) {
            return ChainInfoStatusCode.SUCCESS;
        }

        String serviceChainHost = serviceChainInfo.getChainHost();
        int serviceChainRpcType = serviceChainInfo.getChainHostRpcType();

        request.setAttribute("svrChainHost",serviceChainHost);
        request.setAttribute("svrChainHostRpcType",serviceChainRpcType);
        return serviceChainInfo.getChainInfoStatusCode();
    }


    /**
     * api interface validate
     *
     * @param request
     * @return
     */
    private ApiResponse _validateApiInterface(HttpServletRequest request) {
        ApiResponse apiResponse = rpcHandler.rpcModules(request);
        return rpcHandler.validateApiInterface(request,apiResponse);
    }
}