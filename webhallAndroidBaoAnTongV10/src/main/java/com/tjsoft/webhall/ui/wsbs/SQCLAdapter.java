package com.tjsoft.webhall.ui.wsbs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.entity.SQCLSMEntry;

import java.util.ArrayList;
import java.util.List;

public class SQCLAdapter extends BaseAdapter {
    private List<SQCLSMEntry> data;
    private Context mContext;
    private int count = 1;

    public SQCLAdapter(Context context, List<SQCLSMEntry> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        if (data != null) {
            return data.get(arg0);
        } else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    MSFWResource.getResourseIdByName(mContext, "layout",
                            "tj_guide_sqcl_item"), parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView
                    .findViewById(MSFWResource.getResourseIdByName(mContext,
                            "id", "tv_name"));
            viewHolder.tvGuide = (TextView) convertView
                    .findViewById(MSFWResource.getResourseIdByName(mContext,
                            "id", "tv_guide"));
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        final SQCLSMEntry entry = data.get(position);
        viewHolder.tvGuide.setText(entry.getP_GROUP_NAME());
        if (position == 0) {
            viewHolder.tvGuide.setVisibility(View.VISIBLE);
            count = 1;
//			data.get(position).setShowGuide(true);
        } else {
            if (TextUtils.equals(entry.getP_GROUP_ID(), data.get(position - 1).getP_GROUP_ID())) {
                viewHolder.tvGuide.setVisibility(View.GONE);
                count++;
            } else {
                viewHolder.tvGuide.setVisibility(View.VISIBLE);
                count = 1;
            }
        }
        if (entry.getCLMC() != null) {
            viewHolder.tvName.setText(count + "." + entry.getCLMC() + "(原件" + entry.getORINUM() + "份，复印件" + entry.getCOPYNUM() + "份)");
            viewHolder.tvName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    if (TextUtils.isEmpty(entry.getFILE_URL())) {
                        Toast.makeText(mContext, "该文件暂未提供预览功能", Toast.LENGTH_SHORT)
                             .show();
                    } else {
                        String[] split = entry.getFILE_URL()
                                             .split(",");
                        List<Uri> uris = new ArrayList<>();
                        for (int j = 0; j < split.length; j++) {
                            Uri uri = null;

                            if (split[j].toString().contains("upload")) {
                                uri = Uri.parse("http://sxsl.sz.gov.cn/qz" +split[j].toString());
                            } else {
                                uri = Uri.parse("http://sxsl.sz.gov.cn/qzapi/qzqd/attach/upload/" + split[j].toString());
                            }

                            uris.add(uri);



                        }
                        for (int i = 0; i < uris.size(); i++) {
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uris.get(i));
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, uris.get(i)));
                        }


                    }

//                    Uri uri = null;
//                    if (null != entry.getFILE_URL()) {
//                        if (entry.getFILE_URL().toString().contains("upload")) {
//                            uri = Uri.parse("http://sxsl.sz.gov.cn/qz" + entry.getFILE_URL().toString());
//                        } else {
//                            uri = Uri.parse("http://sxsl.sz.gov.cn/qzapi/qzqd/attach/upload/" + entry.getFILE_URL().toString());
//                        }
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        mContext.startActivity(intent);
//                    } else {
//                        Toast.makeText(mContext, "该文件暂未提供预览功能", Toast.LENGTH_SHORT).show();
//                    }


                }
            });
        }

        return convertView;
    }

    static class ViewHolder {
        public TextView tvName;
        public TextView tvGuide;
    }

}
