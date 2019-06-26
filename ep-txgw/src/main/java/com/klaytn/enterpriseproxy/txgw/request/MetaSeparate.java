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
package com.klaytn.enterpriseproxy.txgw.request;

import com.klaytn.enterpriseproxy.txgw.model.RequestLog;
import com.klaytn.enterpriseproxy.txgw.util.TxgwUtils;
import com.klaytn.enterpriseproxy.utils.ArrayUtil;
import com.klaytn.enterpriseproxy.utils.ObjectUtil;
import com.klaytn.enterpriseproxy.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


@Component
public class MetaSeparate {
    private static final Logger logger = LoggerFactory.getLogger(MetaSeparate.class);

    /**
     * add header name list
     * @type list
     */
    private static final String[] addHeaderNameList = {"host","user-agent","accept","accept-language","accept-encoding","referer","origin","connection","content-length"};




    /**
     * request sepatate execution
     *
     * @param request
     * @return
     */
    public RequestLog execution(HttpServletRequest request) {
        if (ObjectUtil.isEmpty(request) || StringUtil.isEmpty(request.getMethod())) {
            return null;
        }

        RequestLog requestLog = new RequestLog();
        requestLog.setMethod(request.getMethod());
        requestLog.setQueryString(request.getQueryString());
        requestLog.setRequestUri(request.getRequestURI());
        requestLog.setServletPath(request.getServletPath());
        requestLog.setPathInfo(request.getPathInfo());
        requestLog.setCharacterEncoding(request.getCharacterEncoding());
        requestLog.setContentLength(request.getContentLength());
        requestLog.setContentType(request.getContentType());
        requestLog.setLocalAddr(request.getLocalAddr());
        requestLog.setLocalName(request.getLocalName());
        requestLog.setLocalPort(request.getLocalPort());
        requestLog.setRemoteAddr(request.getRemoteAddr());
        requestLog.setRemoteHost(request.getRemoteHost());
        requestLog.setRemotePort(request.getRemotePort());
        requestLog.setScheme(request.getScheme());
        requestLog.setServerName(request.getServerName());
        requestLog.setServerPort(request.getServerPort());

        if (ObjectUtil.isNotEmpty(request.getLocale())) {
            requestLog.setLocale(request.getLocale().getCountry());
        }

        if (ObjectUtil.isEmpty(request.getHeaderNames())) {
            return requestLog;
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = TxgwUtils.getNextElement(headerNames);
            if (StringUtil.isEmpty(headerName)) {
                continue;
            }

            String headerNameKey = StringUtil.toLowerCase(headerName);
            if (!ArrayUtil.contains(addHeaderNameList,headerNameKey)) {
                continue;
            }

            String headerValue = request.getHeader(headerName);
            logger.info("@@@@ HEADER VALUE [" + headerName + "] : " + headerValue + " @@@@");

            switch (headerNameKey) {
                default:
                case "host"                 : requestLog.setHeaderHost(headerValue); break;
                case "user-agent"           : requestLog.setHeaderUserAgent(headerValue); break;
                case "accept"               : requestLog.setHeaderAccept(headerValue); break;
                case "accept-language"      : requestLog.setHeaderAcceptLanguage(headerValue); break;
                case "accept-encoding"      : requestLog.setHeaderAcceptEncoding(headerValue); break;
                case "referer"              : requestLog.setHeaderReferer(headerValue); break;
                case "origin"               : requestLog.setHeaderOrigin(headerValue); break;
                case "connection"           : requestLog.setHeaderConnection(headerValue); break;
                case "content-length"       : requestLog.setHeaderContentLength(Integer.parseInt(headerValue)); break;
            }
        }

        return requestLog;
    }
}