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
package com.klaytn.enterpriseproxy.txgw.util;

import java.util.Enumeration;


public class TxgwUtils {
    public static final String TXGW_QUEUE_KEY = "EPREQUESTKEY";


    /**
     * get next element
     *
     * @param enumeration
     * @return
     */
    public static String getNextElement(Enumeration<String> enumeration) {
        String returnKey = null;

        try {
            returnKey = enumeration.nextElement();
        } catch (Exception ignored) {}

        return returnKey;
    }


    private TxgwUtils() {
        throw new IllegalStateException("Utility Class");
    }
}