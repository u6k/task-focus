
package me.u6k.task_focus.controller;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

public class TaskUpdateVO {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @NotBlank
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private Date estimatedStartTimePart;

    @Range(min = 0)
    private Integer estimatedTime;

    @DateTimeFormat(pattern = "HH:mm")
    private Date actualStartTimePart;

    @Range(min = 0)
    private Integer actualTime;

    @Length(max = 1000000)
    private String description;

    public TaskUpdateVO() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEstimatedStartTimePart() {
        return estimatedStartTimePart;
    }

    public void setEstimatedStartTimePart(Date estimatedStartTimePart) {
        this.estimatedStartTimePart = estimatedStartTimePart;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Date getActualStartTimePart() {
        return actualStartTimePart;
    }

    public void setActualStartTimePart(Date actualStartTimePart) {
        this.actualStartTimePart = actualStartTimePart;
    }

    public Integer getActualTime() {
        return actualTime;
    }

    public void setActualTime(Integer actualTime) {
        this.actualTime = actualTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
