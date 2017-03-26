package com.xbw.arukas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.xbw.arukas.Config.Config;
import com.xbw.arukas.gsonContainer.Attributes;
import com.xbw.arukas.gsonContainer.Data;
import com.xbw.arukas.gsonContainer.Envs;
import com.xbw.arukas.gsonContainer.JsonContainerBean;
import com.xbw.arukas.gsonContainer.PortMappings;
import com.xbw.arukas.gsonContainer.Ports;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Handler;
public class DetailActivity extends BaseLeftActivity {

    /**
     *
     * {
     "data":{
     "id":"98fe4a3c-ad45-42ed-a671-707d0181df7d",
     "type":"containers",
     "attributes":{
     "app_id":"638f78b9-22a8-48d9-90cf-229a14702761",
     "image_name":"kinogmt/centos-ssh",
     "cmd":null,
     "is_running":true,
     "instances":1,
     "mem":512,
     "envs":[{"key":"haha","value":"1"},{"key":"hahaha","value":"2"},{"key":"eheh","value":"2"}],
     "ports":[
     {
     "number":80,
     "protocol":"tcp"
     },
     {
     "number":22,
     "protocol":"tcp"
     },
     {
     "number":8989,
     "protocol":"tcp"
     }
     ],
     "port_mappings":[
     [
     {
     "container_port":80,
     "service_port":31707,
     "host":"seaof-153-125-239-209.jp-tokyo-27.arukascloud.io"
     },
     {
     "container_port":22,
     "service_port":31708,
     "host":"seaof-153-125-239-209.jp-tokyo-27.arukascloud.io"
     },
     {
     "container_port":8989,
     "service_port":31709,
     "host":"seaof-153-125-239-209.jp-tokyo-27.arukascloud.io"
     }
     ]
     ],
     "created_at":"2017-03-13T13:41:38.960+09:00",
     "updated_at":"2017-03-20T21:26:28.899+09:00",
     "status_text":"running",
     "arukas_domain":"jovial-noether-1109",
     "end_point":"jovial-noether-1109.arukascloud.io",
     "custom_domains":null
     },
     "relationships":{
     "app":{
     "data":{
     "id":"638f78b9-22a8-48d9-90cf-229a14702761",
     "type":"apps"
     }
     }
     }
     }
     }
     */
    private String containerID;
    private Data data;
    private Attributes attributes;
    private JsonContainerBean jsonContainerBean;

    private String appId="";
    private String imageName="";
    private String cmd="";
    private int instances=1;
    private int mem=256;
    private List<Envs> envs=new ArrayList<Envs>();
    private List<Ports>ports=new ArrayList<Ports>();
    private List<List<PortMappings>>portMappings=new ArrayList<List<PortMappings>>();
    private String created_at;
    private String updated_at="";
    private String status_text="";
    private String arukas_domain;
    private String end_point="";
    private String custom_domains;


    private TextView tv_update;
    private TextView tv_id;
    private TextView tv_appname;
    private TextView tv_image;
    private TextView tv_instances;
    private TextView tv_memory;
    private TextView tv_endpoint;
    private TextView tv_port;
    private TextView tv_env;
    private TextView tv_cmd;
    private TextView tv_status;
    private Switch sw_status;

