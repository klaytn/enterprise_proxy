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
import java.util.List;


public class ChainInfoBasic implements Serializable {
    private static final long serialVersionUID = 6295502933778469729L;


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
     * chain type
     * @private
     */
    private int type;


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
     * ChainHostInfo
     * @type List
     */
    private List<ServiceChainHost> chainHostInfoList;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner=owner;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type=type;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse=isUse;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate=startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate=endDate;
    }

    public List<ServiceChainHost> getChainHostInfoList() {
        return chainHostInfoList;
    }

    public void setChainHostInfoList(List<ServiceChainHost> chainHostInfoList) {
        this.chainHostInfoList = chainHostInfoList;
    }

    @Override
    public String toString() {
        return JSONSerializer.toJSON(this).toString();
    }
}