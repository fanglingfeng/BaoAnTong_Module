package com.tjsoft.webhall.entity;

/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.entity
 *  @文件名:   DZZZKSendbean
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 17:21
 *  @描述：    TODO
 */

import java.util.List;

public class CertInfoBean {


    /**
     * certfile : [{"fileid":"201808182018161013","CERTFILENAME":"材料_1.jpg","ATTRACHPATH":"MONGO"},{"fileid":"201808182018161014","CERTFILENAME":"材料_1.jpg","ATTRACHPATH":"MONGO"}]
     * dataxml : PHp6bnI+CiAgPGhlYWQvPgogIDxib2R5PgogICAgPFpaTUM+CiAgICAgIDx2YWx1ZT48IVtDREFU
     QVvmsJHlip7pnZ7kvIHkuJrljZXkvY3nmbvorrDor4HkuaZdXT48L3ZhbHVlPgogICAgICA8dGl0
     bGU+6K+B54Wn5ZCN56ewPC90aXRsZT4KICAgIDwvWlpNQz4KICAgIDxaWkhNPgogICAgICA8dmFs
     dWU+PCFbQ0RBVEFbNTI0NDAzMDZNSkwxNzc3MDBRXV0+PC92YWx1ZT4KICAgICAgPHRpdGxlPuiv
     geeFp+WPt+eggTwvdGl0bGU+CiAgICA8L1paSE0+CiAgICA8Q1lSTUM+CiAgICAgIDx2YWx1ZT48
     IVtDREFUQVvmt7HlnLPluILlrp3lronljLrmsYfnvo7npL7kvJrlt6XkvZzmnI3liqHkuK3lv4Nd
     XT48L3ZhbHVlPgogICAgICA8dGl0bGU+5oyB5pyJ5Lq65ZCN56ewPC90aXRsZT4KICAgIDwvQ1lS
     TUM+CiAgICA8Q1lSU0ZaSkhNPgogICAgICA8dmFsdWU+PCFbQ0RBVEFbMTQyNzAxMTk4NDA2MTg0
     MjJYXV0+PC92YWx1ZT4KICAgICAgPHRpdGxlPuaMgeacieS6uui6q+S7veivgeS7tuWPt+eggTwv
     dGl0bGU+CiAgICA8L0NZUlNGWkpITT4KICAgIDxDWVJTRlpKTFg+CiAgICAgIDx2YWx1ZT48IVtD
     REFUQVsxMF1dPjwvdmFsdWU+CiAgICAgIDx0aXRsZT7mjIHmnInkurrouqvku73or4Hku7bnsbvl
     nos8L3RpdGxlPgogICAgPC9DWVJTRlpKTFg+CiAgICA8RlpKR01DPgogICAgICA8dmFsdWU+PCFb
     Q0RBVEFb5a6d5a6J5Yy65rCR5pS/5bGAXV0+PC92YWx1ZT4KICAgICAgPHRpdGxlPuWPkeivgeac
     uuaehOWQjeensDwvdGl0bGU+CiAgICA8L0ZaSkdNQz4KICAgIDxGWkpHWlpKR0RNPgogICAgICA8
     dmFsdWU+PCFbQ0RBVEFbMDA3NTUwNTMxXV0+PC92YWx1ZT4KICAgICAgPHRpdGxlPuWPkeivgeac
     uuaehOWUr+S4gOagh+ivhjwvdGl0bGU+CiAgICA8L0ZaSkdaWkpHRE0+CiAgICA8RlpKR1NTWFpR
     SERNPgogICAgICA8dmFsdWU+PCFbQ0RBVEFbNDQwMzA2XV0+PC92YWx1ZT4KICAgICAgPHRpdGxl
     PuWPkeivgeacuuaehOaJgOWxnuihjOaUv+WMuuWIkuS7o+eggTwvdGl0bGU+CiAgICA8L0ZaSkdT
     U1haUUhETT4KICAgIDxGWlJRPgogICAgICA8dmFsdWU+PCFbQ0RBVEFbMjAxOC0wOC0xM11dPjwv
     dmFsdWU+CiAgICAgIDx0aXRsZT7lj5Hor4Hml6XmnJ88L3RpdGxlPgogICAgPC9GWlJRPgogICAg
     PFlYUUpTUlE+CiAgICAgIDx2YWx1ZT48IVtDREFUQVsyMDIyLTA4LTEzXV0+PC92YWx1ZT4KICAg
     ICAgPHRpdGxlPuacieaViOacn+e7k+adn+aXpeacnzwvdGl0bGU+CiAgICA8L1lYUUpTUlE+CiAg
     ICA8S0JaSj4KICAgICAgPHZhbHVlPjwhW0NEQVRBWzEwLjAwMDAo5LiH5YWDKV1dPjwvdmFsdWU+
     CiAgICAgIDx0aXRsZT7lvIDlip7otYTph5E8L3RpdGxlPgogICAgPC9LQlpKPgogICAgPFlXRlc+
     CiAgICAgIDx2YWx1ZT48IVtDREFUQVvmj5DkvpvlpoflpbPjgIHlhL/nq6XjgIHpnZLlsJHlubTj
     gIHlrrbluq3nrYnpoobln5/nmoTkuJPkuJrljJbnpL7kvJrlt6XkvZzmnI3liqHvvJvnpL7kvJrl
     t6XkvZznnaPlr7zjgIHor4TkvLDmnI3liqHvvJvlvIDlsZXnpL7kvJrlt6XkvZzmnI3liqHnoJTn
     qbbjgIHlrqPkvKDlkozkuqTmtYHmtLvliqjvvJvlhbbku5bkuJrliqHnm7jlhbPnmoTlhaznm4rm
     nI3liqHjgIJdXT48L3ZhbHVlPgogICAgICA8dGl0bGU+5Lia5Yqh6IyD5Zu0PC90aXRsZT4KICAg
     IDwvWVdGVz4KICAgIDxZV1pHRFc+CiAgICAgIDx2YWx1ZT48IVtDREFUQVvmt7HlnLPluILlrp3l
     ronljLrmsJHmlL/lsYBdXT48L3ZhbHVlPgogICAgICA8dGl0bGU+5Lia5Yqh5Li7566h5Y2V5L2N
     PC90aXRsZT4KICAgIDwvWVdaR0RXPgogICAgPFlYUUtTUlE+CiAgICAgIDx2YWx1ZT48IVtDREFU
     QVsyMDExLTAxLTEwXV0+PC92YWx1ZT4KICAgICAgPHRpdGxlPuacieaViOacn+W8gOWni+aXpeac
     nzwvdGl0bGU+CiAgICA8L1lYUUtTUlE+CiAgICA8RlpSU0ZaSkhNPgogICAgICA8dmFsdWU+PCFb
     Q0RBVEFbMTQyNzAxMTk4NDA2MTg0MjJYXV0+PC92YWx1ZT4KICAgICAgPHRpdGxlPui0n+i0o+S6
     uui6q+S7veivgeS7tuWPt+eggTwvdGl0bGU+CiAgICA8L0ZaUlNGWkpITT4KICAgIDxGWlJTRlpK
     TFg+CiAgICAgIDx2YWx1ZT48IVtDREFUQVsxMF1dPjwvdmFsdWU+CiAgICAgIDx0aXRsZT7otJ/o
     tKPkurrouqvku73or4Hku7bnsbvlnos8L3RpdGxlPgogICAgPC9GWlJTRlpKTFg+CiAgPC9ib2R5
     Pgo8L3p6bnI+Cg==
     * certinfo : {"CERTCODE":"102004001","CERTNAME":"危险化学品经营许可证","EXAMPLEPATH1":"http://203.91.37.98:8083/u/certificates/picture/102004001/102004001_fb.jpg","EXAMPLEPATH2":"http://203.91.37.98:8083/u/certificates/picture/102004001/102004001_zb.jpg","STATUS":"1"}
     */

