package org.apache.stratos;/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.*;

public class InMemoryDB {

    private static Map<Integer,Stack<List<Property>>> appIdMap = new HashMap<Integer, Stack<List<Property>>>();

    public static void setData(int appId, List<Property> propertyList) {
        if(!appIdMap.containsKey(appId)){
            Stack<List<Property>> tempStack = new Stack<List<Property>>();
            tempStack.push(propertyList);
            appIdMap.put(appId, tempStack);
            return;
        }
        Stack<List<Property>> stack = appIdMap.get(appId);
        stack.push(propertyList);
    }

    public static List<Property> getData(int appId) {
        Stack<List<Property>> stack= appIdMap.get(appId);
        if (stack == null){
            List<Property> propertiesList = new ArrayList<Property>();
            propertiesList.add(new Property("dummyName","dummyValue"));
            return propertiesList;
        }
        return stack.peek();
    }

}
