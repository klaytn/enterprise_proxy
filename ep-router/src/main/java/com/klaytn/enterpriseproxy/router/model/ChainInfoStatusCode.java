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


public enum ChainInfoStatusCode {
    UNKNOWN(999,"UNKNOWN"),
    PARAMETER_INVALID(998,"CHAIN HOST PARAMETER INVALID"),
    CHAIN_EXPIRE(997,"SERVICE CHAIN USE EXPIRE"),
    CHAIN_IS_NOT_EXIST(996,"SERVICE CHAIN IS NOT EXIST"),
    CHAIN_IS_NOT_USE(995,"SERVICE CHAIN IS NOT USE"),

    CHAIN_HOST_IS_NOT_USE(899,"SERVICE CHAIN HOST IS NOT USE"),
    CHAIN_HOST_IS_NOT_ALIVE(898,"SERVICE CHAIN HOST IS NOT ALIVE"),
    CHAIN_HOST_IS_NOT_EXIST(897,"SERVICE CHAIN HOST IS NOT EXIST"),

    CHAIN_INSERT_FAIL(799,"SERVICE CHAIN INSERT FAIL"),
    CHAIN_HOST_INSERT_FAIL(798,"SERVICE CHAIN HOST INSERT FAIL"),
    CHAIN_UPDATE_FAIL(797,"SERVICE CHAIN UPDATE FAIL"),
    CHAIN_HOST_UPDATE_FAIL(796,"SERVICE CHAIN HOST UPDATE FAIL"),
    CHAIN_DELETE_FAIL(795,"SERVICE CHAIN DELETE FAIL"),
    CHAIN_HOST_DELETE_FAIL(794,"SERVICE CHAIN HOST DELETE FAIL"),

    SUCCESS(0,"SUCCESS");



    /**
     * RESPONSE CODE
     * @type number
     */
    private int code;


    /**
     * RESPONSE MESSAGE
     * @type string
     */
    private String message;




    ChainInfoStatusCode(int code,String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}