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
package com.klaytn.enterpriseproxy.router.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import java.lang.reflect.Method;


public class RouterAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AsyncUncaughtExceptionHandler.class);



    
    @Override
    public void handleUncaughtException(Throwable throwable,Method method,Object... obj) {
        logger.error("@@@@ EXCEPTION CAUSE : " + throwable.getMessage() + " @@@@");
        logger.error("@@@@ METHOD NAME : " + method.getName() + " @@@@");

        for (Object param : obj) {
            logger.error("@@@@ PARAMETER VALUE : " + param + " @@@@");
        }
    }
}
