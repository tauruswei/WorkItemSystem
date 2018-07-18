package com.firefly.pojo;

import javax.persistence.*;

public class Workitemdetail {
    @Id
    private String id;

    @Column(name = "questionId")
    private String questionid;

    private String description;

    private String performer;

    @Column(name = "updatedTime")
    private String updatedtime;

    @Column(name = "userName")
    private String username;

    @Column(name = "questionName")
    private String questionname;

    @Column(name = "fileName")
    private String filename;

    @Column(name = "evaluateDetail")
    private String evaluatedetail;

    private String time;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

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
     * @return fileName
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
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