
package me.u6k.task_focus.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepo;

    public UUID create(Date date, String name, int estimatedTime, Date estimatedStartTime) {
        /*
         * 入力チェック
         */
        if (date == null) {
            throw new IllegalArgumentException("date is null.");
        }
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank.");
        }
        if (estimatedTime < 0) {
            throw new IllegalArgumentException("estimatedTime < 0. estimatedTime=" + estimatedTime);
        }
        if (estimatedStartTime != null) {
            if (!DateUtils.isSameDay(date, estimatedStartTime)) {
                throw new IllegalArgumentException("date and estimatedStartTime are different day. date=" + date + ", estimatedStartTime=" + estimatedStartTime);
            }
        }

        /*
         * タスクを保存
         */
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDate(date);
        // TODO 正しい作業順を設定する。
        task.setOrderOfDate(0);
        task.setName(name.trim());
        task.setEstimatedTime(estimatedTime);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setStartTime(null);
        task.setEndTime(null);

        this.taskRepo.save(task);

        return task.getId();
    }

    public List<Task> findByDate(Date date) {
        /*
         * 入力チェック
         */
        if (date == null) {
            throw new IllegalArgumentException("date is null.");
        }

        /*
         * タスクを検索
         */
        Date startDate = date;

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(date);
        endCalendar.set(Calendar.HOUR_OF_DAY, 0);
        endCalendar.set(Calendar.MINUTE, 0);
        endCalendar.set(Calendar.SECOND, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        endCalendar.add(Calendar.MILLISECOND, -1);
        Date endDate = endCalendar.getTime();

        List<Task> taskList = this.taskRepo.findByDate(startDate, endDate);

        return taskList;
    }

}
