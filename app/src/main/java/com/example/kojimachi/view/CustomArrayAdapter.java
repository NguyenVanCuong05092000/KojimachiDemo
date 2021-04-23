package jp.co.kojimachi.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jp.co.kojimachi.R;
import jp.co.kojimachi.entity.EntityArrayAdapter;

public class CustomArrayAdapter extends BaseAdapter {

    private static final String TAG = "CustomArrayAdapter";

    private ArrayList<EntityArrayAdapter> arrayAdapters;
    private LayoutInflater mInflater;

    public CustomArrayAdapter(Context context, ArrayList<EntityArrayAdapter> objects) {
        mInflater = LayoutInflater.from(context);
        arrayAdapters = objects;
    }

    @Override
    public int getCount() {
        return arrayAdapters.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayAdapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private int positionListViewClick = -1;

    public void changeTextColor(int position) {
        if (positionListViewClick > -1)
            arrayAdapters.get(positionListViewClick).isSelect = false;
        arrayAdapters.get(position).isSelect = true;
        positionListViewClick = position;
        notifyDataSetChanged();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dropdown, null);
            holder = new ViewHolder();
            holder.tvItem = (TextView) convertView.findViewById(R.id.tvItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvItem.setText(arrayAdapters.get(position).name);
        if (arrayAdapters.get(position).isSelect)
            holder.tvItem.setTextColor(Color.parseColor("#00C9A0"));
        else
            holder.tvItem.setTextColor(Color.parseColor("#515151"));
        return convertView;
    }

    static class ViewHolder {
        TextView tvItem;
    }
}
