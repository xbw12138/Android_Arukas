package com.xbw.arukas.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xbw.arukas.Config.Config;
import com.xbw.arukas.DetailActivity;
import com.xbw.arukas.MainActivity;
import com.xbw.arukas.Model.MainModel;
import com.xbw.arukas.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xubowen on 2017/3/23.
 */
public class MainAdapter extends BaseAdapter {
    private List<MainModel> mDate;
    private Context mContext;
    public MainAdapter(Context mContext, List<MainModel> mDate) {
        this.mContext = mContext;
        this.mDate = mDate;
    }
    public void deletItem(int pos) {
        mDate.remove(pos);
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int i) {
        return mDate.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolderMain viewHolderMain;
        final MainModel mainModel=mDate.get(i);
        //if(view==null){
        viewHolderMain=new ViewHolderMain();
        view= LayoutInflater.from(mContext).inflate(R.layout.main_item,null,true);
        viewHolderMain.Tv_name=(TextView)view.findViewById(R.id.title);
        viewHolderMain.TV_id=(TextView)view.findViewById(R.id.sub_title);
        viewHolderMain.TV_detail=(TextView)view.findViewById(R.id.sub_detail);
        viewHolderMain.TV_time=(TextView)view.findViewById(R.id.sub_time);
        viewHolderMain.Img_detail=(ImageView) view.findViewById(R.id.detail_item);
        viewHolderMain.Img_del=(ImageView) view.findViewById(R.id.remove_item);
        //view.setTag(viewHolderMain);
        //}else{
        //    viewHolderMain=(ViewHolderMain)view.getTag();
        //}
        viewHolderMain.Tv_name.setText(mainModel.getAppname());
        viewHolderMain.TV_id.setText(mainModel.getAppid());
        viewHolderMain.TV_time.setText(mainModel.getApptime());
        viewHolderMain.Img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("警告");
                builder.setMessage("确定要删除"+mainModel.getAppname()+"吗?删除不可恢复");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //在此写入请求删除docker
                        //deletItem(i);
                        deleteApps(mainModel.getAppdelid(),i);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        viewHolderMain.TV_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(mContext,DetailActivity.class);
                intent.putExtra("containerID",mainModel.getAppid());
                intent.putExtra("name",mainModel.getAppname());
                mContext.startActivity(intent);
            }
        });
        viewHolderMain.Img_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(mContext,DetailActivity.class);
                intent.putExtra("containerID",mainModel.getAppid());
                intent.putExtra("name",mainModel.getAppname());
                mContext.startActivity(intent);
            }
        });
        return view;
    }
    private void deleteApps(String id,final int i){
        RequestQueue mQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, Config.API+Config.API_APPS+"/"+id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //
                deletItem(i);
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
        SharedPreferences read = mContext.getSharedPreferences(Config.SHPF_COOKIE_INFO, mContext.MODE_PRIVATE);
        return read.getString(s, "");
    }
    static class ViewHolderMain
    {
        private static TextView Tv_name;
        private static TextView TV_id;
        private static TextView TV_time;
        private static ImageView Img_del;
        private static ImageView Img_detail;
        private static TextView TV_detail;
    }
}
