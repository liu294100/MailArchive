package org.apollo.mail.orm.entity.gzhs.mail;

import java.util.Date;

public class GjGzhsChkMailEntity {
    private String vcId;

    private String vcMailuid;

    private String vcMailsubject;

    private String vcSffj;

    private String vcPath;

    private String vcMailremark;

    private Date vcSenddate;

    private String vcPfrom;

    private String vcCc;

    private String vcSjr;

    private String vcPfromname;

    private String vcRecvym;

    private String vcRecvd;

    private String vcRecvh;

    private Date vcReceivedate;

    private Date updatetime;

    private String vcMailtype;

    public String getVcId() {
        return vcId;
    }

    public void setVcId(String vcId) {
        this.vcId = vcId == null ? null : vcId.trim();
    }

    public String getVcMailuid() {
        return vcMailuid;
    }

    public void setVcMailuid(String vcMailuid) {
        this.vcMailuid = vcMailuid == null ? null : vcMailuid.trim();
    }

    public String getVcMailsubject() {
        return vcMailsubject;
    }

    public void setVcMailsubject(String vcMailsubject) {
        this.vcMailsubject = vcMailsubject == null ? null : vcMailsubject.trim();
    }

    public String getVcSffj() {
        return vcSffj;
    }

    public void setVcSffj(String vcSffj) {
        this.vcSffj = vcSffj == null ? null : vcSffj.trim();
    }

    public String getVcPath() {
        return vcPath;
    }

    public void setVcPath(String vcPath) {
        this.vcPath = vcPath == null ? null : vcPath.trim();
    }

    public String getVcMailremark() {
        return vcMailremark;
    }

    public void setVcMailremark(String vcMailremark) {
        this.vcMailremark = vcMailremark == null ? null : vcMailremark.trim();
    }

    public Date getVcSenddate() {
        return vcSenddate;
    }

    public void setVcSenddate(Date vcSenddate) {
        this.vcSenddate = vcSenddate;
    }

    public String getVcPfrom() {
        return vcPfrom;
    }

    public void setVcPfrom(String vcPfrom) {
        this.vcPfrom = vcPfrom == null ? null : vcPfrom.trim();
    }

    public String getVcCc() {
        return vcCc;
    }

    public void setVcCc(String vcCc) {
        this.vcCc = vcCc == null ? null : vcCc.trim();
    }

    public String getVcSjr() {
        return vcSjr;
    }

    public void setVcSjr(String vcSjr) {
        this.vcSjr = vcSjr == null ? null : vcSjr.trim();
    }

    public String getVcPfromname() {
        return vcPfromname;
    }

    public void setVcPfromname(String vcPfromname) {
        this.vcPfromname = vcPfromname == null ? null : vcPfromname.trim();
    }

    public String getVcRecvym() {
        return vcRecvym;
    }

    public void setVcRecvym(String vcRecvym) {
        this.vcRecvym = vcRecvym == null ? null : vcRecvym.trim();
    }

    public String getVcRecvd() {
        return vcRecvd;
    }

    public void setVcRecvd(String vcRecvd) {
        this.vcRecvd = vcRecvd == null ? null : vcRecvd.trim();
    }

    public String getVcRecvh() {
        return vcRecvh;
    }

    public void setVcRecvh(String vcRecvh) {
        this.vcRecvh = vcRecvh == null ? null : vcRecvh.trim();
    }

    public Date getVcReceivedate() {
        return vcReceivedate;
    }

    public void setVcReceivedate(Date vcReceivedate) {
        this.vcReceivedate = vcReceivedate;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getVcMailtype() {
        return vcMailtype;
    }

    public void setVcMailtype(String vcMailtype) {
        this.vcMailtype = vcMailtype == null ? null : vcMailtype.trim();
    }
}