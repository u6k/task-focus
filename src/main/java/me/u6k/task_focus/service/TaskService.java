
package me.u6k.task_focus.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import me.u6k.task_focus.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TaskService {

    private static final Logger L = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepo;

    public UUID create(Date date, String name, int estimatedTime, Date estimatedStartTime) {
        L.trace("#create: date={}, name={}, estimatedTime={}, estimatedStartTime={}", date, name, estimatedTime, estimatedStartTime);

        /*
         * 入力チェック
         */
        L.trace("validate: date={}", date);
        if (date == null) {
            throw new IllegalArgumentException("date is null.");
        }

        L.trace("validate: name={}", name);
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank.");
        }

        L.trace("validate: estimatedTime={}", estimatedTime);
        if (estimatedTime < 0) {
            throw new IllegalArgumentException("estimatedTime < 0. estimatedTime=" + estimatedTime);
        }

        L.trace("validate: estimatedStartTime={}", estimatedStartTime);
        if (estimatedStartTime != null) {
            boolean isSameDay = DateUtils.isSameDay(date, estimatedStartTime);
            L.trace("validate: isSameDay={}", isSameDay);
            if (!isSameDay) {
                throw new IllegalArgumentException("date and estimatedStartTime are different day. date=" + date + ", estimatedStartTime=" + estimatedStartTime);
            }
        }

        /*
         * タスクを保存
         */
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDate(DateUtil.datetimeToDate(date));
        // TODO 正しい作業順を設定する。
        task.setOrderOfDate(0);
        task.setName(name.trim());
        task.setEstimatedTime(estimatedTime);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setStartTime(null);
        task.setEndTime(null);
        L.trace("new Task: task={}", task);

        this.taskRepo.save(task);
        L.trace("taskRepo.save: success");

        L.trace("return: task.getId={}", task.getId());
        return task.getId();
    }

    public List<Task> findByDate(Date date) {
        L.trace("#findByDate: date={}", date);

        /*
         * 入力チェック
         */
        L.trace("validate: date={}", date);
        if (date == null) {
            throw new IllegalArgumentException("date is null.");
        }

        /*
         * タスクを検索
         */
        date = DateUtil.datetimeToDate(date);
        L.trace("datetimeToDate: date={}", date);

        List<Task> taskList = this.taskRepo.findByDate(date);
        L.trace("findByDate: taskList.size={}", taskList.size());

        L.trace("return: taskList.size={}", taskList.size());
        return taskList;
    }

}
