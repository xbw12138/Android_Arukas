package com.xbw.arukas;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;
import com.xbw.arukas.Adapter.EnvAdapter;
import com.xbw.arukas.Adapter.PortsAdapter;
import com.xbw.arukas.Config.Config;
import com.xbw.arukas.Utils.MyListView;
import com.xbw.arukas.Utils.Progress_Dialog;
import com.xbw.arukas.gsonContainer.Envs;
import com.xbw.arukas.gsonContainer.Ports;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateActivity extends BaseLeftActivity {


    private String containerID;

    private EditText Et_appname;
    private EditText Et_image;
    private SeekBar Sb_instances;
    private TextView textViewsb;
    private RadioGroup Rg_mem;
    private RadioButton Rb_amem;
    private RadioButton Rb_bmem;
    private EditText Et_endpoint;
    private MyListView Lv_port;
    private MyListView Lv_env;
    private EditText Et_cmd;
    private Button Bt_create;


    private ImageView Img_padd;
    private ImageView Img_eadd;

    private PortsAdapter PortAdapter;
    private EnvAdapter EnvsAdapter;

    private LinearLayout linearport;
    private LinearLayout linearenv;

    private List<Ports> listport;
    private List<Envs> listenv;



    //
    ProgressDialog dialog;

    static class ViewHolderPort
    {
        private static EditText Et_port;
        private static RadioGroup Rg_port;
        private static RadioButton Rb_ccon;
        private static RadioButton Rb_dcon;
        private static ImageView Img_padd;
        private static ImageView Img_psub;
    }
    static class ViewHolderEnv
    {
        private static EditText Et_key;
        private static EditText Et_value;
        private static ImageView Img_eadd;
        private static ImageView Img_esub;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Edit Application");
        initView();

        initEvent();
        getPutExtra();
        //SetApp();
        //setAPPJson();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Et_appname.getText().toString().equals("")){
                    Toast.makeText(UpdateActivity.this,"应用名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(Et_image.getText().toString().equals("")){
                    Toast.makeText(UpdateActivity.this,"镜像不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //String data="";
                    int memory=256;
                    if(Rb_amem.isChecked()){
                        memory=256;
                    }else{
                        memory=512;
                    }
                    dialog = Progress_Dialog.CreateProgressDialog(UpdateActivity.this);
                    dialog.show();
                    setAPPJson(containerID,Et_image.getText().toString(),Integer.valueOf(textViewsb.getText().toString()).intValue(),memory,Et_cmd.getText().toString(),EnvsAdapter.getEnvsAdapter(),PortAdapter.getPortsAdapter(),"",Et_appname.getText().toString());
                }

            }
        });
    }
    private void getPutExtra(){
        containerID=getIntent().getStringExtra("containerID");
        Et_appname.setText(getIntent().getStringExtra("appname"));
        Et_image.setText(getIntent().getStringExtra("image"));
        String gg[]=getIntent().getStringExtra("endpoint").split("\\.");
        Et_endpoint.setText(gg[0]);
        if(!getIntent().getStringExtra("cmd").equals("Unspecified")){
            Et_cmd.setText(getIntent().getStringExtra("cmd"));
        }
        Sb_instances.setProgress(Integer.valueOf(getIntent().getStringExtra("instances")).intValue()-1);
        textViewsb.setText(getIntent().getStringExtra("instances"));
        if(getIntent().getStringExtra("memory").equals("256 M")){
            Rb_amem.setChecked(true);
        }else{
            Rb_bmem.setChecked(true);
        }
        String ports=getIntent().getStringExtra("ports");
        String envs=getIntent().getStringExtra("envs");
        if(!ports.equals("")){
            String mabi[]=ports.split("\\n");
            for(int i=0;i<mabi.length;i++){
                String regex = "(?<=\\().*(?=\\))";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(mabi[i]);
                String hh="";
                while (m.find()) {
                    hh=m.group();
                }
                Log.d(hh,"????????????");
                String aa[]=hh.split("/");
                Ports po=new Ports();
                po.setNumber(Integer.valueOf(aa[0]).intValue());
                po.setProtocol(aa[1]);
                listport.add(po);
            }
        }
        if(!envs.equals("")&&!envs.equals("0 environment variable(s) has/have been set")){
            String cao[]=envs.split("\\n");
            for(int i=0;i<cao.length;i++){
                String hh[]=cao[i].split("=");
                Envs e=new Envs();
                e.setValue(hh[1]);//修改错误
                e.setKey(hh[0]);
                listenv.add(e);
            }
        }
        PortAdapter = new PortsAdapter(UpdateActivity.this,listport);
        EnvsAdapter = new EnvAdapter(UpdateActivity.this,listenv);
        Lv_port.setAdapter(PortAdapter);
        Lv_env.setAdapter(EnvsAdapter);

    }
    private void initView(){
        Et_appname=(EditText)findViewById(R.id.ET_appname);
        Et_image=(EditText)findViewById(R.id.ET_image);
        Et_endpoint=(EditText)findViewById(R.id.ET_endpoint);
        Et_cmd=(EditText)findViewById(R.id.ET_cmd);
        Sb_instances=(SeekBar)findViewById(R.id.SB_instances);
        textViewsb=(TextView)findViewById(R.id.textViewsb);
        Rg_mem=(RadioGroup)findViewById(R.id.mRadioGroup_mem);
        Rb_amem=(RadioButton)findViewById(R.id.RadioA_mem);
        Rb_bmem=(RadioButton)findViewById(R.id.RadioB_mem);
        Lv_port=(MyListView)findViewById(R.id.listview_port);
        Lv_env=(MyListView)findViewById(R.id.listview_env);
        Bt_create=(Button)findViewById(R.id.Btn_create);

        Img_padd=(ImageView)findViewById(R.id.Img_Padd);
        Img_eadd=(ImageView)findViewById(R.id.Img_Eadd);

        linearport=(LinearLayout)findViewById(R.id.linearport);
        linearenv=(LinearLayout)findViewById(R.id.linearenv);

        listport=new ArrayList<Ports>();
        listenv=new ArrayList<Envs>();
    }
    private void initEvent(){
        Img_padd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog builder = new AlertDialog.Builder(UpdateActivity.this)
                        .create();
                builder.show();
                builder.getWindow().setContentView(R.layout.dialogports_item);//设置弹出框加载的布局
                builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                builder.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                final EditText et=(EditText)builder.findViewById(R.id.ET_port);
                final RadioButton RB_a=(RadioButton)builder.findViewById(R.id.RadioC_Con);
                final RadioButton RB_b=(RadioButton)builder.findViewById(R.id.RadioD_Con);
                builder.getWindow()
                        .findViewById(R.id.Img_Padd)
                        .setOnClickListener(new View.OnClickListener() {  //按钮点击事件
                            @Override
                            public void onClick(View v) {
                                Ports p =new Ports();
                                if(isNumeric(et.getText().toString())){
                                    p.setNumber(Integer.valueOf(!et.getText().toString().equals("")?et.getText().toString():"22").intValue());
                                    if(RB_a.isChecked()){
                                        p.setProtocol("tcp");
                                    }else{
                                        p.setProtocol("udp");
                                    }
                                    PortAdapter.addItem(p);
                                    builder.dismiss();
                                }else{
                                    Toast.makeText(UpdateActivity.this,"端口只能为数字",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                //LayoutInflater flater = LayoutInflater.from(CreateActivity.this);
                //View views = flater.inflate(R.layout.ports_item, null);
                //linearport.addView(views);
            }
        });
        Img_eadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog builder = new AlertDialog.Builder(UpdateActivity.this)
                        .create();
                builder.show();
                builder.getWindow().setContentView(R.layout.dialogenv_item);//设置弹出框加载的布局
                builder.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                builder.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                final EditText et=(EditText)builder.findViewById(R.id.ET_key);
                final EditText et2=(EditText)builder.findViewById(R.id.ET_value);
                builder.getWindow()
                        .findViewById(R.id.Img_Eadd)
                        .setOnClickListener(new View.OnClickListener() {  //按钮点击事件
                            @Override
                            public void onClick(View v) {
                                Envs p =new Envs();
                                p.setKey(et.getText().toString());
                                p.setValue(et2.getText().toString());
                                EnvsAdapter.addItem(p);
                                builder.dismiss();
                            }
                        });
                //EnvsAdapter.addItem();
                //LayoutInflater flater = LayoutInflater.from(CreateActivity.this);
                //View views = flater.inflate(R.layout.env_item, null);
                //Envs envs=new Envs();
                //envs.setKey("caonima");
                //envs.setValue("nimabi");
                //views.setTag(1,envs);
                //views.getTag(1);
                //linearenv.addView(views);
            }
        });
        //seekbar设置最大值为9，旁边显示+1。根据textview记录数值
        Sb_instances.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i==0){
                    textViewsb.setText(1+"");
                }else{
                    textViewsb.setText((i+1)+"");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Bt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Et_appname.getText().toString().equals("")){
                    Toast.makeText(UpdateActivity.this,"应用名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else if(Et_image.getText().toString().equals("")){
                    Toast.makeText(UpdateActivity.this,"镜像不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //String data="";
                    int memory=256;
                    if(Rb_amem.isChecked()){
                        memory=256;
                    }else{
                        memory=512;
                    }
                    //data+=Et_appname.getText().toString()+Et_image.getText().toString()+textViewsb.getText().toString()+memory+"";
                    //List <Ports>listp=new ArrayList<Ports>();
                    //List <Envs>liste=new ArrayList<Envs>();
                    //listp=PortAdapter.getPortsAdapter();
                    //liste=EnvsAdapter.getEnvsAdapter();
                    //for(int i=0;i<listp.size();i++){
                    //    data+=listp.get(i).getNumber()+"";
                    //    data+=listp.get(i).getProtocol();
                    //}
                    //for(int i=0;i<liste.size();i++){
                    //    data+=liste.get(i).getKey();
                    //    data+=liste.get(i).getValue();
                    //}

                    //data+=Et_cmd.getText().toString();
                    //Log.d("$$$",data);
                    dialog = Progress_Dialog.CreateProgressDialog(UpdateActivity.this);
                    dialog.show();
                    setAPPJson(containerID,Et_image.getText().toString(),Integer.valueOf(textViewsb.getText().toString()).intValue(),memory,Et_cmd.getText().toString(),EnvsAdapter.getEnvsAdapter(),PortAdapter.getPortsAdapter(),"",Et_appname.getText().toString());
                }
            }
        });
    }
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    private String getSettingNote(String s){//获取保存设置
        SharedPreferences read = getSharedPreferences(Config.SHPF_COOKIE_INFO, MODE_PRIVATE);
        return read.getString(s, "");
    }

    private void setAPPJson(String id,String imagename,int instance,int memory,String cmd,List<Envs>envs,List<Ports>ports,String endpoint,String appname){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        //a参数%s镜像 %d实例 %d内存 %s命令
        //b参数 无
        //c参数%s env key %s env value
        //d参数 无
        //e参数 %d端口 %s类型
        //f参数 %s域名 %s appname
        String a="{\"data\":{\"type\": \"containers\",\"attributes\": {\"image_name\": \"%s\",\"instances\": %d,\"mem\":%d,\"cmd\": \"%s\",";
        String b="\"envs\": [";
        String c="{\"key\": \"%s\",\"value\": \"%s\"}";
        String d="],\"ports\": [";
        String e="{\"number\": %d,\"protocol\": \"%s\"}";
        String f="],\"arukas_domain\": \"%s\"}}}";//数据格式与create不通 注意


        String jsonObject="";
        jsonObject+=String.format(a, imagename,instance,memory,cmd);
        jsonObject+=b;
        for(int i=0;i<envs.size();i++){
            if(i!=0){
                jsonObject+=",";
                jsonObject+=String.format(c, envs.get(i).getKey(),envs.get(i).getValue());
            }else{
                jsonObject+=String.format(c, envs.get(i).getKey(),envs.get(i).getValue());
            }
        }
        jsonObject+=d;
        for(int i=0;i<ports.size();i++){
            if(i!=0) {
                jsonObject += ",";
                jsonObject+=String.format(e,ports.get(i).getNumber(),ports.get(i).getProtocol());
            }else{
                jsonObject+=String.format(e,ports.get(i).getNumber(),ports.get(i).getProtocol());
            }
        }
        jsonObject+=String.format(f,endpoint,appname);
        Log.d("$$",jsonObject);
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.PATCH,Config.API+Config.API_CONTAINERS+"/"+id, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        finish();
                        Toast.makeText(UpdateActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                        Log.d("", "response -> " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.e("", error.getMessage(), error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type","application/vnd.api+json");
                //headers.put("Content-Length: ",a.length()+"");
                headers.put("Cookie",getSettingNote(Config.SHPF_COOKIE));
                return headers;
            }
        };
        mQueue.add(jsonRequest);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
