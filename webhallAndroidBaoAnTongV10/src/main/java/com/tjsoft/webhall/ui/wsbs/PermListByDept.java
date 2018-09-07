package com.tjsoft.webhall.ui.wsbs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.ShowPopupMoreUtil;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.util.VoiceSearchUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.FavoriteManage;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.ui.search.PermListByName;
import com.tjsoft.webhall.util.SharedPreferenceUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 根据事项查找事项列表
 *
 * @author Administrator
 */
public class PermListByDept extends AutoDialogActivity {
    private ListView permList;
    private List<Permission> permissions;
    //只在事项编码里面的  》756的不展示
    private List<Permission> InCodepermissions = new ArrayList<Permission>();
    private LayoutInflater layoutInflater;
    private ProgressDialog mProgress;
    private MyHandler handler = new MyHandler();
    private final int GET_PERM_SUCCESS = 1;
    private String DEPTID = "";
    private TextView typeName;
    private TextView titleName;
    private Button home;
    private RelativeLayout back;
    private Intent intent;
    private TextView noData;
    private String SFYDSB = ""; // 是否可申报项列表：1为可申报列表
    private Context mContext;
    private Button btnSearch;
    private EditText textSearch;
    private int flag;//1 驻区部门进来 跳转到web界面

    private ImageView voice;
    private VoiceSearchUtil mVoice;
    private PermAdapter adapter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatisticsTools.start();
        setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_permlist"));
        Constants.getInstance().addActivity(this);
        Constants.WSBS_PATH = getIntent().getIntExtra("WSBSFLAG", 0);
        mContext = this;
        sp = this.getSharedPreferences("Version", MODE_PRIVATE);

        InitView();
        dialog = Background.SingleProcess(this, GetPermList, getString(MSFWResource.getResourseIdByName(PermListByDept.this, "string", "tj_loding")));

        mVoice = new VoiceSearchUtil(this, textSearch, btnSearch);
        voice = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "voice_iv"));
        voice.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mVoice.startListenVoice();
            }
        });
    }

    @Override
    protected void onDestroy() {
        StatisticsTools.end(getIntent().getStringExtra("name"), null, null);
        super.onDestroy();
    }

    private void InitView() {
        btnSearch = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnSearch"));
        textSearch = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "textSearch"));
        btnSearch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(textSearch.getText().toString())) {
                    DialogUtil.showUIToast(PermListByDept.this, "事项名称不能为空！");
                } else {
                    intent = new Intent();
                    intent.setClass(PermListByDept.this, PermListByName.class);
                    intent.putExtra("name", textSearch.getText().toString().trim());
                    startActivity(intent);
                }

            }
        });
        flag = getIntent().getIntExtra("fromflag", 0);
        noData = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "noData"));
        layoutInflater = getLayoutInflater();
        permList = (ListView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "permList"));
        if (flag == 1) {
            permList.setOnItemClickListener(new MyItemClick1());
        } else {
            permList.setOnItemClickListener(new MyItemClick());
        }
        permList.setOnItemLongClickListener(new MyItemLongClick());
        ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "empty"));
        permList.setEmptyView(empty);
        adapter = new PermAdapter(mContext, InCodepermissions);
        permList.setAdapter(adapter);

        back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "back"));
        home = (Button) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "home"));
        home.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ShowPopupMoreUtil.PermListshowPopupMore(v, mContext);
            }
        });
        typeName = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "typeName"));
        titleName = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "titleName"));
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PermListByDept.this.finish();
            }
        });
        DEPTID = getIntent().getStringExtra("DEPTID");
        SFYDSB = getIntent().getStringExtra("SFYDSB");
        String name = getIntent().getStringExtra("name");
        typeName.setText("您选择的是" + name + "，请继续选择办理事项");
        if (Constants.WSBS_PATH == 1) {
            titleName.setText("选择办理事项");

        } else if (Constants.WSBS_PATH == 2) {
            titleName.setText("选择办理事项（1/3）");
        }

    }

    private class MyItemLongClick implements OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
            new AlertDialog.Builder(PermListByDept.this).setMessage("是否将该事项加入收藏夹？").setTitle(getString(MSFWResource.getResourseIdByName(PermListByDept.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    FavoriteManage.add(InCodepermissions.get(position), PermListByDept.this);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            }).show();
            return true;
        }

    }

    private class MyItemClick1 implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*			intent = new Intent();
            intent.setClass(PermListByDept.this, WSBSWeb.class);
			intent.putExtra("url", InCodepermissions.get(position).getWSSBDZ());
			startActivity(intent);*/
            adapter.setSelsetorItem(position);
            adapter.notifyDataSetInvalidated();
        }

    }

    private class MyItemClick implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*			intent = new Intent();
            intent.setClass(PermListByDept.this, HistoreShare_v2.class);
			intent.putExtra("PERMID", InCodepermissions.get(position).getID());
			intent.putExtra("permission", InCodepermissions.get(position));
			intent.putExtra("WSBSFLAG", Constants.WSBS_PATH);
			startActivity(intent);*/
            adapter.setSelsetorItem(position);
            adapter.notifyDataSetInvalidated();
        }

    }

    final Runnable GetPermList = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("DEPTID", DEPTID);
                param.put("PAGENO", "1");
                param.put("PAGESIZE", "1000");
                param.put("SFYDSB", SFYDSB);
                String response = HTTP.excuteAndCache("getPermlistByDeptid", "RestSysDeptService", param.toString(), PermListByDept.this);
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    permissions = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Permission>>() {
                    }.getType());

                    //暂时放开这个756的限制
                    InCodepermissions.clear();
                    InCodepermissions.addAll(permissions);
                    Collections.sort(InCodepermissions, new PermissionComparator());
