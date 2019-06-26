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

import com.klaytn.enterpriseproxy.txgw.model.RequestLog;
import com.klaytn.enterpriseproxy.txgw.model.RequestParamsLog;
import com.klaytn.enterpriseproxy.txgw.model.TxGatewayLog;
import com.klaytn.enterpriseproxy.txgw.request.MetaSeparate;
import com.klaytn.enterpriseproxy.txgw.request.ParameterSeparate;
import com.klaytn.enterpriseproxy.txgw.util.TxgwUtils;
import com.klaytn.enterpriseproxy.queue.local.Local;
import com.klaytn.enterpriseproxy.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;


@Component
public class TxgwService {
    /**
     * local queue
     * @type class
     */
    private Local<TxGatewayLog> localQueue;

    @Autowired
    private MetaSeparate metaSeparate;

    @Autowired
    private ParameterSeparate parameterSeparate;



    
    /**
     * do not editable
     *  - ep local queue key
     */
    @PostConstruct
    public void init() {
        localQueue = Local.build(TxgwUtils.TXGW_QUEUE_KEY);
    }


    /**
     * http request add
     * - async
     *
     * @param request
     */
    @Async("txGatewayAsyncExecutor")
    public void execution(HttpServletRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            return;
        }

        // TxGatewayLog Model set
        TxGatewayLog txGatewayLog = new TxGatewayLog();


        // request separate
        RequestLog requestLog = metaSeparate.execution(request);
        if (ObjectUtil.isNotEmpty(requestLog)) {
            txGatewayLog.setMethod(requestLog.getMethod());
            txGatewayLog.setQueryString(requestLog.getQueryString());
            txGatewayLog.setRequestUri(requestLog.getRequestUri());
            txGatewayLog.setServletPath(requestLog.getServletPath());
            txGatewayLog.setPathInfo(requestLog.getPathInfo());
            txGatewayLog.setHeaderHost(requestLog.getHeaderHost());
            txGatewayLog.setHeaderUserAgent(requestLog.getHeaderUserAgent());
            txGatewayLog.setHeaderAccept(requestLog.getHeaderAccept());
            txGatewayLog.setHeaderAcceptLanguage(requestLog.getHeaderAcceptLanguage());
            txGatewayLog.setHeaderAcceptEncoding(requestLog.getHeaderAcceptEncoding());
            txGatewayLog.setHeaderReferer(requestLog.getHeaderReferer());
            txGatewayLog.setHeaderRpcCaller(requestLog.getHeaderRpcCaller());
            txGatewayLog.setHeaderOrigin(requestLog.getHeaderOrigin());
            txGatewayLog.setHeaderConnection(requestLog.getHeaderConnection());
            txGatewayLog.setHeaderContentLength(requestLog.getHeaderContentLength());
            txGatewayLog.setCharacterEncoding(requestLog.getCharacterEncoding());
            txGatewayLog.setContentLength(requestLog.getContentLength());
            txGatewayLog.setContentType(requestLog.getContentType());
            txGatewayLog.setLocalAddr(requestLog.getLocalAddr());
            txGatewayLog.setLocale(requestLog.getLocale());
            txGatewayLog.setLocalName(requestLog.getLocalName());
            txGatewayLog.setLocalPort(requestLog.getLocalPort());
            txGatewayLog.setProtocol(requestLog.getProtocol());
            txGatewayLog.setRemoteAddr(requestLog.getRemoteAddr());
            txGatewayLog.setRemoteHost(requestLog.getRemoteHost());
            txGatewayLog.setScheme(requestLog.getScheme());
            txGatewayLog.setServerName(requestLog.getServerName());
            txGatewayLog.setServerPort(requestLog.getServerPort());
        }

        
        // request params separate
        RequestParamsLog requestParamsLog = parameterSeparate.execution(request);
        if (ObjectUtil.isNotEmpty(requestParamsLog)) {
            txGatewayLog.setRequestParams(requestParamsLog.getParams());
        }

        localQueue.add(txGatewayLog);
    }
}