package org.apollo.mail.orm.entity.gzhs.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GjGzhsMailIdsCheckEntityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GjGzhsMailIdsCheckEntityExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("ID like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("ID not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidIsNull() {
            addCriterion("VC_MAX_MAILID is null");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidIsNotNull() {
            addCriterion("VC_MAX_MAILID is not null");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidEqualTo(String value) {
            addCriterion("VC_MAX_MAILID =", value, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidNotEqualTo(String value) {
            addCriterion("VC_MAX_MAILID <>", value, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidGreaterThan(String value) {
            addCriterion("VC_MAX_MAILID >", value, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidGreaterThanOrEqualTo(String value) {
            addCriterion("VC_MAX_MAILID >=", value, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidLessThan(String value) {
            addCriterion("VC_MAX_MAILID <", value, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidLessThanOrEqualTo(String value) {
            addCriterion("VC_MAX_MAILID <=", value, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidLike(String value) {
            addCriterion("VC_MAX_MAILID like", value, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidNotLike(String value) {
            addCriterion("VC_MAX_MAILID not like", value, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidIn(List<String> values) {
            addCriterion("VC_MAX_MAILID in", values, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidNotIn(List<String> values) {
            addCriterion("VC_MAX_MAILID not in", values, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidBetween(String value1, String value2) {
            addCriterion("VC_MAX_MAILID between", value1, value2, "vcMaxMailid");
            return (Criteria) this;
        }

        public Criteria andVcMaxMailidNotBetween(String value1, String value2) {
            addCriterion("VC_MAX_MAILID not between", value1, value2, "vcMaxMailid");
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