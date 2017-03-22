
package me.u6k.task_focus.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_task")
public class Task {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "order_of_date", nullable = false)
    private int orderOfDate;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "edtimated_start_time", nullable = true)
    private Date estimatedStartTime;

    @Column(name = "start_time", nullable = true)
    private Date startTime;

    @Column(name = "end_time", nullable = true)
    private Date endTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOrderOfDate() {
        return orderOfDate;
    }

    public void setOrderOfDate(int orderOfDate) {
        this.orderOfDate = orderOfDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEstimatedStartTime() {
        return estimatedStartTime;
    }

    public void setEstimatedStartTime(Date estimatedStartTime) {
        this.estimatedStartTime = estimatedStartTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
