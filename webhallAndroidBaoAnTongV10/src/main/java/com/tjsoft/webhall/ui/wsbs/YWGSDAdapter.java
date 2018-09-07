package com.tjsoft.webhall.ui.wsbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;

import java.util.List;

public class YWGSDAdapter extends ArrayAdapter<String> {
    private int resoureId;
    private List<String> objects;
    private Context context;
    private boolean isFirst;


    public YWGSDAdapter(Context context, int resourceId, List<String> objects) {
        super(context, resourceId, objects);
        // TODO Auto-generated constructor stub
        this.objects=objects;
        this.context=context;

    }
    public YWGSDAdapter(boolean isFirst,Context context, int resourceId, List<String> objects) {
        super(context, resourceId, objects);
        // TODO Auto-generated constructor stub
        this.objects=objects;
        this.context=context;
        this.isFirst = isFirst;
    }

    private static class ViewHolder
    {
        TextView title;


    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
            LayoutInflater mInflater=LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.simple_list_item_1, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String person = objects.get(position);
        if(null!=person)
        {
            viewHolder.title.setText(objects.get(position));
        }
//        if (position == 0&&!isFirst) {
//            convertView.setBackgroundResource(R.color.red);
//        } else {
//            convertView.setBackgroundResource(R.color.white);
//
//        }
        return convertView;
    }


}
