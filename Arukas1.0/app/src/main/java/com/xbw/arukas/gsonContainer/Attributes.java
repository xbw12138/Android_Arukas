/**
  * Copyright 2017 bejson.com 
  */
package com.xbw.arukas.gsonContainer;
import java.util.List;
/**
 * Auto-generated: 2017-03-20 20:37:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Attributes {

    private String app_id;
    private String image_name;
    private String cmd;
    private boolean is_running;
    private int instances;
    private int mem;
    private List<Envs> envs;
    private List<Ports> ports;
    private List<List<PortMappings>> port_mappings;
    private String created_at;
    private String updated_at;
    private String status_text;
    private String arukas_domain;
    private String end_point;
    private String custom_domains;
    public void setAppId(String appId) {
         this.app_id = appId;
     }
     public String getAppId() {
         return app_id;
     }

    public void setImageName(String imageName) {
         this.image_name = imageName;
     }
     public String getImageName() {
         return image_name;
     }

    public void setCmd(String cmd) {
         this.cmd = cmd;
     }
     public String getCmd() {
         return cmd;
     }

    public void setIsRunning(boolean isRunning) {
         this.is_running = isRunning;
     }
     public boolean getIsRunning() {
         return is_running;
     }

    public void setInstances(int instances) {
         this.instances = instances;
     }
     public int getInstances() {
         return instances;
     }

    public void setMem(int mem) {
         this.mem = mem;
     }
     public int getMem() {
         return mem;
     }

    public void setEnvs(List<Envs> envs) {
        this.envs = envs;
    }
    public List<Envs> getEnvs() {
        return envs;
    }


    public void setPorts(List<Ports> ports) {
         this.ports = ports;
     }
     public List<Ports> getPorts() {
         return ports;
     }

    public void setPortMappings(List<List<PortMappings>> portMappings) {
         this.port_mappings = portMappings;
     }
     public List<List<PortMappings>> getPortMappings() {
         return port_mappings;
     }

    public void setCreatedAt(String createdAt) {
         this.created_at = createdAt;
     }
     public String getCreatedAt() {
         return created_at;
     }

    public void setUpDatedAt(String upDatedAt) {
         this.updated_at = upDatedAt;
     }
     public String getUpDatedAt() {
         return updated_at;
     }

    public void setStatusText(String statusText) {
         this.status_text = statusText;
     }
     public String getStatusText() {
         return status_text;
     }

    public void setArukasDomain(String arukasDomain) {
         this.arukas_domain = arukasDomain;
     }
     public String getArukasDomain() {
         return arukas_domain;
     }

    public void setEndPoint(String endPoint) {
         this.end_point = endPoint;
     }
     public String getEndPoint() {
         return end_point;
     }

    public void setCustomDomains(String customDomains) {
         this.custom_domains = customDomains;
     }
     public String getCustomDomains() {
         return custom_domains;
     }

}