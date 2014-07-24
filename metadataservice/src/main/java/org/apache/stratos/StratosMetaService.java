/*
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

package org.apache.stratos;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

public class StratosMetaService {

    private static Log log = LogFactory.getLog(StratosMetaService.class);

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{appId}")
    public void setMetaData(@PathParam("appId") String appId,ArrayList<Property> props){
        if(appId == null || appId.isEmpty()){
            System.out.println("appid empty or null, returning");
            log.info("appid empty or null, returning");
            return;
        }
        int intAppId;
        try{
         intAppId = Integer.parseInt(appId);
        }catch (NumberFormatException exception){
            System.out.println(exception);
            log.error(exception);
            return;
        }
        log.info("adding app id : " + intAppId + " with properties : ");
        for(Property property :  props){
            log.info( property.name + " -- > "+ property.value);
        }
        InMemoryDB.setData(intAppId,props);
    }

    /*@POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{appId}/{ipAddress}")
    public void setMetaData(@PathParam("appId") String appId, @PathParam("ipAddress") String ipAddress){
        if(appId == null || appId.isEmpty()){
            System.out.println("appid empty or null, returning");
            log.info("appid empty or null, returning");
            return;
        }
        if(ipAddress == null || ipAddress.isEmpty()){
            System.out.println("ipaddress empty or null, returning");
            log.info("ipaddress empty or null, returning");
            return;
        }
        int intAppId;
        try{
            intAppId = Integer.parseInt(appId);
        }catch (NumberFormatException exception){
            System.out.println(exception);
            log.error(exception);
            return;
        }
        System.out.println("adding app id : " + intAppId + " with IP : " + ipAddress);
        log.info("adding app id : " + intAppId + " with IP : " + ipAddress);
        InMemoryDB.setData(intAppId,ipAddress);
    }*/

    @GET
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{appId}")
    public List<Property> getMetaData(@PathParam("appId") int appId){
        List<Property> props = InMemoryDB.getData(appId);
        log.info("returni properties with appId : " + appId);
        for(Property property :  props){
            log.info( property.name + " -- > "+ property.value);
        }
        return props;
    }
}
