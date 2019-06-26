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
package com.klaytn.enterpriseproxy.router.model;

import net.sf.json.JSONSerializer;

import java.io.Serializable;
import java.util.Date;


public class ServiceChainHost implements Serializable {
    private static final long serialVersionUID = -5174322799728306802L;

    /**
     * host id
     * @private
     */
    private long id;


    /**
     * chain info primary key
     * @private
     */
    private long serviceChainId;


    /**
     * host name
     * @private
     */
    private String host;


    /**
     * host port
     * @private
     */
    private int port;


    /**
     * host is use
     * @private
     */
    private int isUse;


    /**
     * host category
     * @private
     */
    private String category;


    /**
     * rpc type
     * @private
     */
    private int rpcType;


    /**
     * host is alive
     * @private
     */
    private int isAlive;


    /**
     * regiter date
     * @private
     */
    private Date register;


    /**
     * modify date
     * @private
     */
    private Date modify;


    /**
     * host update ip address
     * @private
     */
    private String ipAddr;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServiceChainId() {
        return serviceChainId;
    }

    public void setServiceChainId(long serviceChainId) {
        this.serviceChainId = serviceChainId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public int getRpcType() {
        return rpcType;
    }

    public void setRpcType(int rpcType) {
        this.rpcType=rpcType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(int isAlive) {
        this.isAlive = isAlive;
    }

    public Date getRegister() {
        return register;
    }

    public void setRegister(Date register) {
        this.register = register;
    }

    public Date getModify() {
        return modify;
    }

    public void setModify(Date modify) {
        this.modify = modify;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    @Override
    public String toString() {
        return JSONSerializer.toJSON(this).toString();
    }
}