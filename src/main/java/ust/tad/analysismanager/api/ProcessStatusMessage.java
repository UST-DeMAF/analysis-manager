package ust.tad.analysismanager.api;

import java.util.UUID;

import ust.tad.analysismanager.transformationprocess.Result;

/**
 * Indicates the status of a transformation process.
 * If the transformation process is finished it contains the transformation result.
 */
public class ProcessStatusMessage {

    /**
     * The id of the transformation process.
     */
    private UUID transformationProcessId;

    /**
     * Indicates whether the transformation process is finished or still running.
     */
    private Boolean isFinished;
    
    /**
     * The transformation result.
     * Defaults to null.
     */
    private Result result = null;


    public ProcessStatusMessage() {
    }

    public ProcessStatusMessage(UUID transformationProcessId, Boolean isFinished, String message, Result result) {
        this.transformationProcessId = transformationProcessId;
        this.isFinished = isFinished;
        this.result = result;
    }

    public UUID getTransformationProcessId() {
        return this.transformationProcessId;
    }

    public void setTransformationProcessId(UUID transformationProcessId) {
        this.transformationProcessId = transformationProcessId;
    }

    public Boolean getIsFinished() {
        return this.isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Result getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "{" +
            " transformationProcessId='" + getTransformationProcessId() + "'" +
            ", isFinished='" + getIsFinished() + "'" +
            ", result='" + getResult() + "'" +
            "}";
    }

}
