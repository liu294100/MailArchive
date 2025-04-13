package org.apollo.mail.orm.entity.gzhs.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GjGzhsChkMailEntityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GjGzhsChkMailEntityExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andVcIdIsNull() {
            addCriterion("VC_ID is null");
            return (Criteria) this;
        }

        public Criteria andVcIdIsNotNull() {
            addCriterion("VC_ID is not null");
            return (Criteria) this;
        }

        public Criteria andVcIdEqualTo(String value) {
            addCriterion("VC_ID =", value, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdNotEqualTo(String value) {
            addCriterion("VC_ID <>", value, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdGreaterThan(String value) {
            addCriterion("VC_ID >", value, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdGreaterThanOrEqualTo(String value) {
            addCriterion("VC_ID >=", value, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdLessThan(String value) {
            addCriterion("VC_ID <", value, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdLessThanOrEqualTo(String value) {
            addCriterion("VC_ID <=", value, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdLike(String value) {
            addCriterion("VC_ID like", value, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdNotLike(String value) {
            addCriterion("VC_ID not like", value, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdIn(List<String> values) {
            addCriterion("VC_ID in", values, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdNotIn(List<String> values) {
            addCriterion("VC_ID not in", values, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdBetween(String value1, String value2) {
            addCriterion("VC_ID between", value1, value2, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcIdNotBetween(String value1, String value2) {
            addCriterion("VC_ID not between", value1, value2, "vcId");
            return (Criteria) this;
        }

        public Criteria andVcMailuidIsNull() {
            addCriterion("VC_MAILUID is null");
            return (Criteria) this;
        }

        public Criteria andVcMailuidIsNotNull() {
            addCriterion("VC_MAILUID is not null");
            return (Criteria) this;
        }

        public Criteria andVcMailuidEqualTo(String value) {
            addCriterion("VC_MAILUID =", value, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidNotEqualTo(String value) {
            addCriterion("VC_MAILUID <>", value, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidGreaterThan(String value) {
            addCriterion("VC_MAILUID >", value, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidGreaterThanOrEqualTo(String value) {
            addCriterion("VC_MAILUID >=", value, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidLessThan(String value) {
            addCriterion("VC_MAILUID <", value, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidLessThanOrEqualTo(String value) {
            addCriterion("VC_MAILUID <=", value, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidLike(String value) {
            addCriterion("VC_MAILUID like", value, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidNotLike(String value) {
            addCriterion("VC_MAILUID not like", value, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidIn(List<String> values) {
            addCriterion("VC_MAILUID in", values, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidNotIn(List<String> values) {
            addCriterion("VC_MAILUID not in", values, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidBetween(String value1, String value2) {
            addCriterion("VC_MAILUID between", value1, value2, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailuidNotBetween(String value1, String value2) {
            addCriterion("VC_MAILUID not between", value1, value2, "vcMailuid");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectIsNull() {
            addCriterion("VC_MAILSUBJECT is null");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectIsNotNull() {
            addCriterion("VC_MAILSUBJECT is not null");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectEqualTo(String value) {
            addCriterion("VC_MAILSUBJECT =", value, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectNotEqualTo(String value) {
            addCriterion("VC_MAILSUBJECT <>", value, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectGreaterThan(String value) {
            addCriterion("VC_MAILSUBJECT >", value, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectGreaterThanOrEqualTo(String value) {
            addCriterion("VC_MAILSUBJECT >=", value, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectLessThan(String value) {
            addCriterion("VC_MAILSUBJECT <", value, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectLessThanOrEqualTo(String value) {
            addCriterion("VC_MAILSUBJECT <=", value, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectLike(String value) {
            addCriterion("VC_MAILSUBJECT like", value, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectNotLike(String value) {
            addCriterion("VC_MAILSUBJECT not like", value, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectIn(List<String> values) {
            addCriterion("VC_MAILSUBJECT in", values, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectNotIn(List<String> values) {
            addCriterion("VC_MAILSUBJECT not in", values, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectBetween(String value1, String value2) {
            addCriterion("VC_MAILSUBJECT between", value1, value2, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectNotBetween(String value1, String value2) {
            addCriterion("VC_MAILSUBJECT not between", value1, value2, "vcMailsubject");
            return (Criteria) this;
        }

        public Criteria andVcSffjIsNull() {
            addCriterion("VC_SFFJ is null");
            return (Criteria) this;
        }

        public Criteria andVcSffjIsNotNull() {
            addCriterion("VC_SFFJ is not null");
            return (Criteria) this;
        }

        public Criteria andVcSffjEqualTo(String value) {
            addCriterion("VC_SFFJ =", value, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjNotEqualTo(String value) {
            addCriterion("VC_SFFJ <>", value, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjGreaterThan(String value) {
            addCriterion("VC_SFFJ >", value, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjGreaterThanOrEqualTo(String value) {
            addCriterion("VC_SFFJ >=", value, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjLessThan(String value) {
            addCriterion("VC_SFFJ <", value, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjLessThanOrEqualTo(String value) {
            addCriterion("VC_SFFJ <=", value, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjLike(String value) {
            addCriterion("VC_SFFJ like", value, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjNotLike(String value) {
            addCriterion("VC_SFFJ not like", value, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjIn(List<String> values) {
            addCriterion("VC_SFFJ in", values, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjNotIn(List<String> values) {
            addCriterion("VC_SFFJ not in", values, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjBetween(String value1, String value2) {
            addCriterion("VC_SFFJ between", value1, value2, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcSffjNotBetween(String value1, String value2) {
            addCriterion("VC_SFFJ not between", value1, value2, "vcSffj");
            return (Criteria) this;
        }

        public Criteria andVcPathIsNull() {
            addCriterion("VC_PATH is null");
            return (Criteria) this;
        }

        public Criteria andVcPathIsNotNull() {
            addCriterion("VC_PATH is not null");
            return (Criteria) this;
        }

        public Criteria andVcPathEqualTo(String value) {
            addCriterion("VC_PATH =", value, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathNotEqualTo(String value) {
            addCriterion("VC_PATH <>", value, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathGreaterThan(String value) {
            addCriterion("VC_PATH >", value, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathGreaterThanOrEqualTo(String value) {
            addCriterion("VC_PATH >=", value, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathLessThan(String value) {
            addCriterion("VC_PATH <", value, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathLessThanOrEqualTo(String value) {
            addCriterion("VC_PATH <=", value, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathLike(String value) {
            addCriterion("VC_PATH like", value, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathNotLike(String value) {
            addCriterion("VC_PATH not like", value, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathIn(List<String> values) {
            addCriterion("VC_PATH in", values, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathNotIn(List<String> values) {
            addCriterion("VC_PATH not in", values, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathBetween(String value1, String value2) {
            addCriterion("VC_PATH between", value1, value2, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcPathNotBetween(String value1, String value2) {
            addCriterion("VC_PATH not between", value1, value2, "vcPath");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkIsNull() {
            addCriterion("VC_MAILREMARK is null");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkIsNotNull() {
            addCriterion("VC_MAILREMARK is not null");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkEqualTo(String value) {
            addCriterion("VC_MAILREMARK =", value, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkNotEqualTo(String value) {
            addCriterion("VC_MAILREMARK <>", value, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkGreaterThan(String value) {
            addCriterion("VC_MAILREMARK >", value, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkGreaterThanOrEqualTo(String value) {
            addCriterion("VC_MAILREMARK >=", value, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkLessThan(String value) {
            addCriterion("VC_MAILREMARK <", value, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkLessThanOrEqualTo(String value) {
            addCriterion("VC_MAILREMARK <=", value, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkLike(String value) {
            addCriterion("VC_MAILREMARK like", value, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkNotLike(String value) {
            addCriterion("VC_MAILREMARK not like", value, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkIn(List<String> values) {
            addCriterion("VC_MAILREMARK in", values, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkNotIn(List<String> values) {
            addCriterion("VC_MAILREMARK not in", values, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkBetween(String value1, String value2) {
            addCriterion("VC_MAILREMARK between", value1, value2, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcMailremarkNotBetween(String value1, String value2) {
            addCriterion("VC_MAILREMARK not between", value1, value2, "vcMailremark");
            return (Criteria) this;
        }

        public Criteria andVcSenddateIsNull() {
            addCriterion("VC_SENDDATE is null");
            return (Criteria) this;
        }

        public Criteria andVcSenddateIsNotNull() {
            addCriterion("VC_SENDDATE is not null");
            return (Criteria) this;
        }

        public Criteria andVcSenddateEqualTo(Date value) {
            addCriterion("VC_SENDDATE =", value, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateNotEqualTo(Date value) {
            addCriterion("VC_SENDDATE <>", value, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateGreaterThan(Date value) {
            addCriterion("VC_SENDDATE >", value, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateGreaterThanOrEqualTo(Date value) {
            addCriterion("VC_SENDDATE >=", value, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateLessThan(Date value) {
            addCriterion("VC_SENDDATE <", value, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateLessThanOrEqualTo(Date value) {
            addCriterion("VC_SENDDATE <=", value, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateIn(List<Date> values) {
            addCriterion("VC_SENDDATE in", values, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateNotIn(List<Date> values) {
            addCriterion("VC_SENDDATE not in", values, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateBetween(Date value1, Date value2) {
            addCriterion("VC_SENDDATE between", value1, value2, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcSenddateNotBetween(Date value1, Date value2) {
            addCriterion("VC_SENDDATE not between", value1, value2, "vcSenddate");
            return (Criteria) this;
        }

        public Criteria andVcPfromIsNull() {
            addCriterion("VC_PFROM is null");
            return (Criteria) this;
        }

        public Criteria andVcPfromIsNotNull() {
            addCriterion("VC_PFROM is not null");
            return (Criteria) this;
        }

        public Criteria andVcPfromEqualTo(String value) {
            addCriterion("VC_PFROM =", value, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromNotEqualTo(String value) {
            addCriterion("VC_PFROM <>", value, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromGreaterThan(String value) {
            addCriterion("VC_PFROM >", value, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromGreaterThanOrEqualTo(String value) {
            addCriterion("VC_PFROM >=", value, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromLessThan(String value) {
            addCriterion("VC_PFROM <", value, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromLessThanOrEqualTo(String value) {
            addCriterion("VC_PFROM <=", value, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromLike(String value) {
            addCriterion("VC_PFROM like", value, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromNotLike(String value) {
            addCriterion("VC_PFROM not like", value, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromIn(List<String> values) {
            addCriterion("VC_PFROM in", values, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromNotIn(List<String> values) {
            addCriterion("VC_PFROM not in", values, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromBetween(String value1, String value2) {
            addCriterion("VC_PFROM between", value1, value2, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcPfromNotBetween(String value1, String value2) {
            addCriterion("VC_PFROM not between", value1, value2, "vcPfrom");
            return (Criteria) this;
        }

        public Criteria andVcCcIsNull() {
            addCriterion("VC_CC is null");
            return (Criteria) this;
        }

        public Criteria andVcCcIsNotNull() {
            addCriterion("VC_CC is not null");
            return (Criteria) this;
        }

        public Criteria andVcCcEqualTo(String value) {
            addCriterion("VC_CC =", value, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcNotEqualTo(String value) {
            addCriterion("VC_CC <>", value, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcGreaterThan(String value) {
            addCriterion("VC_CC >", value, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcGreaterThanOrEqualTo(String value) {
            addCriterion("VC_CC >=", value, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcLessThan(String value) {
            addCriterion("VC_CC <", value, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcLessThanOrEqualTo(String value) {
            addCriterion("VC_CC <=", value, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcLike(String value) {
            addCriterion("VC_CC like", value, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcNotLike(String value) {
            addCriterion("VC_CC not like", value, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcIn(List<String> values) {
            addCriterion("VC_CC in", values, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcNotIn(List<String> values) {
            addCriterion("VC_CC not in", values, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcBetween(String value1, String value2) {
            addCriterion("VC_CC between", value1, value2, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcCcNotBetween(String value1, String value2) {
            addCriterion("VC_CC not between", value1, value2, "vcCc");
            return (Criteria) this;
        }

        public Criteria andVcSjrIsNull() {
            addCriterion("VC_SJR is null");
            return (Criteria) this;
        }

        public Criteria andVcSjrIsNotNull() {
            addCriterion("VC_SJR is not null");
            return (Criteria) this;
        }

        public Criteria andVcSjrEqualTo(String value) {
            addCriterion("VC_SJR =", value, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrNotEqualTo(String value) {
            addCriterion("VC_SJR <>", value, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrGreaterThan(String value) {
            addCriterion("VC_SJR >", value, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrGreaterThanOrEqualTo(String value) {
            addCriterion("VC_SJR >=", value, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrLessThan(String value) {
            addCriterion("VC_SJR <", value, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrLessThanOrEqualTo(String value) {
            addCriterion("VC_SJR <=", value, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrLike(String value) {
            addCriterion("VC_SJR like", value, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrNotLike(String value) {
            addCriterion("VC_SJR not like", value, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrIn(List<String> values) {
            addCriterion("VC_SJR in", values, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrNotIn(List<String> values) {
            addCriterion("VC_SJR not in", values, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrBetween(String value1, String value2) {
            addCriterion("VC_SJR between", value1, value2, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcSjrNotBetween(String value1, String value2) {
            addCriterion("VC_SJR not between", value1, value2, "vcSjr");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameIsNull() {
            addCriterion("VC_PFROMNAME is null");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameIsNotNull() {
            addCriterion("VC_PFROMNAME is not null");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameEqualTo(String value) {
            addCriterion("VC_PFROMNAME =", value, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameNotEqualTo(String value) {
            addCriterion("VC_PFROMNAME <>", value, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameGreaterThan(String value) {
            addCriterion("VC_PFROMNAME >", value, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameGreaterThanOrEqualTo(String value) {
            addCriterion("VC_PFROMNAME >=", value, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameLessThan(String value) {
            addCriterion("VC_PFROMNAME <", value, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameLessThanOrEqualTo(String value) {
            addCriterion("VC_PFROMNAME <=", value, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameLike(String value) {
            addCriterion("VC_PFROMNAME like", value, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameNotLike(String value) {
            addCriterion("VC_PFROMNAME not like", value, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameIn(List<String> values) {
            addCriterion("VC_PFROMNAME in", values, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameNotIn(List<String> values) {
            addCriterion("VC_PFROMNAME not in", values, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameBetween(String value1, String value2) {
            addCriterion("VC_PFROMNAME between", value1, value2, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcPfromnameNotBetween(String value1, String value2) {
            addCriterion("VC_PFROMNAME not between", value1, value2, "vcPfromname");
            return (Criteria) this;
        }

        public Criteria andVcRecvymIsNull() {
            addCriterion("VC_RECVYM is null");
            return (Criteria) this;
        }

        public Criteria andVcRecvymIsNotNull() {
            addCriterion("VC_RECVYM is not null");
            return (Criteria) this;
        }

        public Criteria andVcRecvymEqualTo(String value) {
            addCriterion("VC_RECVYM =", value, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymNotEqualTo(String value) {
            addCriterion("VC_RECVYM <>", value, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymGreaterThan(String value) {
            addCriterion("VC_RECVYM >", value, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymGreaterThanOrEqualTo(String value) {
            addCriterion("VC_RECVYM >=", value, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymLessThan(String value) {
            addCriterion("VC_RECVYM <", value, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymLessThanOrEqualTo(String value) {
            addCriterion("VC_RECVYM <=", value, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymLike(String value) {
            addCriterion("VC_RECVYM like", value, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymNotLike(String value) {
            addCriterion("VC_RECVYM not like", value, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymIn(List<String> values) {
            addCriterion("VC_RECVYM in", values, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymNotIn(List<String> values) {
            addCriterion("VC_RECVYM not in", values, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymBetween(String value1, String value2) {
            addCriterion("VC_RECVYM between", value1, value2, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvymNotBetween(String value1, String value2) {
            addCriterion("VC_RECVYM not between", value1, value2, "vcRecvym");
            return (Criteria) this;
        }

        public Criteria andVcRecvdIsNull() {
            addCriterion("VC_RECVD is null");
            return (Criteria) this;
        }

        public Criteria andVcRecvdIsNotNull() {
            addCriterion("VC_RECVD is not null");
            return (Criteria) this;
        }

        public Criteria andVcRecvdEqualTo(String value) {
            addCriterion("VC_RECVD =", value, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdNotEqualTo(String value) {
            addCriterion("VC_RECVD <>", value, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdGreaterThan(String value) {
            addCriterion("VC_RECVD >", value, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdGreaterThanOrEqualTo(String value) {
            addCriterion("VC_RECVD >=", value, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdLessThan(String value) {
            addCriterion("VC_RECVD <", value, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdLessThanOrEqualTo(String value) {
            addCriterion("VC_RECVD <=", value, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdLike(String value) {
            addCriterion("VC_RECVD like", value, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdNotLike(String value) {
            addCriterion("VC_RECVD not like", value, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdIn(List<String> values) {
            addCriterion("VC_RECVD in", values, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdNotIn(List<String> values) {
            addCriterion("VC_RECVD not in", values, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdBetween(String value1, String value2) {
            addCriterion("VC_RECVD between", value1, value2, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvdNotBetween(String value1, String value2) {
            addCriterion("VC_RECVD not between", value1, value2, "vcRecvd");
            return (Criteria) this;
        }

        public Criteria andVcRecvhIsNull() {
            addCriterion("VC_RECVH is null");
            return (Criteria) this;
        }

        public Criteria andVcRecvhIsNotNull() {
            addCriterion("VC_RECVH is not null");
            return (Criteria) this;
        }

        public Criteria andVcRecvhEqualTo(String value) {
            addCriterion("VC_RECVH =", value, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhNotEqualTo(String value) {
            addCriterion("VC_RECVH <>", value, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhGreaterThan(String value) {
            addCriterion("VC_RECVH >", value, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhGreaterThanOrEqualTo(String value) {
            addCriterion("VC_RECVH >=", value, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhLessThan(String value) {
            addCriterion("VC_RECVH <", value, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhLessThanOrEqualTo(String value) {
            addCriterion("VC_RECVH <=", value, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhLike(String value) {
            addCriterion("VC_RECVH like", value, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhNotLike(String value) {
            addCriterion("VC_RECVH not like", value, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhIn(List<String> values) {
            addCriterion("VC_RECVH in", values, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhNotIn(List<String> values) {
            addCriterion("VC_RECVH not in", values, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhBetween(String value1, String value2) {
            addCriterion("VC_RECVH between", value1, value2, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcRecvhNotBetween(String value1, String value2) {
            addCriterion("VC_RECVH not between", value1, value2, "vcRecvh");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateIsNull() {
            addCriterion("VC_RECEIVEDATE is null");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateIsNotNull() {
            addCriterion("VC_RECEIVEDATE is not null");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateEqualTo(Date value) {
            addCriterion("VC_RECEIVEDATE =", value, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateNotEqualTo(Date value) {
            addCriterion("VC_RECEIVEDATE <>", value, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateGreaterThan(Date value) {
            addCriterion("VC_RECEIVEDATE >", value, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateGreaterThanOrEqualTo(Date value) {
            addCriterion("VC_RECEIVEDATE >=", value, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateLessThan(Date value) {
            addCriterion("VC_RECEIVEDATE <", value, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateLessThanOrEqualTo(Date value) {
            addCriterion("VC_RECEIVEDATE <=", value, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateIn(List<Date> values) {
            addCriterion("VC_RECEIVEDATE in", values, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateNotIn(List<Date> values) {
            addCriterion("VC_RECEIVEDATE not in", values, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateBetween(Date value1, Date value2) {
            addCriterion("VC_RECEIVEDATE between", value1, value2, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andVcReceivedateNotBetween(Date value1, Date value2) {
            addCriterion("VC_RECEIVEDATE not between", value1, value2, "vcReceivedate");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNull() {
            addCriterion("UPDATETIME is null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIsNotNull() {
            addCriterion("UPDATETIME is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeEqualTo(Date value) {
            addCriterion("UPDATETIME =", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotEqualTo(Date value) {
            addCriterion("UPDATETIME <>", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThan(Date value) {
            addCriterion("UPDATETIME >", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("UPDATETIME >=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThan(Date value) {
            addCriterion("UPDATETIME <", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeLessThanOrEqualTo(Date value) {
            addCriterion("UPDATETIME <=", value, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeIn(List<Date> values) {
            addCriterion("UPDATETIME in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotIn(List<Date> values) {
            addCriterion("UPDATETIME not in", values, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeBetween(Date value1, Date value2) {
            addCriterion("UPDATETIME between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andUpdatetimeNotBetween(Date value1, Date value2) {
            addCriterion("UPDATETIME not between", value1, value2, "updatetime");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeIsNull() {
            addCriterion("VC_MAILTYPE is null");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeIsNotNull() {
            addCriterion("VC_MAILTYPE is not null");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeEqualTo(String value) {
            addCriterion("VC_MAILTYPE =", value, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeNotEqualTo(String value) {
            addCriterion("VC_MAILTYPE <>", value, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeGreaterThan(String value) {
            addCriterion("VC_MAILTYPE >", value, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeGreaterThanOrEqualTo(String value) {
            addCriterion("VC_MAILTYPE >=", value, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailsubjectInstrlt0(String value) {
            //instr(a.vc_mailsubject ,'关于开放日')>0
            addCriterion(" instr(VC_MAILSUBJECT ,'"+value+"')>0 ");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeLessThan(String value) {
            addCriterion("VC_MAILTYPE <", value, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeLessThanOrEqualTo(String value) {
            addCriterion("VC_MAILTYPE <=", value, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeLike(String value) {
            addCriterion("VC_MAILTYPE like", value, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeNotLike(String value) {
            addCriterion("VC_MAILTYPE not like", value, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeIn(List<String> values) {
            addCriterion("VC_MAILTYPE in", values, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeNotIn(List<String> values) {
            addCriterion("VC_MAILTYPE not in", values, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeBetween(String value1, String value2) {
            addCriterion("VC_MAILTYPE between", value1, value2, "vcMailtype");
            return (Criteria) this;
        }

        public Criteria andVcMailtypeNotBetween(String value1, String value2) {
            addCriterion("VC_MAILTYPE not between", value1, value2, "vcMailtype");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}