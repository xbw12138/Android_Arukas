package com.xbw.arukas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.xbw.arukas.Adapter.MainAdapter;
import com.xbw.arukas.Config.Config;
import com.xbw.arukas.Model.MainModel;
import com.xbw.arukas.gsonAPP.JsonAppBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.xbw.arukas.gsonAPP.Data;
import com.xbw.arukas.gsonAPP.Attributes;
public class MainActivity extends AppCompatActivity {
    private JsonAppBean jsonAppBean;
    private Data data;
    private Attributes attributes;
    private ListView listviewmain;
    private MainAdapter mainAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApps();
        listviewmain=(ListView)findViewById(R.id.listviewmain);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getApps();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabcreate);
        fab.setColorFilter(getResources().getColor(R.color.white));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,CreateActivity.class);
                startActivity(intent);
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
    private void getApps(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.API+Config.API_APPS, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                swipeRefreshLayout.setRefreshing(false);
                Gson gson=new Gson();
                jsonAppBean=gson.fromJson(s,JsonAppBean.class);

                List<MainModel> mDate=new ArrayList<MainModel>();

                for(int i=0;i<jsonAppBean.getData().size();i++){
                    MainModel mainModel=new MainModel();
                    data=jsonAppBean.getData().get(i);
                    attributes=data.getAttributes();
                    mainModel.setAppname(attributes.getName());
                    mainModel.setAppid(attributes.getImageId());
                    mainModel.setApptime(getTime(attributes.getCreatedAt()));
                    mainModel.setAppdelid(data.getId());
                    mDate.add(mainModel);
                    //addItem(attributes.getName(), new String[]{"CID:"+attributes.getImageId(), "Created At:"+getTime(attributes.getCreatedAt()),data.getId()}, getColors(i%3), R.drawable.ic_ghost);
                }
                mainAdapter=new MainAdapter(MainActivity.this,mDate);
                listviewmain.setAdapter(mainAdapter);
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
        return b[0].replace("T"," ");
    }
    private int getColors(int i){
        int a[]={R.color.blue,R.color.green,R.color.pink};
        return a[i];
    }
    /*private void deleteApps(String id, final ExpandingItem item){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Config.API+Config.API_APPS+"/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //
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
    }*/
}
