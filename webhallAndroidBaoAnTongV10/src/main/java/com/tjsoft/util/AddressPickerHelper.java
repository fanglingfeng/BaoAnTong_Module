package com.tjsoft.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.tjsoft.webhall.entity.JsonBean;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by TYKY001 on 2017/6/2.
 */

public class AddressPickerHelper {
    private Context context;
    private ArrayList<JsonBean> options1Items = new ArrayList<JsonBean>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();

    public AddressPickerHelper(Context context) {
        this.context = context;
    }

    public void ShowPickerView(final onSelectedListener listener, final View view) {// 弹出选择器
        new Thread(){
            @Override
            public void run() {
                super.run();
                initJsonData();
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showPicview(listener);
                    }
                });
            }
        }.start();
    }
    public void ShowPickerView(final onSelectedListener listener, final View view, final String pro, final String country) {// 弹出选择器
        new Thread(){
            @Override
            public void run() {
                super.run();
                initJsonData(pro,country);
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        showPicview(listener);
                    }
                });
            }
        }.start();
    }
    private void showPicview(final onSelectedListener listener) {
        if (options2Items.size() <= 0 || options1Items.size() <= 0 || options3Items.size() <= 0) {
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);

                listener.onSelected(checkText(options1Items.get(options1).getPickerViewText()),
                        checkText(options2Items.get(options1).get(options2)),
                        checkText(options3Items.get(options1).get(options2).get(options3)));
            }
        }).setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
    private void initJsonData() {//解析数据

        if (options2Items.size() <= 0 || options1Items.size() <= 0 || options3Items.size() <= 0) {
            /**
             * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
             * 关键逻辑在于循环体
             *
             * */
            String JsonData = getJson(context, "province.json");//获取assets目录下的json文件数据

            ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

            /**
             * 添加省份数据
             *
             * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
             * PickerView会通过getPickerViewText方法获取字符串显示出来。
             */
            options1Items = jsonBean;

            for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
                ArrayList<String> CityList = new ArrayList<String>();//该省的城市列表（第二级）
                ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<ArrayList<String>>();//该省的所有地区列表（第三极）

                for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                    String CityName = jsonBean.get(i).getCityList().get(c).getName();
                    CityList.add(CityName);//添加城市

                    ArrayList<String> City_AreaList = new ArrayList<String>();//该城市的所有地区列表

                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (jsonBean.get(i).getCityList().get(c).getArea() == null
                            || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                        City_AreaList.add("");
                    } else {

                        for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                            String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                            City_AreaList.add(AreaName);//添加该城市所有地区数据
                        }
                    }
                    Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                }

                /**
                 * 添加城市数据
                 */
                options2Items.add(CityList);

                /**
                 * 添加地区数据
                 */
                options3Items.add(Province_AreaList);
            }
        }
    }
    private void initJsonData(String pro,String city) {//解析数据

        if (options2Items.size() <= 0 || options1Items.size() <= 0 || options3Items.size() <= 0) {
            /**
             * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
             * 关键逻辑在于循环体
             *
             * */
            String JsonData = getJson(context, "province.json");//获取assets目录下的json文件数据

            ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
            options1Items = new ArrayList<JsonBean>();
            for(JsonBean jsonBean1 : jsonBean) {
                if(jsonBean1.getName().contains(pro)) {
                    options1Items.add(jsonBean1);
                    break;
                }
            }

            /**
             * 添加省份数据
             *
             * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
             * PickerView会通过getPickerViewText方法获取字符串显示出来。
             */

            for (int i = 0; i < options1Items.size(); i++) {//遍历省份
                ArrayList<String> CityList = new ArrayList<String>();//该省的城市列表（第二级）
                ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<ArrayList<String>>();//该省的所有地区列表（第三极）

                for (int c = 0; c < options1Items.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                    String CityName = options1Items.get(i).getCityList().get(c).getName();
                    if(CityName.contains(city)) {
                        CityList.add(CityName);//添加城市

                        ArrayList<String> City_AreaList = new ArrayList<String>();//该城市的所有地区列表

                        //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                        if (options1Items.get(i).getCityList().get(c).getArea() == null
                                || options1Items.get(i).getCityList().get(c).getArea().size() == 0) {
                            City_AreaList.add("");
                        } else {

                            for (int d = 0; d < options1Items.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                                String AreaName = options1Items.get(i).getCityList().get(c).getArea().get(d);

                                City_AreaList.add(AreaName);//添加该城市所有地区数据
                            }
                        }
                        Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                    }
                }

                /**
                 * 添加城市数据
                 */
                options2Items.add(CityList);

                /**
                 * 添加地区数据
                 */
                options3Items.add(Province_AreaList);
            }
        }
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<JsonBean>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    public String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    private static String checkText(String text) {
        return TextUtils.isEmpty(text) ? "" : text;
    }
    public interface onSelectedListener {
        void onSelected(String province,String city,String country);
    }
}
