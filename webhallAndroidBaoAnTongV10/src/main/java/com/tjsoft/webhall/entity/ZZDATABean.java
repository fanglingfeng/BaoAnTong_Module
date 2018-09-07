package com.tjsoft.webhall.entity;

/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.entity
 *  @文件名:   DZZZKSendbean
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 17:21
 *  @描述：    TODO
 */

public class ZZDATABean {


        /**
         * AUTHCODE : 20180607092113173NC0252519_44030020180008T6J2
         * NAME : 深圳经济特区居住证
         * IDCODE : 445222199903040633
         * LICENSECODE : 44030020180008T6J2
         */

        private String AUTHCODE;
        private String NAME;
        private String IDCODE;
        private String LICENSECODE;

        public boolean isIsopen() {
                return isopen;
        }

        public void setIsopen(boolean isopen) {
                this.isopen = isopen;
        }

        private boolean isopen;

        public String getAUTHCODE() { return AUTHCODE;}

        public void setAUTHCODE(String AUTHCODE) { this.AUTHCODE = AUTHCODE;}

        public String getNAME() { return NAME;}

        public void setNAME(String NAME) { this.NAME = NAME;}

        public String getIDCODE() { return IDCODE;}

        public void setIDCODE(String IDCODE) { this.IDCODE = IDCODE;}

        public String getLICENSECODE() { return LICENSECODE;}

        public void setLICENSECODE(String LICENSECODE) { this.LICENSECODE = LICENSECODE;}

}
