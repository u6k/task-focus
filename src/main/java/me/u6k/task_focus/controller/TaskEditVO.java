
package me.u6k.task_focus.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class TaskEditVO {

    @NotNull
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String date;

    @NotBlank
    private String name;

    @NotNull
    @Range(min = 0)
    private int estimatedTime;

    @Pattern(regexp = "\\d{2}:\\d{2}")
    private String estimatedStartTime;

    @Pattern(regexp = "\\d{2}:\\d{2}")
    private String startTime;

    @Pattern(regexp = "\\d{2}:\\d{2}")
    private String endTime;

    public TaskEditVO() {
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getEstimatedStartTime() {
        return estimatedStartTime;
    }

    public void setEstimatedStartTime(String estimatedStartTime) {
        this.estimatedStartTime = estimatedStartTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
