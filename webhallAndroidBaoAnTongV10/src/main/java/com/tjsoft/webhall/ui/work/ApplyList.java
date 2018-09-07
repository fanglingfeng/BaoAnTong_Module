package com.tjsoft.webhall.ui.work;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.Form;

/**
 * 申请材料列表页面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("HandlerLeak")
public class ApplyList extends Fragment {
	private TextView formList, bigFile, license, applyForm;
	private String PERMID;
	private List<Form> forms;
	private List<ApplyBean> applyBeans;
	private final int GET_FORMS_SUCCESS = 1;
	private final int GET_APPLY_BEAN_SUCCESS = 2;
	private Handler handler = new MyHandler();
	private String formsContent = "";
	private String bigFileContent = "";
	private String licenseContent = "";
	private String applyFormContent = "";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_applylist"), container, false);
		formList = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "formList"));
		bigFile = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "bigFile"));
		license = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "license"));
		applyForm = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "applyForm"));
		PERMID = getActivity().getIntent().getStringExtra("PERMID");
	
		formsContent = "";
		bigFileContent = "";
		licenseContent = "";
		applyFormContent = "";
		Background.Process(ApplyList.this.getActivity(), GetApplyList,
				getString(MSFWResource.getResourseIdByName(ApplyList.this.getActivity(), "string", "tj_loding")));
		new Thread(GetFormList).start();
		return view;
	}

	/**
	 * 获取申报信息
	 */
	final Runnable GetApplyList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				param.put("PAGENO", "1");
//				param.put("P_GROUP_ID", PermGuideContainer.P_GROUP_ID);
				param.put("PAGESIZE", "1000");
				String response = HTTP.excute("getClxxByPermid",
						"RestPermissionitemService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					applyBeans = JSONUtil.getGson().fromJson(
							json.getString("ReturnValue"),
							new TypeToken<List<ApplyBean>>() {
							}.getType());
					handler.sendEmptyMessage(GET_APPLY_BEAN_SUCCESS);
				} else {
					DialogUtil.showUIToast(ApplyList.this.getActivity(),
							json.getString("error"));
				}
			} catch (Exception e) {
				DialogUtil.showUIToast(ApplyList.this.getActivity(),
						getString(MSFWResource.getResourseIdByName(ApplyList.this.getActivity(), "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	/**
	 * 获取表单信息
	 */
	final Runnable GetFormList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				String response = HTTP.excute("getFormByPermid",
						"RestPermissionitemService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					forms = JSONUtil.getGson().fromJson(
							json.getString("ReturnValue"),
							new TypeToken<List<Form>>() {
							}.getType());

					handler.sendEmptyMessage(GET_FORMS_SUCCESS);
				} else {
					DialogUtil.showUIToast(ApplyList.this.getActivity(),
							json.getString("error"));
					getActivity().finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ApplyList.this.getActivity(),
						getString(MSFWResource.getResourseIdByName(ApplyList.this.getActivity(), "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_FORMS_SUCCESS:
				if (null != forms) {// 获取基本表单名称
					for (int i = 0; i < forms.size(); i++) {
						if (forms.get(i).getFORMTYPE().equals("0")) {
							formsContent += ("\n" + "1、"
									+ forms.get(i).getNAME() + "\n");
							return;
						}
					}
				}
				formList.setText(formsContent);

				break;
			case GET_APPLY_BEAN_SUCCESS:
				if (null != applyBeans) {
					int bigFileLine = 1, licenseLine = 1, applyFormLine = 1;
					for (int i = 0; i < applyBeans.size(); i++) {
						if (applyBeans.get(i).getDZHYQ().contains("3")) {
							bigFileContent += "\n" + bigFileLine + "、"
									+ applyBeans.get(i).getCLMC() + "\n";
							bigFileLine++;
						}
						if (applyBeans.get(i).getDZHYQ().contains("5")
								|| applyBeans.get(i).getDZHYQ().contains("1")) {// 申请表内容
							applyFormContent += "\n" + applyFormLine + "、"
									+ applyBeans.get(i).getCLMC();
							applyFormLine++;
						}
						if (applyBeans.get(i).getDZHYQ().contains("4")) {
							licenseContent += "\n" + licenseLine + "、"
									+ applyBeans.get(i).getCLMC() + "\n";
							licenseLine++;
						}
					}
				}
				formList.setText(formsContent);
				bigFile.setText(bigFileContent);
				license.setText(licenseContent);
				applyForm.setText(applyFormContent);
				break;
			default:
				break;
			}
		}
	}

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.back:
//			getActivity().finish();
//			break;
//
//		default:
//			break;
//		}
//
//	}
}
