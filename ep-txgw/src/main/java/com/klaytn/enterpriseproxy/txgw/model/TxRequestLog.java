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


public class TxRequestLog implements Serializable {
    private static final long serialVersionUID=7169804141257499312L;

    /**
     * id
     * @type number
     */
    private long id;


    /**
     * request_log_id
     * @type number
     */
    private long requestLogId;


    /**
     * tx_type
     * @type string
     */
    private String txType;


    /**
     * target_host
     * @type string
     */
    private String targetHost;


    /**
     * to_address
     * @type string
     */
    private String toAddress;


    /**
     * from_address
     * @type string
     */
    private String fromAddress;


    /**
     * params
     * @type string
     */
    private String params;


    /**
     * register
     * @type date
     */
    private Date register;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id=id;
    }

    public long getRequestLogId() {
        return requestLogId;
    }

    public void setRequestLogId(long requestLogId) {
        this.requestLogId=requestLogId;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType=txType;
    }

    public String getTargetHost() {
        return targetHost;
    }

    public void setTargetHost(String targetHost) {
        this.targetHost=targetHost;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress=toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress=fromAddress;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params=params;
    }

    public Date getRegister() {
        return register;
    }

    public void setRegister(Date register) {
        this.register=register;
    }

    @Override
    public String toString() {
        return JSONSerializer.toJSON(this).toString();
    }
}