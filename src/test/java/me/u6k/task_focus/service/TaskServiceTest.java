
package me.u6k.task_focus.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepo;

    @Test
    public void findAll() {
        List<Task> l = this.taskRepo.findAll();

        assertThat(l.size(), is(0));

        this.taskService.create(new Date(), "テスト作業1", null);
        this.taskService.create(new Date(), "テスト作業2", null);
        this.taskService.create(new Date(), "テスト作業3", null);

        l = this.taskRepo.findAll();

        assertThat(l.size(), is(3));
    }

}
