
package me.u6k.task_focus.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public class TaskUIController_index_Test {

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
