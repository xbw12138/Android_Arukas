package com.xbw.arukas.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import com.xbw.arukas.Model.PortModel;
import com.xbw.arukas.R;
import com.xbw.arukas.gsonContainer.Ports;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by xubowen on 2017/3/22.
 */
public class PortAdapter extends BaseAdapter {

    private ViewHolderPort mViewHolder;
    private List<Ports> mDate;
    private Context mContext;

    public PortAdapter(Context mContext, List<Ports> mDate) {
        this.mContext = mContext;
        this.mDate = mDate;
    }
    public void addItem(int a) {
        Ports p=new Ports();
        p.setNumber(a);
        p.setProtocol("TCP");
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
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        /*if (convertView == null) {
            mViewHolder = new ViewHolderPort();
            convertView = View.inflate(mContext,R.layout.ports_item, null);
            mViewHolder.Et_port = (TextView) convertView.findViewById(R.id.ET_port);
            mViewHolder.Rb_ccon = (RadioButton) convertView.findViewById(R.id.RadioC_Con);
            mViewHolder.Rb_dcon = (RadioButton) convertView.findViewById(R.id.RadioD_Con);
            mViewHolder.Img_psub = (ImageView) convertView.findViewById(R.id.Img_Psub);
        */
            /*mViewHolder.Et_port.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    //注意，此处必须使用getTag的方式，不能将position定义为final，写成mTouchItemPosition = position
                    mTouchItemPosition = (Integer) view.getTag();
                    return false;
                }
            });*/
            /*mViewHolder.Img_psub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletItem(position);
                    Log.d(""+position,"caioomnnd****");
                }
            });*/
           /* convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolderPort) convertView.getTag();
        }
        */
        //if(convertView==null){
        //    convertView=mLayoutInflater.inflate(R.layout.ports_item,null);
        //}
        //convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ports_item, null);
        //EditText Et_port=(EditText)convertView.findViewById(R.id.ET_port);
        //RadioButton Rb_ccon=(RadioButton)convertView.findViewById(R.id.RadioC_Con);
        //RadioButton Rb_dcon=(RadioButton)convertView.findViewById(R.id.RadioD_Con);
        //ImageView Img_psub=(ImageView)convertView.findViewById(R.id.Img_Psub);

        //Et_port.setText("888");
        /*Et_port.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDate.get(position).setNumber(Integer.valueOf(charSequence.toString()).intValue());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
        //Et_port.setText(mDate.get(position).getNumber());
        //View view = View.inflate(mContext, R.layout.ports_item, null);
        //ViewHolderPort m=new ViewHolderPort();
        //final Ports model=mDate.get(position);

        //ViewHolderPort.Et_port = (EditText) convertView .findViewById(R.id.ET_port);
        //ViewHolderPort.Rb_ccon = (RadioButton) convertView .findViewById(R.id.RadioC_Con);
        //ViewHolderPort.Rb_dcon = (RadioButton) convertView .findViewById(R.id.RadioD_Con);
        //ViewHolderPort.Img_psub = (ImageView) convertView .findViewById(R.id.Img_Psub);
        //m.Et_port.setText(model.getNumber());
        /*Et_port.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                //注意，此处必须使用getTag的方式，不能将position定义为final，写成mTouchItemPosition = position
                mTouchItemPosition = (Integer) view.getTag();
                return false;
            }
        });*/
        /*ViewHolderPort.Et_port.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mDate.size()>0){
                    Ports p=new Ports();
                    p.setNumber(Integer.valueOf(ViewHolderPort.Et_port.getText().toString()).intValue());
                    p.setProtocol("TCP");
                    mDate.set(position,p);
                }
            }
        });*/
        //Ports p=mDate.get(position);
        //mViewHolder.Et_port.setText(p.getNumber());
        //mViewHolder.Img_psub.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        deletItem(position);
        //        Log.d(""+position,"caioomnnd****");
        //    }
        //});
        /*ViewHolderPort.Et_port.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Ports ports=new Ports();
                ports.setNumber(Integer.valueOf(editable.toString()).intValue());
                mDate.add(position,ports);
            }
        });*/
        // 让ViewHolder持有一个TextWathcer，动态更新position来防治数据错乱；不能将position定义成final直接使用，必须动态更新
        //ViewHolderPort.mTextWatcher = new MyTextWatcher();
        //ViewHolderPort.Et_port.addTextChangedListener(ViewHolderPort.mTextWatcher);
        //ViewHolderPort.updatePosition(position);


        //Img_psub.setTag(position);
        //Et_port.setTag(position);
        //ViewHolderPort.Et_port.setText(mDate.get(position).getNumber());
        //mViewHolder.Et_port.setText(position+"");

        /*if (mTouchItemPosition == position) {
            Et_port.requestFocus();
            Et_port.setSelection(Et_port.getText().length());
        } else {
            Et_port.clearFocus();
        }*/
        View view = View.inflate(mContext, R.layout.ports_item, null);
        TextView Et_port=(TextView)view.findViewById(R.id.ET_port);
        Ports p=mDate.get(position);
        Et_port.setText(p.getNumber());
        return convertView;
    }

    static class ViewHolderPort
    {
        private static TextView Et_port;
        private static RadioGroup Rg_port;
        private static RadioButton Rb_ccon;
        private static RadioButton Rb_dcon;
        //private static ImageView Img_padd;
        private static ImageView Img_psub;

        //private static MyTextWatcher mTextWatcher;
        //动态更新TextWathcer的position
        //public static void updatePosition(int position) {
        //    mTextWatcher.updatePosition(position);
        //}
    }

    /*class MyTextWatcher implements TextWatcher {
        //由于TextWatcher的afterTextChanged中拿不到对应的position值，所以自己创建一个子类
        private int mPosition;

        public void updatePosition(int position) {
            mPosition = position;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Ports editable) {
            mDate.set(mPosition, editable);
        }
        @Override
        public void afterTextChanged(Editable editable) {
           // mDate.set(mPosition,);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
    };*/
}
