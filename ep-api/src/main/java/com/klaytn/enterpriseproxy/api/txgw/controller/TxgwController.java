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
package com.klaytn.enterpriseproxy.api.txgw.controller;

import com.klaytn.enterpriseproxy.api.txgw.service.TxgatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.klaytn.enterpriseproxy.api.common.exception.ApiException;
import com.klaytn.enterpriseproxy.api.common.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api("TXGW Tool API")
@RequestMapping(value="/txgw/",method=RequestMethod.POST)
public class TxgwController {
    @Autowired
    private TxgatewayService service;




    @ApiOperation(value="ALL TXGW LOG LIST")
    @RequestMapping(value="all")
    public ApiResponse all() throws ApiException {
        return service.all();
    }
}