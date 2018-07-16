package com.firefly.pojo;

import javax.persistence.*;

public class Workitem {
    @Id
    @Column(name = "questionId")
    private String questionid;

    @Column(name = "userName")
    private String username;

    @Column(name = "questionName")
    private String questionname;

    private String description;

    private String status;

    private String performer;

    @Column(name = "createdTime")
    private String createdtime;

    @Column(name = "updatedTime")
    private String updatedtime;

    @Column(name = "evaluateScore")
    private String evaluatescore;

    @Column(name = "evaluateDetail")
    private String evaluatedetail;

    @Column(name = "workItemType")
    private String workitemtype;

    private String time;

    /**
     * @return questionId
     */
    public String getQuestionid() {
        return questionid;
    }

    /**
     * @param questionid
     */
    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }

    /**
     * @return userName
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return questionName
     */
    public String getQuestionname() {
        return questionname;
    }

    /**
     * @param questionname
     */
    public void setQuestionname(String questionname) {
        this.questionname = questionname;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return performer
     */
    public String getPerformer() {
        return performer;
    }

    /**
     * @param performer
     */
    public void setPerformer(String performer) {
        this.performer = performer;
    }

    /**
     * @return createdTime
     */
    public String getCreatedtime() {
        return createdtime;
    }

    /**
     * @param createdtime
     */
    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    /**
     * @return updatedTime
     */
    public String getUpdatedtime() {
        return updatedtime;
    }

    /**
     * @param updatedtime
     */
    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }

    /**
     * @return evaluateScore
     */
    public String getEvaluatescore() {
        return evaluatescore;
    }

    /**
     * @param evaluatescore
     */
    public void setEvaluatescore(String evaluatescore) {
        this.evaluatescore = evaluatescore;
    }

    /**
     * @return evaluateDetail
     */
    public String getEvaluatedetail() {
        return evaluatedetail;
    }

    /**
     * @param evaluatedetail
     */
    public void setEvaluatedetail(String evaluatedetail) {
        this.evaluatedetail = evaluatedetail;
    }

    /**
     * @return workItemType
     */
    public String getWorkitemtype() {
        return workitemtype;
    }

    /**
     * @param workitemtype
     */
    public void setWorkitemtype(String workitemtype) {
        this.workitemtype = workitemtype;
    }

    /**
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }
}