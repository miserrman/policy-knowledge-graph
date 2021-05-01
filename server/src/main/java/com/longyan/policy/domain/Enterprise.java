package com.longyan.policy.domain;

public class Enterprise {

    private String name;

    private String region;

    //单位为万元
    private Double profit; //年度利润

    private Double researchExpense;

    private Double techOutfit;

    private Integer industryId;

    private Integer researchersNum;

    private Integer scale;//企业规模

    private String certificates;//用逗号分隔的字符串

    private String keywords;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getResearchExpense() {
        return researchExpense;
    }

    public void setResearchExpense(Double researchExpense) {
        this.researchExpense = researchExpense;
    }

    public Double getTechOutfit() {
        return techOutfit;
    }

    public void setTechOutfit(Double techOutfit) {
        this.techOutfit = techOutfit;
    }

    public Integer getIndustry() {
        return industryId;
    }

    public void setIndustry(Integer industryId) {
        this.industryId = industryId;
    }

    public Integer getResearchersNum() {
        return researchersNum;
    }

    public void setResearchersNum(Integer researchersNum) {
        this.researchersNum = researchersNum;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
