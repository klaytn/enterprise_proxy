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


public class ServiceChain implements Serializable {
    private static final long serialVersionUID = 4849832671662108050L;

    /**
     * ChainInfo ID
     * @private
     */
    private long id;


    /**
     * chain name
     * @private
     */
    private String name;


    /**
     * chain owner
     * @private
     */
    private String owner;


    /**
     * chain is use
     * @private
     */
    private int isUse;


    /**
     * chain service start date
     * @private
     */
    private Date startDate;


    /**
     * chain service end date
     * @private
     */
    private Date endDate;


    /**
     * chain info register date
     * @private
     */
    private Date register;


    /**
     * chain info modify date
     * @private
     */
    private Date modify;


    /**
     * chain info update ip address
     * @private
     */
    private String ipAddr;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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