
package me.u6k.task_focus.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.TimeZone;
import java.util.UUID;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import me.u6k.task_focus.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskService_update_Test {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepo;

    private UUID task1id;

    private UUID task2id;

    private UUID task3id;

    private UUID task4id;

    private UUID task5id;

    @Before
    public void setup() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        this.taskRepo.deleteAllInBatch();

        this.task1id = this.taskService.create(DateUtil.toDate(2017, 8, 12, 23, 59, 59, 999), "テスト作業0", 0, null);
        this.task2id = this.taskService.create(DateUtil.toDate(2017, 8, 13, 0, 0, 0, 0), "テスト作業1", 0, null);
        this.task3id = this.taskService.create(DateUtil.toDate(2017, 8, 13, 12, 34, 56, 987), "テスト作業2", 0, null);
        this.task4id = this.taskService.create(DateUtil.toDate(2017, 8, 13, 23, 59, 59, 999), "テスト作業3", 0, null);
        this.task5id = this.taskService.create(DateUtil.toDate(2017, 8, 14, 0, 0, 0, 0), "テスト作業4", 0, null);
    }

    @Test
    public void 更新が成功() {
        // テスト実行
        Task task = this.taskRepo.findOne(this.task1id);
        this.taskService.update(this.task1id, task.getDate(), "てすとさぎょう　いち", 60, null, null, null);

        // テスト結果を検証
        Task newTask = this.taskRepo.findOne(this.task1id);

        assertThat(newTask.getDate(), is(task.getDate()));
        assertThat(newTask.getName(), is("てすとさぎょう　いち"));
        assertThat(newTask.getEstimatedTime(), is(60));
        assertNull(newTask.getEstimatedStartTime());
        assertNull(newTask.getStartTime());
        assertNull(newTask.getEndTime());
    }

    // FIXME: テストを追加

}
