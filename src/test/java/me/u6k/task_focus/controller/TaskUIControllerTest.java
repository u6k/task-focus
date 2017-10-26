
package me.u6k.task_focus.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import me.u6k.task_focus.service.TaskService;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(Enclosed.class)
public class TaskUIControllerTest {

    @BeforeClass
    public static void setup() {
        // FIXME: CircleCIでコントローラー・テストを正常動作できるようにする。
        boolean isUITestSkip = Boolean.parseBoolean(System.getProperty("uitest.skip", "false"));
        System.out.println("isUITestSkip=" + isUITestSkip);
        Assume.assumeFalse("コントローラー・テストがCircleCIでハングアップしてしまうため、スキップ", isUITestSkip);
    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureMockMvc
    public static class index {

        @Autowired
        private MockMvc mvc;

        @Test
        public void リダイレクトされる() throws Exception {
            // テスト実行
            ResultActions result = this.mvc.perform(get("/"));

            // テスト結果検証
            result.andExpect(status().isFound())
                .andExpect(redirectedUrl("/ui/tasks"));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureMockMvc
    public static class list {

        @Autowired
        private MockMvc mvc;

        @Test
        public void リストが表示される_0件() throws Exception {
            // テスト実行
            ResultActions result = this.mvc.perform(get("/ui/tasks"));

            // テスト結果検証
            result.andExpect(status().isOk());
            // FIXME 表示内容を確認する
        }

        // FIXME テストを追加する

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureMockMvc
    public static class add {

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
                .param("name", "本を読む"));

            // テスト結果検証
            result.andExpect(status().isFound())
                .andExpect(redirectedUrl("/ui/tasks"));

            List<Task> taskList = this.taskRepo.findAll();

            assertThat(taskList.size(), is(1));
            // FIXME テスト結果を検証する
        }

        // FIXME テストを追加する

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureMockMvc
    public static class update {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private TaskService taskService;

        @Autowired
        private TaskRepository taskRepo;

        private UUID task1id;

        @Before
        public void setup() {
            this.taskRepo.deleteAllInBatch();

            this.task1id = this.taskService.create(new Date(), "テスト作業1", 0, null);
        }

        @Test
        public void 初期表示() throws Exception {
            // テスト実行
            ResultActions result = this.mvc.perform(get("/ui/tasks/" + this.task1id + "/update"));

            // テスト結果検証
            result.andExpect(status().isOk());

            // FIXME テスト結果を検証する
        }

        // FIXME テストを追加する

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    @AutoConfigureMockMvc
    public static class delete {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private TaskService taskService;

        @Autowired
        private TaskRepository taskRepo;

        private UUID task1Id;

        private UUID task2Id;

        private UUID task3Id;

        @Before
        public void setup() {
            this.taskRepo.deleteAllInBatch();

            this.task1Id = this.taskService.create(new Date(), "テスト作業1", 0, null);
            this.task2Id = this.taskService.create(new Date(), "テスト作業2", 0, null);
            this.task3Id = this.taskService.create(new Date(), "テスト作業3", 0, null);
        }

        @Test
        public void 作業を削除() throws Exception {
            // 事前条件を確認
            assertThat(this.taskRepo.count(), is(3L));

            // テスト実行
            ResultActions result = this.mvc.perform(post("/ui/tasks/" + this.task1Id + "/delete"));

            // テスト結果検証
            result.andExpect(status().isFound())
                .andExpect(redirectedUrl("/ui/tasks"));
        }

        // FIXME テストを追加する

    }

}
