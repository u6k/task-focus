
package me.u6k.task_focus.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.UUID;

import me.u6k.task_focus.model.TaskRepository;
import me.u6k.task_focus.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TaskUIController_edit_Test {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepo;

    private UUID task1id;

    @Before
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.ctx).build();

        this.taskRepo.deleteAllInBatch();

        this.task1id = this.taskService.create(new Date(), "テスト作業1", 0, null);
    }

    @Test
    public void 初期表示() throws Exception {
        // テスト実行
        ResultActions result = this.mvc.perform(get("/ui/tasks/" + this.task1id + "/edit"));

        // テスト結果検証
        result.andExpect(status().isOk());

        // FIXME テスト結果を検証する
    }

    // FIXME テストを追加する

}
