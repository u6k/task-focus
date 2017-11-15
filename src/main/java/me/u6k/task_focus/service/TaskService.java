
package me.u6k.task_focus.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import me.u6k.task_focus.model.User;
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
    private UserService userService;

    @Autowired
    private TaskRepository taskRepo;

    public UUID add(UUID userId, String name, Date estimatedStartTime, Integer estimatedTime) {
        L.debug("#add: userId={}, name={}, estimatedStartTime={}, estimatedTime={}", userId, name, estimatedStartTime, estimatedTime);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
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

        // ユーザーの存在確認
        User user = this.userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("user not found: userId=" + userId);
        }

        /*
         * タスクを保存
         */
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setName(name != null ? name.trim() : null);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setEstimatedTime(estimatedTime);
        task.setUser(user);
        L.debug("new Task: task={}", task);

        this.taskRepo.save(task);
        L.debug("taskRepo.save: success");

        L.debug("return: task.getId={}", task.getId());
        return task.getId();
    }

    public void update(UUID id, UUID userId, String name, Date estimatedStartTime, Integer estimatedTime, Date actualStartTime, Integer actualTime, String description) {
        L.debug("#update: id={}, userId={}, name={}, estimatedStartTime={}, estimatedTime={}, actualStartTime={}, actualTime={}, description={}, userId={}", id, userId, name, estimatedStartTime, estimatedTime, actualStartTime, actualTime, description, userId);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
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

        // ユーザーの存在確認
        User user = this.userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("user not found: userId=" + userId);
        }

        // タスクの存在確認
        List<Task> tasks = user.getTasks().stream()
            .filter(x -> x.getId().equals(id))
            .collect(Collectors.toList());

        if (tasks.size() == 0) {
            throw new IllegalArgumentException("task not found: id=" + id + ", userId=" + userId);
        }

        /*
         * タスクを保存
         */
        Task task = tasks.get(0);
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

    public void remove(UUID id, UUID userId) {
        L.debug("#remove: id={}, userId={}", id, userId);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }

        // ユーザーの存在確認
        User user = this.userService.findById(userId);

        // タスクの存在確認
        if (user.getTasks().stream()
            .filter(x -> x.getId().equals(id))
            .count() == 0) {
            throw new IllegalArgumentException("task not found: id=" + id + ", userId=" + userId);
        }

        /*
         * タスクを削除
         */
        // ユーザーが持つタスクを削除
        user.getTasks().removeIf(x -> x.getId().equals(id));
        // ユーザーを更新することで、タスクの削除を反映
        this.userService.update(user);
        L.debug("task remove: success");
    }

    public List<Task> findByDate(UUID userId, Date date) {
        L.debug("#findByDate: userId={}, date={}", userId, date);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        }

        /*
         * タスクを検索
         */
        Date fromDate = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date toDate = DateUtils.addDays(fromDate, 1);
        L.debug("fromDate={}, toDate={}", fromDate, toDate);

        List<Task> taskList = this.taskRepo.findByDate(userId, fromDate, toDate);
        L.debug("findByDate: taskList.size={}", taskList.size());

        L.debug("return: taskList.size={}", taskList.size());
        return taskList;
    }

    public Task findById(UUID id, UUID userId) {
        L.debug("#findById: id={}, userId={}", id, userId);

        /*
         * 前提条件確認
         */
        // 入力チェック
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("userId is null");
        }

        // ユーザーの存在確認
        User user = this.userService.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("user not found: userId=" + userId);
        }

        /*
         * タスクを取得
         */
        List<Task> tasks = user.getTasks().stream()
            .filter(x -> x.getId().equals(id))
            .collect(Collectors.toList());

        Task task = null;
        if (tasks.size() != 0) {
            task = tasks.get(0);
        }

        L.debug("return: task={}", task);
        return task;
    }

}
