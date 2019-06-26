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
package com.klaytn.enterpriseproxy.txgw.model;

import net.sf.json.JSONSerializer;
import java.io.Serializable;
import java.util.Date;


public class TxGatewayLog implements Serializable {
    private static final long serialVersionUID = -7059973799610824071L;

    
    /**
     * request log id
     * @type number
     */
    private long id;


    /**
     * method
     * @type string
     */
    private String method;


    /**
     * query_string
     * @type string
     */
    private String queryString;


    /**
     * request_uri
     * @type string
     */
    private String requestUri;



    /**
     * servlet_path
     * @type string
     */
    private String servletPath;


    /**
     * path info
     * @type string
     */
    private String pathInfo;


    /**
     * header_host
     * @type string
     */
    private String headerHost;


    /**
     * header_user_agent
     * @type string
     */
    private String headerUserAgent;


    /**
     * header_accept
     * @type string
     */
    private String headerAccept;


    /**
     * header_accept_language
     * @type string
     */
    private String headerAcceptLanguage;


    /**
     * header_accept_encoding
     * @type string
     */
    private String headerAcceptEncoding;


    /**
     * header_referer
     * @type string
     */
    private String headerReferer;


    /**
     * header_rpc_caller
     * @type string
     */
    private String headerRpcCaller;


    /**
     * header_origin
     * @type string
     */
    private String headerOrigin;


    /**
     * header_connection
     * @type string
     */
    private String headerConnection;


    /**
     * header_content_length
     * @type number
     */
    private int headerContentLength;


    /**
     * character_encoding
     * @type string
     */
    private String characterEncoding;


    /**
     * content_length
     * @type number
     */
    private int contentLength;


    /**
     * content_type
     * @type string
     */
    private String contentType;


    /**
     * local_addr
     * @type string
     */
    private String localAddr;


    /**
     * locale
     * @type string
     */
    private String locale;


    /**
     * local_name
     * @type string
     */
    private String localName;


    /**
     * local_port
     * @type number
     */
    private int localPort;


    /**
     * protocol
     * @type string
     */
    private String protocol;


    /**
     * remote_addr
     * @type string
     */
    private String remoteAddr;


    /**
     * remote_host
     * @type string
     */
    private String remoteHost;


    /**
     * remote_port
     * @type number
     */
    private int remotePort;


    /**
     * scheme
     * @type string
     */
    private String scheme;


    /**
     * server_name
     * @type string
     */
    private String serverName;


    /**
     * server_port
     * @type number
     */
    private int serverPort;


    /**
     * http request params
     * @type string
     */
    private String requestParams;

    
    /**
     * register
     * @type date
     */
    private Date register;



    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public String getHeaderHost() {
        return headerHost;
    }

    public void setHeaderHost(String headerHost) {
        this.headerHost = headerHost;
    }

    public String getHeaderUserAgent() {
        return headerUserAgent;
    }

    public void setHeaderUserAgent(String headerUserAgent) {
        this.headerUserAgent = headerUserAgent;
    }

    public String getHeaderAccept() {
        return headerAccept;
    }

    public void setHeaderAccept(String headerAccept) {
        this.headerAccept = headerAccept;
    }

    public String getHeaderAcceptLanguage() {
        return headerAcceptLanguage;
    }

    public void setHeaderAcceptLanguage(String headerAcceptLanguage) {
        this.headerAcceptLanguage = headerAcceptLanguage;
    }

    public String getHeaderAcceptEncoding() {
        return headerAcceptEncoding;
    }

    public void setHeaderAcceptEncoding(String headerAcceptEncoding) {
        this.headerAcceptEncoding = headerAcceptEncoding;
    }

    public String getHeaderReferer() {
        return headerReferer;
    }

    public void setHeaderReferer(String headerReferer) {
        this.headerReferer = headerReferer;
    }

    public String getHeaderRpcCaller() {
        return headerRpcCaller;
    }

    public void setHeaderRpcCaller(String headerRpcCaller) {
        this.headerRpcCaller = headerRpcCaller;
    }

    public String getHeaderOrigin() {
        return headerOrigin;
    }

    public void setHeaderOrigin(String headerOrigin) {
        this.headerOrigin = headerOrigin;
    }

    public String getHeaderConnection() {
        return headerConnection;
    }

    public void setHeaderConnection(String headerConnection) {
        this.headerConnection = headerConnection;
    }

    public int getHeaderContentLength() {
        return headerContentLength;
    }

    public void setHeaderContentLength(int headerContentLength) {
        this.headerContentLength = headerContentLength;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public Date getRegister() {
        return register;
    }

    public void setRegister(Date register) {
        this.register = register;
    }

    @Override
    public String toString() {
        return JSONSerializer.toJSON(this).toString();
    }
}
