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
package com.klaytn.enterpriseproxy.api.operation.controller;

import com.klaytn.enterpriseproxy.api.common.exception.ApiException;
import com.klaytn.enterpriseproxy.api.common.model.ApiResponse;
import com.klaytn.enterpriseproxy.api.operation.service.ChainRouterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@Api("ServiceChain Info DB Register Tool API")
@RequestMapping(value="/operation/router/",method=RequestMethod.POST)
public class ChainRouterController {
    @Autowired
    private ChainRouterService service;




    
    @ApiOperation(value="ALL CHAIN INFO")
    @RequestMapping(value="allChain")
    public ApiResponse all() throws ApiException {
        return service.allChain();
    }


    @ApiOperation(value="ALL HEALTH CHECK LOG")
    @RequestMapping(value="allHealchCheckLog")
    public ApiResponse allHealthCheck() throws ApiException {
        return service.allHealthCheck();
    }

    
    @ApiOperation(value="Add ServiceChain")
    @RequestMapping(value="addServiceChain")
    public ApiResponse addServiceChain(
            HttpServletRequest request,
            @RequestParam(value="name",required=false,defaultValue="") String name,
            @RequestParam(value="owner",required=false,defaultValue="") String owner,
            @RequestParam(value="startDate",required=false,defaultValue="") String startDate,
            @RequestParam(value="endDate",required=false,defaultValue="") String endDate) throws ApiException {
        return service.addChain(request,name,owner,startDate,endDate);
    }


    @ApiOperation(value="Edit ServiceChain")
    @RequestMapping(value="editServiceChain")
    public ApiResponse editServiceChain(
            HttpServletRequest request,
            @RequestParam(value="id",required=false,defaultValue="0") long id,
            @RequestParam(value="name",required=false,defaultValue="") String name,
            @RequestParam(value="owner",required=false,defaultValue="") String owner,
            @RequestParam(value="isUse",required=false,defaultValue="0") int isUse,
            @RequestParam(value="startDate",required=false,defaultValue="") String startDate,
            @RequestParam(value="endDate",required=false,defaultValue="") String endDate) throws ApiException {
        return service.modifyChain(request,id,name,owner,isUse,startDate,endDate);
    }


    @ApiOperation(value="Delete ServiceChain")
    @RequestMapping(value="delServiceChain")
    public ApiResponse delServiceChain(
            @RequestParam(value="id",required=false,defaultValue="0") long id) throws ApiException {
        return service.delChain(id);
    }


    @ApiOperation(value="Add ServiceChainHost")
    @RequestMapping(value="addServiceChainHost")
    public ApiResponse addServiceChainHost(
            HttpServletRequest request,
            @RequestParam(value="serviceChainId",required=false,defaultValue="0") long serviceChainId,
            @RequestParam(value="category",required=false,defaultValue="") String category,
            @RequestParam(value="host",required=false,defaultValue="") String host,
            @RequestParam(value="port",required=false,defaultValue="0") int port) throws ApiException {
        return service.addChainHost(request,serviceChainId,category,host,port);
    }


    @ApiOperation(value="Edit ServiceChainHost")
    @RequestMapping(value="editServiceChainHost")
    public ApiResponse editServiceChainHost(
            HttpServletRequest request,
            @RequestParam(value="id",required=false,defaultValue="0") long id,
            @RequestParam(value="serviceChainId",required=false,defaultValue="0") long serviceChainId,
            @RequestParam(value="host",required=false,defaultValue="") String host,
            @RequestParam(value="port",required=false,defaultValue="0") int port,
            @RequestParam(value="isUse",required=false,defaultValue="0") int isUse,
            @RequestParam(value="category",required=false,defaultValue="") String category,
            @RequestParam(value="rpcType",required=false,defaultValue="0") int rpcType,
            @RequestParam(value="isAlive",required=false,defaultValue="0") int isAlive) throws ApiException {
        return service.modifyChainHost(request,id,serviceChainId,host,port,isUse,category,rpcType,isAlive);
    }


    @ApiOperation(value="Delete ServiceChainHost")
    @RequestMapping(value="delServiceChainHost")
    public ApiResponse delServiceChainHost(
            @RequestParam(value="id",required=false,defaultValue="0") long id) throws ApiException {
        return service.delChainHost(id);
    }
}
