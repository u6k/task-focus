
package me.u6k.task_focus.service;

import java.util.Date;
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

}
