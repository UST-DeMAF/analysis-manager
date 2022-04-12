package ust.tad.analysismanager.transformationprocess;

import java.util.Objects;

public class Result {

    private String path;

    private double analysisProgress;

    private double comprehensibility;

    private double confidence;

    private double typeCompletenessVal1;

    private double typeCompletenessVal2;


    public Result() {
    }

    public Result(String path, double analysisProgress, double comprehensibility, double confidence, double typeCompletenessVal1, double typeCompletenessVal2) {
        this.path = path;
        this.analysisProgress = analysisProgress;
        this.comprehensibility = comprehensibility;
        this.confidence = confidence;
        this.typeCompletenessVal1 = typeCompletenessVal1;
        this.typeCompletenessVal2 = typeCompletenessVal2;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getAnalysisProgress() {
        return this.analysisProgress;
    }

    public void setAnalysisProgress(double analysisProgress) {
        this.analysisProgress = analysisProgress;
    }

    public double getComprehensibility() {
        return this.comprehensibility;
    }

    public void setComprehensibility(double comprehensibility) {
        this.comprehensibility = comprehensibility;
    }

    public double getConfidence() {
        return this.confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getTypeCompletenessVal1() {
        return this.typeCompletenessVal1;
    }

    public void setTypeCompletenessVal1(double typeCompletenessVal1) {
        this.typeCompletenessVal1 = typeCompletenessVal1;
    }

    public double getTypeCompletenessVal2() {
        return this.typeCompletenessVal2;
    }

    public void setTypeCompletenessVal2(double typeCompletenessVal2) {
        this.typeCompletenessVal2 = typeCompletenessVal2;
    }

    public Result path(String path) {
        setPath(path);
        return this;
    }

    public Result analysisProgress(double analysisProgress) {
        setAnalysisProgress(analysisProgress);
        return this;
    }

    public Result comprehensibility(double comprehensibility) {
        setComprehensibility(comprehensibility);
        return this;
    }

    public Result confidence(double confidence) {
        setConfidence(confidence);
        return this;
    }

    public Result typeCompletenessVal1(double typeCompletenessVal1) {
        setTypeCompletenessVal1(typeCompletenessVal1);
        return this;
    }

    public Result typeCompletenessVal2(double typeCompletenessVal2) {
        setTypeCompletenessVal2(typeCompletenessVal2);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Result)) {
            return false;
        }
        Result result = (Result) o;
        return Objects.equals(path, result.path) && analysisProgress == result.analysisProgress && comprehensibility == result.comprehensibility && confidence == result.confidence && typeCompletenessVal1 == result.typeCompletenessVal1 && typeCompletenessVal2 == result.typeCompletenessVal2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, analysisProgress, comprehensibility, confidence, typeCompletenessVal1, typeCompletenessVal2);
    }

    @Override
    public String toString() {
        return "{" +
            " path='" + getPath() + "'" +
            ", analysisProgress='" + getAnalysisProgress() + "'" +
            ", comprehensibility='" + getComprehensibility() + "'" +
            ", confidence='" + getConfidence() + "'" +
            ", typeCompletenessVal1='" + getTypeCompletenessVal1() + "'" +
            ", typeCompletenessVal2='" + getTypeCompletenessVal2() + "'" +
            "}";
    }

    
}
