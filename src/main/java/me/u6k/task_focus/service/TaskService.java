
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

    public UUID add(Date date, String name, int estimatedTime, Date estimatedStartTime) {
        L.debug("#add: date={}, name={}, estimatedTime={}, estimatedStartTime={}", date, name, estimatedTime, estimatedStartTime);

        /*
         * タスクを設定
         */
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDate(date != null ? DateUtils.truncate(date, Calendar.DAY_OF_MONTH) : null);
        // TODO 正しい作業順を設定する。
        task.setOrderOfDate(0);
        task.setName(name != null ? name.trim() : null);
        task.setEstimatedTime(estimatedTime);
        task.setEstimatedStartTime(estimatedStartTime);
        L.debug("new Task: task={}", task);

        /*
         * 入力チェック
         */
        this.validate(task);

        /*
         * タスクを保存
         */
        this.taskRepo.save(task);
        L.debug("taskRepo.save: success");

        L.debug("return: task.getId={}", task.getId());
        return task.getId();
    }

    public void update(UUID id, Date date, String name, int estimatedTime, Date estimatedStartTime, Date startTime, Date endTime) {
        L.debug("#update: id={}, date={}, name={}, estimatedTime={}, estimatedStartTime={}, startTime={}, endTime={}", id, date, name, estimatedTime, estimatedStartTime, startTime, endTime);

        /*
         * タスクを検索、設定
         */
        Task task = this.taskRepo.findOne(id);
        if (task == null) {
            throw new IllegalArgumentException("task not found. id=" + id);
        }

        task.setDate(date != null ? DateUtils.truncate(date, Calendar.DAY_OF_MONTH) : null);
        // TODO 正しい作業順を設定する。
        task.setOrderOfDate(0);
        task.setName(name != null ? name.trim() : null);
        task.setEstimatedTime(estimatedTime);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        L.debug("setup Task: task={}", task);

        /*
         * 入力チェック
         */
        this.validate(task);

        /*
         * タスクを更新
         */
        this.taskRepo.save(task);
        L.debug("taskRepo.save: success");
    }

    public void delete(UUID id) {
        L.debug("#delete: id={}", id);

        /*
         * 入力チェック
         */
        if (id == null) {
            throw new IllegalArgumentException("id is null.");
        }

        /*
         * タスクを検索
         */
        Task task = this.taskRepo.findOne(id);
        if (task == null) {
            throw new IllegalArgumentException("task not found. id=" + id);
        }

        /*
         * タスクを削除
         */
        this.taskRepo.delete(task);
        L.debug("taskRepo.delete: success");
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

    private void validate(Task task) {
        L.debug("#validate: task={}", task);

        if (task.getDate() == null) {
            throw new IllegalStateException("task.date is null.");
        }

        if (StringUtils.isBlank(task.getName())) {
            throw new IllegalStateException("task.name is blank.");
        }

        if (task.getEstimatedStartTime() != null) {
            if (!DateUtils.isSameDay(task.getDate(), task.getEstimatedStartTime())) {
                throw new IllegalStateException("task.date and task.estimatedStartTime is not same day.");
            }
        }

        if (task.getEstimatedTime() < 0) {
            throw new IllegalStateException("task.estimatedTime < 0");
        }

        if (task.getStartTime() != null) {
            if (!DateUtils.isSameDay(task.getDate(), task.getStartTime())) {
                throw new IllegalStateException("task.date and task.startTime is not same day.");
            }
        }

        if (task.getEndTime() != null) {
            if (task.getStartTime() == null) {
                throw new IllegalStateException("task.endTime is not null, but task.startTime is null.");
            }

            if (task.getStartTime().getTime() > task.getEndTime().getTime()) {
                throw new IllegalStateException("task.startTime > task.endTime");
            }
        }
    }

}
