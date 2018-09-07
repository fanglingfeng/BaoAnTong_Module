package com.tjsoft.webhall.ui.work;


import org.json.JSONObject;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Preaccept;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * 结果通知
 * @author Administrator
 *
 */
public class PreacceptNotice extends AutoDialogActivity {
	private TextView material, place, phone, traffic;
	private Button back;
	private String BSNUM = "";
	private Preaccept preaccept;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_preaccept_notice"));
		BSNUM = getIntent().getStringExtra("BSNUM");
		InitView();
		dialog = Background.Process(this, GetPreacceptNotice, getString(MSFWResource.getResourseIdByName(PreacceptNotice.this, "string", "tj_loding")));
	}

	final Runnable GetPreacceptNotice = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("BSNUM", BSNUM);
				String response = HTTP.excute("preacceptnotice", "RestOnlineDeclareService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					preaccept = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), Preaccept.class);
					runOnUiThread(new Runnable() {
						public void run() {
							if (null != preaccept && preaccept.getCLXXS() != null) {
								phone.setText(preaccept.getPHONE());
								place.setText(preaccept.getADDRESS());
								traffic.setText(preaccept.getTRAFFIC());
								String materialStr = "";
								for (int i = 0; i < preaccept.getCLXXS().size(); i++) {
									materialStr += (i+1)+"、"+ preaccept.getCLXXS().get(i).getCLMC() + "\n      （原件" + preaccept.getCLXXS().get(i).getORINUM() + "份,复印件" + preaccept.getCLXXS().get(i).getCOPYNUM() + "份）\n";
								}
								material.setText(materialStr);
							}
						}
					});
				} else {
					DialogUtil.showUIToast(PreacceptNotice.this, getString(MSFWResource.getResourseIdByName(PreacceptNotice.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(PreacceptNotice.this, getString(MSFWResource.getResourseIdByName(PreacceptNotice.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	private void InitView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PreacceptNotice.this.finish();
			}
		});
		material = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "material"));
		place = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "place"));
		phone = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "phone"));
		traffic = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "traffic"));
	}

}
