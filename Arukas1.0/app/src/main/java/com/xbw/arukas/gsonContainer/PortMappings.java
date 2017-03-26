/**
  * Copyright 2017 bejson.com 
  */
package com.xbw.arukas.gsonContainer;
/**
 * Auto-generated: 2017-03-20 20:37:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PortMappings {

    private int container_port;
    private int service_port;
    private String host;
    public void setContainerPort(int containerPort) {
         this.container_port = containerPort;
     }
     public int getContainerPort() {
         return container_port;
     }

    public void setServicePort(int servicePort) {
         this.service_port = servicePort;
     }
     public int getServicePort() {
         return service_port;
     }

    public void setHost(String host) {
         this.host = host;
     }
     public String getHost() {
         return host;
     }

}