//					for (int i = 0; i <permissions.size(); i++) {						
//						if(!TextUtils.isEmpty(permissions.get(i).getCODE3())&&!permissions.get(i).getCODE3().equals("null")){
//							int CODE3=Integer.parseInt(permissions.get(i).getCODE3().substring(0, 3));
//							int permCode=756;
////							Log.d("PermList", "permCode==="+permCode+"       CODE3======"+CODE3);
//							if(CODE3<=permCode){
//								InCodepermissions.add(permissions.get(i));
//								
//								Collections.sort(InCodepermissions,new PermissionComparator());
//							}
//						}
//						
//					}
                    Log.e("sps====", "事项编码756事项数====" + InCodepermissions.size());
                    handler.sendEmptyMessage(GET_PERM_SUCCESS);

                } else {
                    DialogUtil.showUIToast(PermListByDept.this, getString(MSFWResource.getResourseIdByName(PermListByDept.this, "string", "tj_occurs_error_network")));
                    finish();
                }

            } catch (Exception e) {
//				DialogUtil.showUIToast(PermListByDept.this, getString(MSFWResource.getResourseIdByName(PermListByDept.this, "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };

    private class PermissionComparator implements Comparator<Permission> {
        @Override
        public int compare(Permission o1, Permission o2) {
            if (!TextUtils.isEmpty(o1.getCODE3()) && !o1.getCODE3().equals("null") && !TextUtils.isEmpty(o2.getCODE3()) && !o2.getCODE3().equals("null")) {
                return o1.getCODE3().compareTo(o2.getCODE3());
            } else {
                return o2.getCODE3().compareTo(o1.getCODE3());
            }
        }
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_PERM_SUCCESS:
                    adapter.addData(InCodepermissions);

                    typeName.setText("您选择的是" + getIntent().getStringExtra("name") + "，事项总数：" + permissions.size());
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {

                String result = data.getStringExtra("result ");
                System.out.println("fuchl  得到result" + result);
                if (TextUtils.equals(result, "success")) {
                    Object object = SharedPreferenceUtil.get(mContext,"file_key", "value_key");
                    if (object != null) {
                        Permission permission = (Permission) object;
                        System.out.println("fuchl  取出permission" + permission.toString());

                        adapter.nextActivity(1, permission);

                    }

                } else {
                    DialogUtil.showUIToast(mContext,
                            "认证失败");
                }


            }
        }
    }
}
