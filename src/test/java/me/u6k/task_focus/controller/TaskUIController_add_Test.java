
package me.u6k.task_focus.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskUIController_add_Test {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TaskRepository taskRepo;

    @Before
    public void setup() {
        this.taskRepo.deleteAllInBatch();
    }

    @Test
    public void タスクが追加される() throws Exception {
        // テスト実行
        ResultActions result = this.mvc.perform(post("/ui/tasks/add")
                        .param("date", "2017-08-15")
                        .param("name", "本を読む")
                        .param("estimatedTime", "60")
                        .param("estimatedStartTime", ""));

        // テスト結果検証
        result.andExpect(status().isFound())
                        .andExpect(redirectedUrl("/ui/tasks"));

        List<Task> taskList = this.taskRepo.findAll();

        assertThat(taskList.size(), is(1));
        // FIXME テスト結果を検証する
    }

    // FIXME テストを追加する

}
