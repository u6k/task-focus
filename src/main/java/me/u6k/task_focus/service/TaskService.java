
package me.u6k.task_focus.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {

    private static final Logger L = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepo;

    public UUID create(Date date, String name, int estimatedTime, Date estimatedStartTime) {
        L.debug("#create: date={}, name={}, estimatedTime={}, estimatedStartTime={}", date, name, estimatedTime, estimatedStartTime);

        /*
         * 入力チェック
         */
        L.debug("validate: date={}", date);
        if (date == null) {
            throw new IllegalArgumentException("date is null.");
        }

        L.debug("validate: name={}", name);
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank.");
        }

        L.debug("validate: estimatedTime={}", estimatedTime);
        if (estimatedTime < 0) {
            throw new IllegalArgumentException("estimatedTime < 0. estimatedTime=" + estimatedTime);
        }

        L.debug("validate: estimatedStartTime={}", estimatedStartTime);
        if (estimatedStartTime != null) {
            boolean isSameDay = DateUtils.isSameDay(date, estimatedStartTime);
            L.debug("validate: isSameDay(date, estimatedStartTime)={}", isSameDay);
            if (!isSameDay) {
                throw new IllegalArgumentException("date and estimatedStartTime are different day. date=" + date + ", estimatedStartTime=" + estimatedStartTime);
            }
        }

        /*
         * タスクを保存
         */
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDate(DateUtils.truncate(date, Calendar.DAY_OF_MONTH));
        // TODO 正しい作業順を設定する。
        task.setOrderOfDate(0);
        task.setName(name.trim());
        task.setEstimatedTime(estimatedTime);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setStartTime(null);
        task.setEndTime(null);
        L.debug("new Task: task={}", task);

        this.taskRepo.save(task);
        L.debug("taskRepo.save: success");

        L.debug("return: task.getId={}", task.getId());
        return task.getId();
    }

    public void update(UUID id, Date date, String name, int estimatedTime, Date estimatedStartTime, Date startTime, Date endTime) {
        L.debug("#update: id={}, date={}, name={}, estimatedTime={}, estimatedStartTime={}, startTime={}, endTime={}", id, date, name, estimatedTime, estimatedStartTime, startTime, endTime);

        /*
         * 入力チェック
         */
        L.debug("validate: id={}", id);
        if (id == null) {
            throw new IllegalArgumentException("id is null.");
        }

        L.debug("validate: date={}", date);
        if (date == null) {
            throw new IllegalArgumentException("date is null.");
        }

        L.debug("validate: name={}", name);
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank.");
        }

        L.debug("validate: estimatedTime={}", estimatedTime);
        if (estimatedTime < 0) {
            throw new IllegalArgumentException("estimatedTime < 0. estimatedTime=" + estimatedTime);
        }

        L.debug("validate: estimatedStartTime={}", estimatedStartTime);
        if (estimatedStartTime != null) {
            boolean isSameDay = DateUtils.isSameDay(date, estimatedStartTime);
            L.debug("validate: isSameDay={}", isSameDay);
            if (!isSameDay) {
                throw new IllegalArgumentException("date and estimatedStartTime are different day. date=" + date + ", estimatedStartTime=" + estimatedStartTime);
            }
        }

        L.debug("validate: startTime={}", startTime);
        if (startTime != null) {
            boolean isSameDay = DateUtils.isSameDay(date, startTime);
            L.debug("validate: isSameDay(date, startTime)={}", isSameDay);
            if (!isSameDay) {
                throw new IllegalArgumentException("date and startTime are different day. date=" + date + ", startTime=" + startTime);
            }
        }

        L.debug("validate: endTime={}", endTime);
        if (endTime != null) {
            L.debug("validate: startTime={}", startTime);
            if (startTime == null) {
                throw new IllegalArgumentException("startTime is null, but endTime is not null. endTime=" + endTime);
            }

            boolean compare = (startTime.getTime() <= endTime.getTime());
            L.debug("validate: (startTime <= endTime)={}", compare);
            if (!compare) {
                throw new IllegalArgumentException("startTime > endTime. startTime=" + startTime + ", endTime=" + endTime);
            }
        }

        /*
         * タスクを検索
         */
        Task task = this.taskRepo.findOne(id);

        L.debug("findOne: task={}", task);
        if (task == null) {
            throw new IllegalArgumentException("task not found. id=" + id);
        }

        /*
         * タスクを更新
         */
        task.setDate(DateUtils.truncate(date, Calendar.HOUR));
        // TODO 正しい作業順を設定する。
        task.setOrderOfDate(0);
        task.setName(name.trim());
        task.setEstimatedTime(estimatedTime);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        L.debug("setup Task: task={}", task);

        this.taskRepo.save(task);
        L.debug("taskRepo.save: success");
    }

    public List<Task> findByDate(Date date) {
        L.debug("#findByDate: date={}", date);

        /*
         * 入力チェック
         */
        L.debug("validate: date={}", date);
        if (date == null) {
            throw new IllegalArgumentException("date is null.");
        }

        /*
         * タスクを検索
         */
        date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        L.debug("DateUtils.truncate(HOUR): date={}", date);

        List<Task> taskList = this.taskRepo.findByDate(date);
        L.debug("findByDate: taskList.size={}", taskList.size());

        L.debug("return: taskList.size={}", taskList.size());
        return taskList;
    }

    public Task findById(UUID id) {
        L.debug("#findById: id={}", id);

        /*
         * 入力チェック
         */
        L.debug("validate: id={}", id);
        if (id == null) {
            throw new IllegalArgumentException("id is null.");
        }

        /*
         * タスクを検索
         */
        Task task = this.taskRepo.findOne(id);
        L.debug("findOne: task={}", task);

        L.debug("return: task={}", task);
        return task;
    }

}
