package org.apollo.mail.orm.entity.gzhs.mail;

public class GjGzhsChkMailEntityWithBLOBs extends GjGzhsChkMailEntity {
    private String vcMailcontent;

    private String vcFj;

    public String getVcMailcontent() {
        return vcMailcontent;
    }

    public void setVcMailcontent(String vcMailcontent) {
        this.vcMailcontent = vcMailcontent == null ? null : vcMailcontent.trim();
    }

    public String getVcFj() {
        return vcFj;
    }

    public void setVcFj(String vcFj) {
        this.vcFj = vcFj == null ? null : vcFj.trim();
    }
}