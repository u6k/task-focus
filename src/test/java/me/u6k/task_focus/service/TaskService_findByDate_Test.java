
package me.u6k.task_focus.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
public class TaskService_findByDate_Test {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepo;

    @Before
    public void setup() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        this.taskRepo.deleteAllInBatch();

        this.taskService.create(DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), "テスト作業0", 0, null);
        this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), "テスト作業1", 0, null);
        this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), "テスト作業2", 0, null);
        this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), "テスト作業3", 0, null);
        this.taskService.create(DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), "テスト作業4", 0, null);
    }

    @Test
    public void ユーザーのタスクが0件() throws Exception {
        // 事前条件を削除
        this.taskRepo.deleteAllInBatch();
        assertThat(this.taskRepo.count(), is(0L));

        // テスト実行
        List<Task> taskList = this.taskService.findByDate(new Date());

        // テスト結果検証
        assertThat(taskList.size(), is(0));
    }

    @Test
    public void 指定日付のタスクが0件() throws Exception {
        // テスト実行
        Date date = DateUtil.parseFullDatetime("2017-08-11 23:59:59.999");
        List<Task> taskList = this.taskService.findByDate(date);

        // テスト結果検証
        assertThat(taskList.size(), is(0));

        // テスト実行
        date = DateUtil.parseFullDatetime("2017-08-15 00:00:00.000");
        taskList = this.taskService.findByDate(date);

        // テスト結果検証
        assertThat(taskList.size(), is(0));
    }

    @Test
    public void findByDate_結果あり() throws Exception {
        this.taskRepo.findAll().forEach(x -> {
            System.out.println(x);
        });

        // テスト実行
        Date date = DateUtil.parseFullDatetime("2017-08-12 00:00:00.000");
        List<Task> taskList = this.taskService.findByDate(date);

        // テスト結果検証
        assertThat(taskList.size(), is(1));

        // テスト実行
        date = DateUtil.parseFullDatetime("2017-08-12 23:59:59.999");
        taskList = this.taskService.findByDate(date);

        // テスト結果検証
        assertThat(taskList.size(), is(1));

        // テスト実行
        date = DateUtil.parseFullDatetime("2017-08-13 00:00:00.000");
        taskList = this.taskService.findByDate(date);

        // テスト結果検証
        assertThat(taskList.size(), is(3));

        // テスト実行
        date = DateUtil.parseFullDatetime("2017-08-13 23:59:59.999");
        taskList = this.taskService.findByDate(date);

        // テスト結果検証
        assertThat(taskList.size(), is(3));

        // テスト実行
        date = DateUtil.parseFullDatetime("2017-08-14 00:00:00.000");
        taskList = this.taskService.findByDate(date);

        // テスト結果検証
        assertThat(taskList.size(), is(1));

        // テスト実行
        date = DateUtil.parseFullDatetime("2017-08-14 23:59:59.999");
        taskList = this.taskService.findByDate(date);

        // テスト結果検証
        assertThat(taskList.size(), is(1));
    }

}
