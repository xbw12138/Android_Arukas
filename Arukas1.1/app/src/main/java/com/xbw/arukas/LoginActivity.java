package com.xbw.arukas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.umeng.analytics.MobclickAgent;
import com.xbw.arukas.Config.Config;
import com.xbw.arukas.Utils.Progress_Dialog;

import android.view.View.OnClickListener;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xubowen on 2017/3/19.
 */
public class LoginActivity extends Activity {
    private EditText mUsernameET;
    private EditText mPasswordET;
    private Button mSigninBtn;
    private Button mForget;
    private Button mSignupTV;
    private CheckBox mPasswordCB;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //LoginCookie("xbw@ecfun.cc","xbw12138");
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    private void LoginCookie(final String email, final String password){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.API+Config.API_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {}
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("email", email);
                map.put("password", password);
                return map;
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Accept", "application/vnd.api+json");
                //headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                dialog.dismiss();
                try {
                    Log.d("stauscode",response.statusCode+"");
                    if(response.statusCode==200){
                        Map<String, String> responseHeaders = response.headers;
                        String rawCookies = responseHeaders.get("Set-Cookie");
                        String a[]=rawCookies.split(";");
                        //保存Cookie-saveSettingNote是本地存储
                        saveSettingNote(Config.SHPF_COOKIE,a[0]);
                        Log.i("px",a[0]+"\n");
                        //保存用户名密码
                        saveSettingUser(Config.SHPF_USER,mUsernameET.getText().toString());
                        saveSettingPass(Config.SHPF_PASS,mPasswordET.getText().toString());
                        //登录成功跳转
                        //Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent();
                        intent.setClass(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    String dataString = new String(response.data, "UTF-8");
                    Log.i("px",dataString);
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    Log.d("stauscode2",response.statusCode+"");
                    if(response.statusCode==422){
                        Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(LoginActivity.this,"未知错误",Toast.LENGTH_LONG).show();
                    }
                    return Response.error(new ParseError(e));
                }
            }
        };
        mQueue.add(stringRequest);
    }

    private void initView()
    {
        mUsernameET = (EditText) findViewById(R.id.chat_login_username);
        mPasswordET = (EditText) findViewById(R.id.chat_login_password);
        mSigninBtn = (Button) findViewById(R.id.chat_login_signin_btn);
        mSignupTV = (Button) findViewById(R.id.chat_login_signup);
        mPasswordCB = (CheckBox) findViewById(R.id.chat_login_password_checkbox);
        mForget=(Button) findViewById(R.id.chat_forgot_password);

        if(!getSettingUser(Config.SHPF_USER).equals("")&&!getSettingPass(Config.SHPF_PASS).equals("")){
            String name=getSettingUser(Config.SHPF_USER);
            String pass=getSettingPass(Config.SHPF_PASS);
            mUsernameET.setText(name);
            mUsernameET.setSelection(name.length());
            mPasswordET.setText(pass);
            mPasswordET.setSelection(pass.length());
        }
        mPasswordCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    mPasswordCB.setChecked(true);
                    //动态设置密码是否可见
                    mPasswordET
                            .setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());
                } else {
                    mPasswordCB.setChecked(false);
                    mPasswordET
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                }
            }
        });
        mSigninBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final String userName = mUsernameET.getText().toString().trim();
                final String password = mPasswordET.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "请输入邮箱",
                            Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码",
                            Toast.LENGTH_SHORT).show();
                } else {
                    LoginCookie(userName,password);
                    dialog = Progress_Dialog.CreateProgressDialog(LoginActivity.this);
                    dialog.show();
                }
            }
        });
        mForget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://app.arukas.io");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mSignupTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Uri uri = Uri.parse("https://app.arukas.io");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                //Intent mIntent = new Intent();
                //mIntent.setClass(LoginActivity.this, WebActivity.class);
                //mIntent.putExtra("title_name",
                //        getString(R.string.register_sign_up));
                //mIntent.putExtra("url",getString(R.string.menu_button_url_11));
                //startActivity(mIntent);
            }
        });

    }


    private void saveSettingNote(String s,String saveData){//保存设置
        SharedPreferences.Editor note = getSharedPreferences(Config.SHPF_COOKIE_INFO,MODE_PRIVATE).edit();
        note.putString(s, saveData);
        note.commit();
    }
    private String getSettingNote(String s){//获取保存设置
        SharedPreferences read = getSharedPreferences(Config.SHPF_COOKIE_INFO, MODE_PRIVATE);
        return read.getString(s, "");
    }

    private void saveSettingUser(String s,String saveData){//保存设置
        SharedPreferences.Editor note = getSharedPreferences(Config.SHPF_COOKIE_INFO,MODE_PRIVATE).edit();
        note.putString(s, saveData);
        note.commit();
    }
    private String getSettingUser(String s){//获取保存设置
        SharedPreferences read = getSharedPreferences(Config.SHPF_COOKIE_INFO, MODE_PRIVATE);
        return read.getString(s, "");
    }
    private void saveSettingPass(String s,String saveData){//保存设置
        SharedPreferences.Editor note = getSharedPreferences(Config.SHPF_COOKIE_INFO,MODE_PRIVATE).edit();
        note.putString(s, saveData);
        note.commit();
    }
    private String getSettingPass(String s){//获取保存设置
        SharedPreferences read = getSharedPreferences(Config.SHPF_COOKIE_INFO, MODE_PRIVATE);
        return read.getString(s, "");
    }


}
