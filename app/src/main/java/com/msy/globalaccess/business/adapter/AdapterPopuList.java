package com.msy.globalaccess.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.msy.globalaccess.R;

import java.util.ArrayList;

import cn.msy.zc.commonutils.StringUtils;

/**
 * 设置ListPopuWindow的adapter
 * Created by chensh on 2017/1/22 0022.
 */

public class AdapterPopuList extends BaseAdapter {

    private Context context;
    private ArrayList<String> list = new ArrayList<>();

    public AdapterPopuList(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHodler holder;
        if (convertView == null) {
            holder = new AdapterHodler();
            convertView = LayoutInflater.from(context).inflate(R.layout.popuwindow_list_item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.pop_list_text);
            convertView.setTag(holder);
        } else {
            holder = (AdapterHodler) convertView.getTag();
        }
        if (!StringUtils.isEmpty(list.get(position))) {
            holder.textView.setText(list.get(position));
        }
        return convertView;
    }

    class AdapterHodler {
        TextView textView;
    }
}
