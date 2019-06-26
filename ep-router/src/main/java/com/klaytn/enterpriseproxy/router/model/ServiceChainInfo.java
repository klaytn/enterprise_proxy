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


public class ServiceChainInfo implements Serializable {
    private static final long serialVersionUID = 6998394922874208434L;

    /**
     * service chain host
     * @type string
     */
    private String chainHost;


    /**
     * service chain host rpc type
     * @type number
     */
    private int chainHostRpcType;


    /**
     * chain info status code
     * @type enum
     */
    private ChainInfoStatusCode chainInfoStatusCode;




    public String getChainHost() {
        return chainHost;
    }

    public void setChainHost(String chainHost) {
        this.chainHost = chainHost;
    }

    public int getChainHostRpcType() {
        return chainHostRpcType;
    }

    public void setChainHostRpcType(int chainHostRpcType) {
        this.chainHostRpcType = chainHostRpcType;
    }

    public ChainInfoStatusCode getChainInfoStatusCode() {
        return chainInfoStatusCode;
    }

    public void setChainInfoStatusCode(ChainInfoStatusCode chainInfoStatusCode) {
        this.chainInfoStatusCode = chainInfoStatusCode;
    }

    @Override
    public String toString() {
        return JSONSerializer.toJSON(this).toString();
    }
}