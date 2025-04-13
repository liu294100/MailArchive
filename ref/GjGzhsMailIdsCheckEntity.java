package org.apollo.mail.orm.entity.gzhs.mail;

import java.util.Date;

public class GjGzhsMailIdsCheckEntity {
    private String id;

    private String vcMaxMailid;

    private Date updatetime;

    private String vcMailtype;

    private String vcMailids;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getVcMaxMailid() {
        return vcMaxMailid;
    }

    public void setVcMaxMailid(String vcMaxMailid) {
        this.vcMaxMailid = vcMaxMailid == null ? null : vcMaxMailid.trim();
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

    public String getVcMailids() {
        return vcMailids;
    }

    public void setVcMailids(String vcMailids) {
        this.vcMailids = vcMailids == null ? null : vcMailids.trim();
    }
}