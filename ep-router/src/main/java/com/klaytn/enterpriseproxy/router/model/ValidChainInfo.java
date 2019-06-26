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


public class ValidChainInfo implements Serializable {
    private static final long serialVersionUID = -9029814617080677287L;

    /**
     * chain info model
     * @type model
     */
    private ServiceChain chainInfo;


    /**
     * chain host info model
     * @type model
     */
    private ServiceChainHost chainHostInfo;


    /**
     * chain info status code
     * @type enum
     */
    private ChainInfoStatusCode chainInfoStatusCode;


    /**
     * error message
     * @type string
     */
    private String errorMessage;




    public ServiceChain getChainInfo() {
        return chainInfo;
    }

    public void setChainInfo(ServiceChain chainInfo) {
        this.chainInfo=chainInfo;
    }

    public ServiceChainHost getChainHostInfo() {
        return chainHostInfo;
    }

    public void setChainHostInfo(ServiceChainHost chainHostInfo) {
        this.chainHostInfo=chainHostInfo;
    }

    public ChainInfoStatusCode getChainInfoStatusCode() {
        return chainInfoStatusCode;
    }

    public void setChainInfoStatusCode(ChainInfoStatusCode chainInfoStatusCode) {
        this.chainInfoStatusCode=chainInfoStatusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return JSONSerializer.toJSON(this).toString();
    }
}