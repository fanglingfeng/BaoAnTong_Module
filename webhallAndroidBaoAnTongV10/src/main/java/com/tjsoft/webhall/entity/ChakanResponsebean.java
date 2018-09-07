package com.tjsoft.webhall.entity;

/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.entity
 *  @文件名:   DZZZKSendbean
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 17:21
 *  @描述：    TODO
 */

public class ChakanResponsebean {


    /**
     * ZZDATA : {"FILEID":"6B7258E8-7FC5-4454-B20B-F25B7DFF3A63","FILENAME":"44030020170005P4V0.pdf"}
     * TYPE : 1 成功 0 失败
     */

    private ZZDATABean ZZDATA;
    private String TYPE;

    public ZZDATABean getZZDATA() { return ZZDATA;}

    public void setZZDATA(ZZDATABean ZZDATA) { this.ZZDATA = ZZDATA;}

    public String getTYPE() { return TYPE;}

    public void setTYPE(String TYPE) { this.TYPE = TYPE;}

    public static class ZZDATABean {
        /**
         * FILEID : 6B7258E8-7FC5-4454-B20B-F25B7DFF3A63
         * FILENAME : 44030020170005P4V0.pdf
         */

        private String FILEID;
        private String FILENAME;

        public String getFILEID() { return FILEID;}

        public void setFILEID(String FILEID) { this.FILEID = FILEID;}

        public String getFILENAME() { return FILENAME;}

        public void setFILENAME(String FILENAME) { this.FILENAME = FILENAME;}
    }
}
