package com.xbw.arukas.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.xbw.arukas.R;
import com.xbw.arukas.gsonContainer.Envs;
import com.xbw.arukas.gsonContainer.Ports;

import java.util.List;

/**
 * Created by xubowen on 2017/3/22.
 */
public class EnvAdapter extends BaseAdapter {

    private List<Envs> mDate;
    private Context mContext;

    public EnvAdapter(Context mContext, List<Envs> mDate) {
        this.mContext = mContext;
        this.mDate = mDate;
    }
    public List<Envs> getEnvsAdapter(){
        return mDate;
    }
    public void addItem(Envs e) {
        mDate.add(e);
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.env_item, null);
        Envs model = mDate.get(position);
        ViewHolderEnv.Et_key=(TextView)view.findViewById(R.id.ET_key);
        ViewHolderEnv.Et_value=(TextView) view.findViewById(R.id.ET_value);
        //ViewHolderEnv.Img_eadd=(ImageView) view.findViewById(R.id.Img_Eadd);
        ViewHolderEnv.Et_key.setText(model.getKey());
        ViewHolderEnv.Et_value.setText(model.getValue());
        ViewHolderEnv.Img_esub=(ImageView)view.findViewById(R.id.Img_Esub);
        ViewHolderEnv.Img_esub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletItem(position);
            }
        });

        return view;
    }
    static class ViewHolderEnv
    {
        private static TextView Et_key;
        private static TextView Et_value;
        //private static ImageView Img_eadd;
        private static ImageView Img_esub;
    }
}
