
package me.u6k.task_focus.service;

import java.util.Date;
import java.util.UUID;

import javax.transaction.Transactional;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepo;

    public UUID create(Date date, String name, Date estimatedStartTime) {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setDate(date);
        // TODO 正しい作業順を設定する。
        task.setOrderOfDate(0);
        task.setName(name);
        task.setEstimatedStartTime(estimatedStartTime);
        task.setStartTime(null);
        task.setEndTime(null);

        this.taskRepo.save(task);

        return task.getId();
    }

}
