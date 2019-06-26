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


public class ServiceChainHostHealthCheck implements Serializable {
    private static final long serialVersionUID = -8733532213774887460L;


    /**
     * log id
     * @private
     */
    private long id;


    /**
     * chain host primary key
     * @private
     */
    private long chainHostId;


    /**
     * check type
     * @private
     */
    private int checkType;


    /**
     * check comment
     * @private
     */
    private String comment;


    /**
     * check date
     * @private
     */
    private Date checkDate;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChainHostId() {
        return chainHostId;
    }

    public void setChainHostId(long chainHostId) {
        this.chainHostId = chainHostId;
    }

    public int getCheckType() {
        return checkType;
    }

    public void setCheckType(int checkType) {
        this.checkType = checkType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    @Override
    public String toString() {
        return JSONSerializer.toJSON(this).toString();
    }
}