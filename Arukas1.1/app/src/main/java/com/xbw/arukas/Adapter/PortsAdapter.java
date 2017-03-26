package com.xbw.arukas.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbw.arukas.R;
import com.xbw.arukas.gsonContainer.Envs;
import com.xbw.arukas.gsonContainer.Ports;

import java.util.List;

/**
 * Created by xubowen on 2017/3/23.
 */
public class PortsAdapter extends BaseAdapter{
    private List<Ports> mDate;
    private Context mContext;

    public PortsAdapter(Context mContext, List<Ports> mDate) {
        this.mContext = mContext;
        this.mDate = mDate;
    }
    public List<Ports> getPortsAdapter(){
        return mDate;
    }
    public void addItem(Ports p) {
        mDate.add(p);
        this.notifyDataSetChanged();
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
    public Object getItem(int position) {
        return mDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @SuppressLint("NewApi") @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = View.inflate(mContext, R.layout.ports_item, null);
        // 初始化
        Ports model = mDate.get(position);
        TextView txt_port = (TextView) view.findViewById(R.id.ET_portt);
        TextView txt_cont = (TextView) view.findViewById(R.id.ET_cont);
        ImageView img_sub = (ImageView)view.findViewById(R.id.Img_Psub);
        // 绑定数据
        //txt_id.setText(model.getOrderID());
        txt_port.setText(model.getNumber()+"");
        txt_cont.setText(model.getProtocol()+"");
        img_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletItem(position);
            }
        });

        // 返回
        return view;
    }
}
