
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

    public UUID add(String name, Date estimatedStartTime, Integer estimatedTime) {
        L.debug("#add: name={}, estimatedStartTime={}, estimatedTime={}", name, estimatedStartTime, estimatedTime);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank");
        }
        if (estimatedStartTime == null) {
            throw new IllegalArgumentException("estimatedStartTime is null");
        }
        if (estimatedTime != null) {
            if (estimatedTime < 0) {
                throw new IllegalArgumentException("estimatedTime < 0: estimatedTime=" + estimatedTime);
            }
        }

        /*
         * タスクを保存
         */
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setName(name != null ? name.trim() : null);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setEstimatedTime(estimatedTime);
        L.debug("new Task: task={}", task);

        this.taskRepo.save(task);
        L.debug("taskRepo.save: success");

        L.debug("return: task.getId={}", task.getId());
        return task.getId();
    }

    public void update(UUID id, String name, Date estimatedStartTime, Integer estimatedTime, Date actualStartTime, Integer actualTime, String description) {
        L.debug("#update: id={}, name={}, estimatedStartTime={}, estimatedTime={}, actualStartTime={}, actualTime={}, description={}", id, name, estimatedStartTime, estimatedTime, actualStartTime, actualTime, description);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank");
        }
        if (estimatedStartTime == null) {
            throw new IllegalArgumentException("estimatedStartTime is null");
        }
        if (estimatedTime != null) {
            if (estimatedTime < 0) {
                throw new IllegalArgumentException("estimatedTime < 0: estimatedTime=" + estimatedTime);
            }
        }
        if (actualStartTime != null) {
            if (!DateUtils.isSameDay(estimatedStartTime, actualStartTime)) {
                throw new IllegalArgumentException("estimatedStartTime.date is not actualStartTime.date: estimatedStartTime=" + estimatedStartTime + ", actualStartTime=" + actualStartTime);
            }
        }
        if (actualTime != null) {
            if (actualTime < 0) {
                throw new IllegalArgumentException("actualTime < 0: actualTime=" + actualTime);
            }
        }

        // タスクの存在確認
        Task task = this.taskRepo.findOne(id);
        if (task == null) {
            throw new IllegalArgumentException("task not found: id=" + id);
        }

        /*
         * タスクを保存
         */
        task.setName(name != null ? name.trim() : null);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setEstimatedTime(estimatedTime);
        task.setActualStartTime(actualStartTime);
        task.setActualTime(actualTime);
        task.setDescription(description);
        L.debug("setup Task: task={}", task);

        this.taskRepo.save(task);
        L.debug("taskRepo.save: success");
    }

    public void remove(UUID id) {
        L.debug("#remove: id={}", id);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        // タスクの存在確認
        Task task = this.taskRepo.findOne(id);
        if (task == null) {
            throw new IllegalArgumentException("task not found: id=" + id);
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
         * 前提条件確認
         */
        // 入力チェック
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }

        /*
         * タスクを検索
         */
        Date fromDate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date toDate = DateUtils.addDays(fromDate, 1);
        L.debug("fromDate={}, toDate={}", fromDate, toDate);

        List<Task> taskList = this.taskRepo.findByDate(fromDate, toDate);
        L.debug("findByDate: taskList.size={}", taskList.size());

        L.debug("return: taskList.size={}", taskList.size());
        return taskList;
    }

    public Task findById(UUID id) {
        L.debug("#findById: id={}", id);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (id == null) {
            throw new IllegalArgumentException("id is null");
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