    private Button btn_ssh;
    private EditText btn_ss;
    private boolean openswitch=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getIntent().getStringExtra("name"));
        //String a[]=getIntent().getStringExtra("containerID").split(":");
        //containerID=a[1];
        containerID=getIntent().getStringExtra("containerID");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getContainer(containerID);
        setAtt();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(DetailActivity.this,UpdateActivity.class);
                intent.putExtra("appname",tv_appname.getText().toString());
                intent.putExtra("image",tv_image.getText().toString());
                intent.putExtra("instances",tv_instances.getText().toString());
                intent.putExtra("memory",tv_memory.getText().toString());
                intent.putExtra("endpoint",tv_endpoint.getText().toString());
                intent.putExtra("cmd",tv_cmd.getText().toString());
                intent.putExtra("ports",tv_port.getText().toString());
                intent.putExtra("envs",tv_env.getText().toString());
                intent.putExtra("containerID",containerID);
                startActivity(intent);
                //Intent intent=new Intent();
                //intent.setClass(DetailActivity.this,UpdateActivity.class);
                //startActivity(intent);
                //Toast.makeText(DetailActivity.this,"暂未开放",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    private void setAtt() {
        tv_update = (TextView) findViewById(R.id.tv_update);
        tv_id = (TextView) findViewById(R.id.tv_id);
        tv_appname = (TextView) findViewById(R.id.tv_appname);
        tv_image = (TextView) findViewById(R.id.tv_image);
        tv_instances = (TextView) findViewById(R.id.tv_instances);
        tv_memory = (TextView) findViewById(R.id.tv_memory);
        tv_endpoint = (TextView) findViewById(R.id.tv_endpoint);
        tv_port = (TextView) findViewById(R.id.tv_port);
        tv_env = (TextView) findViewById(R.id.tv_env);
        tv_cmd = (TextView) findViewById(R.id.tv_cmd);
        tv_status = (TextView) findViewById(R.id.tv_status);
        sw_status = (Switch) findViewById(R.id.switch1);
        btn_ssh = (Button) findViewById(R.id.Btn_ssh);
        btn_ss = (EditText) findViewById(R.id.Btn_ss);
        //监听switch状态不适用。
        /*sw_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    Toast.makeText(DetailActivity.this,"ceshi",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DetailActivity.this,"ceshi2",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        sw_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openswitch) {
                    getStop(containerID);
                } else {
                    getStart(containerID);
                }
            }
        });
        btn_ssh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tv_port.getText().toString().equals("")) {
                    String mabi[] = tv_port.getText().toString().split("\\n");
                    String aa[]={},bb[]={},cc[]={},dd[]={},ee[]={};
                    int flag=1;
                    for (int i = 0; i < mabi.length; i++) {
                        //String regex = "(?<=\\().*(?=\\))";
                        //Pattern p = Pattern.compile(regex);
                        //Matcher m = p.matcher(mabi[i]);
                        //String hh="";
                        //while (m.find()) {
                        //    hh=m.group();
                        //}
                        //Log.d(hh,"????????????");
                        //http://seaof-153-125-239-209.jp-tokyo-27.arukascloud.io:31309 (22/tcp)
                        aa = mabi[i].split(":");
                        bb = aa[1].split("-");//bb[0]=//seaof bb[1]=153 bb[2]=125 bb[3]=239
                        cc = aa[2].split("\\(");//cc[0]=31309
                        dd = cc[1].split("/");
                        ee = bb[4].split("\\.");
                        if (dd[0].equals("22")) {
                            flag=0;
                            break;
                        }
                    }
                    if(flag==1){
                        Toast.makeText(DetailActivity.this, "未找到默认22端口，请自行修改", Toast.LENGTH_SHORT).show();
                        String host = bb[1] + "." + bb[2] + "." + bb[3] + "." + ee[0] + ":" + "修改";
                        Intent intent = new Intent();
                        intent.setClass(DetailActivity.this, WebActivity.class);
                        intent.putExtra("url", Config.SSH_URL+"?host="+host+"&user=&pwd=");
                        startActivity(intent);
                    }else{
                        String host = bb[1] + "." + bb[2] + "." + bb[3] + "." + ee[0] + ":" + cc[0];
                        Intent intent = new Intent();
                        intent.setClass(DetailActivity.this, WebActivity.class);
                        if(tv_image.getText().toString().equals("xbw12138/auto-shadowsocks")){
                            Log.d("iii","uuuu");
                            intent.putExtra("url", Config.SSH_URL+"?host="+host+"&user=root&pwd=password");
                        }else{
                            Log.d("iii","uuuuiiii");
                            intent.putExtra("url", Config.SSH_URL+"?host="+host+"&user=&pwd=");
                        }
                       
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(DetailActivity.this, "该docker没有被分配ip", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getStart(String id){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API+Config.API_CONTAINERS+"/"+id+"/"+Config.API_POWER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //刷新状态
                Toast.makeText(DetailActivity.this,"两秒后刷新状态",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        getContainer(containerID);
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {}
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type","application/vnd.api+json");
                headers.put("Cookie",getSettingNote(Config.SHPF_COOKIE));
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }
    private void getStop(String id){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Config.API+Config.API_CONTAINERS+"/"+id+"/"+Config.API_POWER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //刷新状态
                getContainer(containerID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {}
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type","application/vnd.api+json");
                headers.put("Cookie",getSettingNote(Config.SHPF_COOKIE));
                return headers;
            }
        };
        mQueue.add(stringRequest);
    }
    private void getContainer(String id){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.API+Config.API_CONTAINERS+"/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson=new Gson();
                jsonContainerBean=gson.fromJson(s,JsonContainerBean.class);
                data=jsonContainerBean.getData();
                attributes=data.getAttributes();

                appId=attributes.getAppId();
                imageName=attributes.getImageName();
                cmd=attributes.getCmd();
                instances=attributes.getInstances();
                mem=attributes.getMem();
                envs=attributes.getEnvs();
                ports=attributes.getPorts();
                portMappings=attributes.getPortMappings();
                created_at=attributes.getCreatedAt();
                updated_at=attributes.getUpDatedAt();
                status_text=attributes.getStatusText();
                arukas_domain=attributes.getArukasDomain();
                end_point=attributes.getEndPoint();
                custom_domains=attributes.getCustomDomains();
                /*for(int i=0;i<ports.size();i++){
                    ports.get(i).getNumber();
                    ports.get(i).getProtocol();
                }
                for(int i=0;i<portMappings.size();i++){
                    for(int j=0;j<portMappings.get(i).size();j++){
                        portMappings.get(i).get(j).getContainerPort();
                        portMappings.get(i).get(j).getHost();
                        portMappings.get(i).get(j).getServicePort();
                    }
                }*/
                //tv_status.setText("Status: "+status_text);
                if(status_text!=null){
                    if(status_text.equals("running")){
                        sw_status.setChecked(true);
                        openswitch=true;
                        tv_status.setText("Status: "+status_text);
                    }else if(status_text.equals("stopped")){
                        sw_status.setChecked(false);
                        openswitch=false;
                        tv_status.setText("Status: "+status_text);
                    }else if(status_text.equals("booting")){
                        sw_status.setChecked(true);
                        openswitch=true;
                        tv_status.setText("Status: deploying... ("+status_text+")");
                    }else if(status_text.equals("interrupted")){
                        sw_status.setChecked(false);
                        openswitch=false;
                        tv_status.setText("Status: Failed to start ("+status_text+")");
                    }
                }

                tv_update.setText(updated_at!=null?getTime(updated_at):"");
                tv_appname.setText(getIntent().getStringExtra("name"));
                tv_id.setText(appId!=null?appId:"");
                tv_image.setText(imageName!=null?imageName:"");
                tv_instances.setText(instances+"");
                tv_memory.setText(mem+" M");
                tv_endpoint.setText(end_point!=null?end_point:"");



                //if(imageName.equals("xbw12138/auto-shadowsocks")){
                //    btn_ss.setVisibility(View.VISIBLE);
                //}

                /*String date="";
                date="ID: "+appId+"\n"
                +"Image: "+imageName+"\n"
                +"CMD: "+cmd+"\n"
                +"Instances: "+instances+"\n"
                +"Memory: "+mem+"\n"
                +"Endpoint: "+end_point+"\n"
                +"Port: \n";
                */
                //for(int i=0;i<ports.size();i++){
                //    date+="number: "+ports.get(i).getNumber()+"  protocol: "+ports.get(i).getProtocol()+"\n";
                //}
                String pportss="";
                if(portMappings!=null){
                    for(int i=0;i<portMappings.size();i++){
                        for(int j=0;j<portMappings.get(i).size();j++){
                            pportss+="http://"+portMappings.get(i).get(j).getHost()+":"+portMappings.get(i).get(j).getServicePort()+"("+portMappings.get(i).get(j).getContainerPort()+"/"+ports.get(i).getProtocol()+")\n";
                            //pportss+="container_port: "+portMappings.get(i).get(j).getContainerPort()+"\nservice_port: "+portMappings.get(i).get(j).getHost()+"\nhost: "+portMappings.get(i).get(j).getServicePort()+"\n";
                        }
                    }
                }
                tv_port.setText(pportss);
                if(envs==null){
                    tv_env.setText("0 environment variable(s) has/have been set");
                }else{
                    String eenvss="";
                    for(int i=0;i<envs.size();i++){
                        eenvss+=envs.get(i).getKey()+" = "+envs.get(i).getValue()+"\n";
                    }
                    tv_env.setText(eenvss);
                }
                if(cmd!=null){
                    if(cmd.equals("")){
                        tv_cmd.setText("Unspecified");
                    }else{
                        tv_cmd.setText(cmd);
                    }
                }else{
                    tv_cmd.setText("Unspecified");
                }
                if((imageName!=null?imageName:"").equals("xbw12138/auto-shadowsocks")){

                    //ss://aes-256-cfb:xbw12138@104.128.89.102:10000
                    if (!pportss.equals("")) {
                        String mabi[] = tv_port.getText().toString().split("\\n");
                        String aa[] = {}, bb[] = {}, cc[] = {}, dd[] = {}, ee[] = {};
                        int flag = 1;
                        for (int i = 0; i < mabi.length; i++) {
                            //http://seaof-153-125-239-209.jp-tokyo-27.arukascloud.io:31309 (22/tcp)
                            aa = mabi[i].split(":");
                            bb = aa[1].split("-");//bb[0]=//seaof bb[1]=153 bb[2]=125 bb[3]=239
                            cc = aa[2].split("\\(");//cc[0]=31309
                            dd = cc[1].split("/");
                            ee = bb[4].split("\\.");
                            if (dd[0].equals("8989")) {
                                flag = 0;
                                break;
                            }
                        }
                        String host = bb[1] + "." + bb[2] + "." + bb[3] + "." + ee[0] + ":" + cc[0];
                        btn_ss.setVisibility(View.VISIBLE);
                        btn_ss.setText("ss://aes-256-cfb:xbw12138@"+host);
                    }

                }


                /*date+="Created at: "+created_at+"\n"
                +"Updated at: "+updated_at+"\n"
                +"Status: "+status_text+"\n";
                */
                //detailTV.setText(date);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {}
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type","application/vnd.api+json");
                headers.put("Cookie",getSettingNote(Config.SHPF_COOKIE));
                return headers;
            }
        };
        mQueue.add(stringRequest);

    }
    private String getSettingNote(String s){//获取保存设置
        SharedPreferences read = getSharedPreferences(Config.SHPF_COOKIE_INFO, MODE_PRIVATE);
        return read.getString(s, "");
    }
    private String getTime(String s){
        //2017-03-17T14:25:17.446+09:00
        String a[]=s.split("\\+");
        String b[]=a[0].split("\\.");
        String c= b[0].replace("T"," ");
        return c+"(东京)";
    }
}