    private String dataxml;
    private CertinfoBean certinfo;
    private List<CertfileBean> certfile;

    public String getDataxml() {
        return dataxml;
    }

    public void setDataxml(String dataxml) {
        this.dataxml = dataxml;
    }

    public CertinfoBean getCertinfo() {
        return certinfo;
    }

    public void setCertinfo(CertinfoBean certinfo) {
        this.certinfo = certinfo;
    }

    public List<CertfileBean> getCertfile() {
        return certfile;
    }

    public void setCertfile(List<CertfileBean> certfile) {
        this.certfile = certfile;
    }

    public static class CertinfoBean {
        /**
         * CERTCODE : 102004001
         * CERTNAME : 危险化学品经营许可证
         * EXAMPLEPATH1 : http://203.91.37.98:8083/u/certificates/picture/102004001/102004001_fb.jpg
         * EXAMPLEPATH2 : http://203.91.37.98:8083/u/certificates/picture/102004001/102004001_zb.jpg
         * STATUS : 1
         */

        private String CERTCODE;
        private String CERTNAME;
        private String EXAMPLEPATH1;
        private String EXAMPLEPATH2;
        private String STATUS;

        public String getCERTCODE() {
            return CERTCODE;
        }

        public void setCERTCODE(String CERTCODE) {
            this.CERTCODE = CERTCODE;
        }

        public String getCERTNAME() {
            return CERTNAME;
        }

        public void setCERTNAME(String CERTNAME) {
            this.CERTNAME = CERTNAME;
        }

        public String getEXAMPLEPATH1() {
            return EXAMPLEPATH1;
        }

        public void setEXAMPLEPATH1(String EXAMPLEPATH1) {
            this.EXAMPLEPATH1 = EXAMPLEPATH1;
        }

        public String getEXAMPLEPATH2() {
            return EXAMPLEPATH2;
        }

        public void setEXAMPLEPATH2(String EXAMPLEPATH2) {
            this.EXAMPLEPATH2 = EXAMPLEPATH2;
        }

        public String getSTATUS() {
            return STATUS;
        }

        public void setSTATUS(String STATUS) {
            this.STATUS = STATUS;
        }
    }

    public static class CertfileBean {
        /**
         * fileid : 201808182018161013
         * CERTFILENAME : 材料_1.jpg
         * ATTRACHPATH : MONGO
         */

        private String fileid;
        private String CERTFILENAME;
        private String ATTRACHPATH;

        public String getFileid() {
            return fileid;
        }

        public void setFileid(String fileid) {
            this.fileid = fileid;
        }

        public String getCERTFILENAME() {
            return CERTFILENAME;
        }

        public void setCERTFILENAME(String CERTFILENAME) {
            this.CERTFILENAME = CERTFILENAME;
        }

        public String getATTRACHPATH() {
            return ATTRACHPATH;
        }

        public void setATTRACHPATH(String ATTRACHPATH) {
            this.ATTRACHPATH = ATTRACHPATH;
        }
    }
}
