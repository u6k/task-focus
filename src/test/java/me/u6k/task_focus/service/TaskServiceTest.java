
package me.u6k.task_focus.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
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

    private SimpleDateFormat formatter;

    @Before
    public void setup() throws Exception{
        this.formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        this.taskService.create(this.formatter.parse("2017-08-12 23:59:59.999"), "テスト作業0", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 00:00:00.000"), "テスト作業1", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 12:34:56.987"), "テスト作業2", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 23:59:59.999"), "テスト作業3", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-14 00:00:00.000"), "テスト作業4", 0, null);
    }

    @After
    public void teardown(){
        this.taskRepo.deleteAllInBatch();
    }

    @Test
    public void create() {
        List<Task> l = this.taskRepo.findAll();

        assertThat(l.size(), is(5));

        this.taskService.create(new Date(), "テスト作業1", 60, null);
        this.taskService.create(new Date(), "テスト作業2", 10, null);
        this.taskService.create(new Date(), "テスト作業3", 0, null);

        l = this.taskRepo.findAll();

        assertThat(l.size(), is(8));
    }

    @Test
    public void create_作業日が空の場合はエラー() {
        try {
            this.taskService.create(null, "テスト作業", 0, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("date is null."));
        }
    }

    @Test
    public void create_作業名が空の場合はエラー() throws Exception {
        Date date = this.formatter.parse("2015-12-23 00:00:00.000");

        // nullはエラー
        try {
            this.taskService.create(date, null, 0, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("name is blank."));
        }

        // 空文字列はエラー
        try {
            this.taskService.create(date, "", 0, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("name is blank."));
        }

        // 空白のみはエラー
        try {
            this.taskService.create(date, "   ", 0, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("name is blank."));
        }

        // 前後の空白はトリミング
        this.taskService.create(date, "  テスト作業   ", 0, null);

        List<Task> l = this.taskRepo.findAll();
        assertThat(l.size(), is(1));

        Task t = l.get(0);
        assertNotNull(t.getId());
        assertTrue(DateUtils.isSameInstant(t.getDate(), date));
        assertThat(t.getOrderOfDate(), is(0));
        assertThat(t.getName(), is("テスト作業"));
        assertThat(t.getEstimatedTime(), is(0));
        assertNull(t.getEstimatedStartTime());
        assertNull(t.getStartTime());
        assertNull(t.getEndTime());
    }

    @Test
    public void create_見積り時間がマイナスの場合はエラー() throws Exception {
        Date date = this.formatter.parse("2015-12-23 00:00:00.000");

        // マイナスはエラー
        try {
            this.taskService.create(date, "テスト作業", -1, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("estimatedTime < 0. estimatedTime=-1"));
        }

        // マイナスはエラー
        try {
            this.taskService.create(date, "テスト作業", -123, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("estimatedTime < 0. estimatedTime=-123"));
        }

        // 0は正常
        UUID id = this.taskService.create(date, "テスト作業", 0, null);

        Task t = this.taskRepo.findOne(id);
        assertThat(t.getId(), is(id));
        assertTrue(DateUtils.isSameInstant(t.getDate(), date));
        assertThat(t.getOrderOfDate(), is(0));
        assertThat(t.getName(), is("テスト作業"));
        assertThat(t.getEstimatedTime(), is(0));
        assertNull(t.getEstimatedStartTime());
        assertNull(t.getStartTime());
        assertNull(t.getEndTime());

        // プラスは正常
        id = this.taskService.create(date, "テスト作業", 1, null);

        t = this.taskRepo.findOne(id);
        assertThat(t.getId(), is(id));
        assertTrue(DateUtils.isSameInstant(t.getDate(), date));
        assertThat(t.getOrderOfDate(), is(0));
        assertThat(t.getName(), is("テスト作業"));
        assertThat(t.getEstimatedTime(), is(1));
        assertNull(t.getEstimatedStartTime());
        assertNull(t.getStartTime());
        assertNull(t.getEndTime());

        // プラスは正常
        id = this.taskService.create(date, "テスト作業", 234, null);

        t = this.taskRepo.findOne(id);
        assertThat(t.getId(), is(id));
        assertTrue(DateUtils.isSameInstant(t.getDate(), date));
        assertThat(t.getOrderOfDate(), is(0));
        assertThat(t.getName(), is("テスト作業"));
        assertThat(t.getEstimatedTime(), is(234));
        assertNull(t.getEstimatedStartTime());
        assertNull(t.getStartTime());
        assertNull(t.getEndTime());
    }

    @Test
    public void create_開始予定時刻と作業日が異なる場合はエラー() throws Exception {
        Date date = this.formatter.parse("2015-12-23 00:00:00.000");

        try {
            Date estimatedStartTime = this.formatter.parse("2015-12-24 13:00:00.000");
            this.taskService.create(date, "テスト作業", 0, estimatedStartTime);

            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("date and estimatedStartTime are different day. date=Wed Dec 23 00:00:00 GMT 2015, estimatedStartTime=Thu Dec 24 13:00:00 GMT 2015"));
        }

        Date estimatedStartTime = this.formatter.parse("2015-12-23 14:27:00.000");
        UUID id = this.taskService.create(date, "テスト作業", 0, estimatedStartTime);
        Task t = this.taskRepo.findOne(id);
        assertThat(t.getId(), is(id));
        assertTrue(DateUtils.isSameInstant(t.getDate(), date));
        assertThat(t.getOrderOfDate(), is(0));
        assertThat(t.getName(), is("テスト作業"));
        assertThat(t.getEstimatedTime(), is(0));
        assertTrue(DateUtils.isSameInstant(t.getEstimatedStartTime(), estimatedStartTime));
        assertNull(t.getStartTime());
        assertNull(t.getEndTime());
    }

    @Test
    public void findByDate_0件() throws Exception{
        this.taskService.create(this.formatter.parse("2017-08-12 23:59:59.999"), "テスト作業0", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 00:00:00.000"), "テスト作業1", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 12:34:56.987"), "テスト作業2", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 23:59:59.999"), "テスト作業3", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-14 00:00:00.000"), "テスト作業4", 0, null);

        Date date = this.formatter.parse("2017-08-11 23:59:59.999");
        List<Task> taskList = this.taskService.findByDate(date);

        assertThat(taskList.size(), is(0));

        date = this.formatter.parse("2017-08-15 00:00:00.000");
        taskList=this.taskService.findByDate(date);

        assertThat(taskList.size(), is(0));
    }

    @Test
    public void findByDate_結果あり() throws Exception{
        this.taskService.create(this.formatter.parse("2017-08-12 23:59:59.999"), "テスト作業0", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 00:00:00.000"), "テスト作業1", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 12:34:56.987"), "テスト作業2", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-13 23:59:59.999"), "テスト作業3", 0, null);
        this.taskService.create(this.formatter.parse("2017-08-14 00:00:00.000"), "テスト作業4", 0, null);

        Date date = this.formatter.parse("2017-08-12 00:00:00.000");
        List<Task> taskList = this.taskService.findByDate(date);

        assertThat(taskList.size(), is(1));
    }

}
