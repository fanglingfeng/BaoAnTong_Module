package com.tjsoft.util;

import android.text.TextUtils;

import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.Table;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XMLUtil {

    private static String tagname;
    private static String myvalue;
    public static List<Table> getTables(String xml) {

        String id = "";
        String type = "";
        String name = "";
        String source = "";
        List<Table> tables = new ArrayList<Table>();
        String tag = "";
        String value = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xml));

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        value = "";
                        break;
                    case XmlPullParser.TEXT:
                        value = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("id")) {// 材料编号
                            id = value;
                        } else if (tag.equals("type")) {// 材料id
                            type = value;
                        } else if (tag.equals("name")) {
                            name = value;
                        } else if (tag.equals("source")) {
                            source = value;
                        } else if (tag.equals("file")) {
                            Table table = new Table(id, type, name, source);
                            tables.add(table);
                            id = "";
                            type = "";
                            name = "";
                            source = "";
                        }

                        break;

                }
                parserEvent = parser.next();

            }

        } catch (Exception e) {

        }

        return tables;

    }

    /**
     * 解析xml到map
     *
     * @param xml
     * @return
     */
    public static void material2Map(String xml) {

        String key = "";
        String ID = "";
        String ATTACHNAME = "";
        String ATTACHURL = "";
        String ATTACHUID = "";
        List<ATTBean> atts = new ArrayList<ATTBean>();
        String tag = "";
        String value = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xml));

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        value = "";//解决标签不成对出现，value的值不会更新
                        break;
                    case XmlPullParser.TEXT:
                        value = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("materialcode")) {// 材料编号
                            key = value;
                        } else if (tag.equals("fileid")) {// 材料id
                            ID = value;
                        } else if (tag.equals("filename")) {
                            ATTACHNAME = value;
                        } else if (tag.equals("filepath")) {
                            ATTACHURL = value;
                        } else if (tag.equals("id") && parser.getDepth() == 4) {
                            ATTACHUID = value;
                        } else if (tag.equals("file")) {
                            ATTBean att = new ATTBean(ID, ATTACHNAME, ATTACHURL, ATTACHUID);
                            atts.add(att);
                            ID = "";
                            ATTACHNAME = "";
                            ATTACHURL = "";
                            ATTACHUID = "";
                        } else if (tag.equals("materialinfo")) {
                            Constants.material.put(key, atts);
                            key = "";
                            atts = new ArrayList<ATTBean>();
                        }

                        break;

                }
                parserEvent = parser.next();

            }

        } catch (Exception e) {

        }

    }

    public static List<ApplyBean> parseMaterials(String xml) {
        String id = "";
        String materialid = "";
        String materialname = "";
        String materialcode = "";
        String submittype = "";
        String orinum = "";
        String copynum = "";
        String isneed = "";
        String status = "";
        String fileid = "";
        String filename = "";
        String filepath = "";
        String filedel = "";
        String sh = "";// 审核
        String shyj = "";// 审核意见
        List<ApplyBean> applyBeans = new ArrayList<ApplyBean>();

        String tag = "";
        String value = "";
        String certificateid = "";
        String certificatestartdate = "";
        String certificateenddate = "";
        String formid = "";
        String formver = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xml));

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        value = "";//解决标签不成对出现，value的值不会更新
                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("id")) {
                            id = value;
                        } else if (tag.equals("materialid")) {
                            materialid = value;
                        } else if (tag.equals("materialname")) {
                            materialname = value;
                        } else if (tag.equals("materialcode")) {
                            materialcode = value;
                        } else if (tag.equals("submittype")) {
                            submittype = value;
                        } else if (tag.equals("orinum")) {
                            orinum = value;
                        } else if (tag.equals("copynum")) {
                            copynum = value;
                        } else if (tag.equals("isneed")) {
                            isneed = value;
                        } else if (tag.equals("status")) {
                            status = value;
                        } else if (tag.equals("fileid")) {
                            fileid = value;
                        } else if (tag.equals("filename")) {
                            filename = value;
                        } else if (tag.equals("filepath")) {
                            filepath = value;
                        } else if (tag.equals("filedel")) {
                            filedel = value;
                        } else if (tag.equals("sh")) {
                            sh = value;
                        } else if (tag.equals("shyj")) {
                            shyj = value;
                        } else if (tag.equals("certificateid")) {
                            certificateid = value;
                        } else if (tag.equals("certificatestartdate")) {
                            certificatestartdate = value;
                        } else if (tag.equals("certificateenddate")) {
                            certificateenddate = value;
                        } else if (tag.equals("formid")) {
                            formid = value;
                        } else if (tag.equals("FORMVER ")) {
                            formver = value;
                        } else if (tag.equals("materialinfo")) {
                            ApplyBean applyBean = new ApplyBean(id, materialid, materialcode, materialname, submittype, orinum, copynum, isneed, status, fileid, filename, filepath, filedel, sh, shyj, certificateid, certificatestartdate, certificateenddate, formid, formver);
                            applyBeans.add(applyBean);
                            id = "";
                            materialid = "";
                            materialname = "";
                            materialcode = "";
                            submittype = "";
                            orinum = "";
                            copynum = "";
                            isneed = "";
                            status = "";
                            fileid = "";
                            filename = "";
                            filepath = "";
                            filedel = "";
                            sh = "";
                            shyj = "";
                            certificateid = "";
                            certificatestartdate = "";
                            certificateenddate = "";
                            formid = "";
                            formver = "";
                        }

                        break;

                    case XmlPullParser.TEXT:
                        value = parser.getText();
                        break;
                }
                parserEvent = parser.next();

            }

        } catch (Exception e) {

        }
        return applyBeans;

    }

    public static String toJsonForJS(String xml) {

        String tag = "";
        String value = "";
        JSONObject json = new JSONObject();
        JSONArray dataidArray = new JSONArray();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xml.toString().trim()));

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        break;

                    case XmlPullParser.END_TAG:

                        break;

                    case XmlPullParser.TEXT:
                        value = parser.getText().trim();
                        if (!TextUtils.isEmpty(value)) {//微信端的返回有回车和空格，暂时处理去掉回车和空格
                            if (tag.equals("dataid")) {
                                dataidArray.put(value);
                            } else {
                                json.put(tag, value);
                                value = "";
                            }
                        }
                        break;
                }
                parserEvent = parser.next();

            }

            json.put("dataid", dataidArray);
        } catch (Exception e) {

        }
        return json.toString();

    }

    public static String toJsonForJSXkz(String xml) {
        int i = 0;

        String tag = "";
        String value = "";
        String value1 = "";
        String value2 = "";
        JSONObject json = new JSONObject();
        JSONArray dataidArray = new JSONArray();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(xml.toString().trim()));

            int parserEvent = parser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                switch (parserEvent) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.END_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG:

                        if (!parser.getName().equals("value") && !parser.getName().equals("title")) {

                            tagname = parser.getName();
                            i = 0;
                        }


                        break;

                    case XmlPullParser.END_TAG:

                        break;

                    case XmlPullParser.TEXT:
                        i++;
                        if (i%2!=0 ) {
                            value1 = parser.getText().trim();
                        }else{
                            value2 = parser.getText().trim();
                            json.put(tagname, value1);

                        }
////                        if (!TextUtils.isEmpty(value)) {//微信端的返回有回车和空格，暂时处理去掉回车和空格
////						if (tag.equals("dataid")) {
////							dataidArray.put(value);
////						} else {
//                            if (i%2!=0) {
//                                if (TextUtils.equals(value,"  ")) {
//                                    json.put(tagname, "");
//
//                                } else {
//                                    json.put(tagname, value);
//
//                                }
//                            }
//                        if (i == 2) {
//
//                        }
                            value = "";
//                        }
//						}
//                        }
                        break;
                }
                parserEvent = parser.next();

            }

        } catch (Exception e) {

        }
        return json.toString();

    }

}
