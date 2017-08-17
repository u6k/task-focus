
package me.u6k.task_focus.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.model.TaskRepository;
import me.u6k.task_focus.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

@RunWith(Parameterized.class)
@SpringBootTest
public class TaskServiceParameterizedTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepo;

    private Fixture fixture;

    public TaskServiceParameterizedTest(Fixture fixture) {
        this.fixture = fixture;
    }

    @Before
    public void setup() throws Exception {
        new TestContextManager(this.getClass()).prepareTestInstance(this);

        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        this.taskRepo.deleteAllInBatch();
    }

    static class Fixture {

        String testName;

        Date date;

        String name;

        int estimatedTime;

        Date estimatedStartTime;

        Task result;

        Class<? extends Throwable> cause;

        String causeMessage;

        Fixture(String testName) {
            this.testName = testName;
        }

        @Override
        public String toString() {
            return this.testName;
        }

        Fixture setDate(Date date) {
            this.date = date;
            return this;
        }

        Fixture setName(String name) {
            this.name = name;
            return this;
        }

        Fixture setEstimatedTime(int estimatedTime) {
            this.estimatedTime = estimatedTime;
            return this;
        }

        Fixture setEstimatedStartTime(Date estimatedStartTime) {
            this.estimatedStartTime = estimatedStartTime;
            return this;
        }

        Fixture setResult(Task result) {
            this.result = result;
            return this;
        }

        Fixture setCause(Class<? extends Throwable> cause) {
            this.cause = cause;
            return this;
        }

        Fixture setCauseMessage(String causeMessage) {
            this.causeMessage = causeMessage;
            return this;
        }

    }

    @Parameters(name = "{0}")
    public static Iterable<Fixture> getParameters() throws Exception {
        return Arrays.asList(
            new Fixture("OK: 登録できる")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName("テスト作業")
                .setEstimatedTime(60)
                .setEstimatedStartTime(null)
                .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
            new Fixture("OK: dateの時分秒ミリ秒がリセットされる")
                .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                .setName("テスト作業")
                .setEstimatedTime(60)
                .setEstimatedStartTime(null)
                .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
            new Fixture("NG: dateがnull")
                .setDate(null)
                .setName("テスト作業")
                .setEstimatedTime(60)
                .setEstimatedStartTime(null)
                .setCause(IllegalStateException.class)
                .setCauseMessage("task.date is null."),
            new Fixture("NG: nameがnull")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName(null)
                .setEstimatedTime(60)
                .setEstimatedStartTime(null)
                .setCause(IllegalStateException.class)
                .setCauseMessage("task.name is blank."),
            new Fixture("NG: nameが空文字列")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName("")
                .setEstimatedTime(60)
                .setEstimatedStartTime(null)
                .setCause(IllegalStateException.class)
                .setCauseMessage("task.name is blank."),
            new Fixture("NG: nameが空白文字")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName(" ")
                .setEstimatedTime(60)
                .setEstimatedStartTime(null)
                .setCause(IllegalStateException.class)
                .setCauseMessage("task.name is blank."),
            new Fixture("OK: nameの前後に空白文字")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName("     テスト作業    ")
                .setEstimatedTime(60)
                .setEstimatedStartTime(null)
                .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
            new Fixture("NG: estimatedTimeがマイナス")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName("テスト作業")
                .setEstimatedTime(-1)
                .setEstimatedStartTime(null)
                .setCause(IllegalStateException.class)
                .setCauseMessage("task.estimatedTime < 0"),
            new Fixture("OK: estimatedTimeが0")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName("テスト作業")
                .setEstimatedTime(0)
                .setEstimatedStartTime(null)
                .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
            new Fixture("NG: estimatedStartTimeがdateの前日")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName("テスト作業")
                .setEstimatedTime(0)
                .setEstimatedStartTime(DateUtil.toDate(2017, 8, 16, 23, 59, 59, 999))
                .setCause(IllegalStateException.class)
                .setCauseMessage("task.date and task.estimatedStartTime is not same day."),
            new Fixture("OK: estimatedStartTimeとdateが同日(1)")
                .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                .setName("テスト作業")
                .setEstimatedTime(0)
                .setEstimatedStartTime(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), null, null)),
            new Fixture("OK: estimatedStartTimeとdateが同日(2)")
                .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                .setName("テスト作業")
                .setEstimatedTime(0)
                .setEstimatedStartTime(DateUtil.toDate(2017, 8, 17, 23, 59, 59, 999))
                .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, DateUtil.toDate(2017, 8, 17, 23, 59, 59, 999), null, null)),
            new Fixture("NG: estimatedStartTimeがdateの翌日")
                .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                .setName("テスト作業")
                .setEstimatedTime(0)
                .setEstimatedStartTime(DateUtil.toDate(2017, 8, 18, 0, 0, 0, 0))
                .setCause(IllegalStateException.class)
                .setCauseMessage("task.date and task.estimatedStartTime is not same day."));
    }

    @Test
    public void test() {
        try {
            /*
             * テスト実行
             */
            UUID id = this.taskService.create(this.fixture.date,
                this.fixture.name,
                this.fixture.estimatedTime,
                this.fixture.estimatedStartTime);

            /*
             * テスト結果検証
             */
            // NGケースなのに正常終了した場合、失敗
            if (this.fixture.cause != null) {
                fail();
            }

            // データ件数を検証
            assertThat(this.taskRepo.count(), is(1L));

            // データ内容を検証
            Task task = this.taskRepo.findOne(id);
            assertThat(task.getId(), is(id));
            assertThat(task.getDate(), is(this.fixture.result.getDate()));
            assertThat(task.getOrderOfDate(), is(this.fixture.result.getOrderOfDate()));
            assertThat(task.getName(), is(this.fixture.result.getName()));
            assertThat(task.getEstimatedTime(), is(this.fixture.result.getEstimatedTime()));
            assertThat(task.getEstimatedStartTime(), is(this.fixture.result.getEstimatedStartTime()));
            assertThat(task.getStartTime(), is(this.fixture.result.getStartTime()));
            assertThat(task.getEndTime(), is(this.fixture.result.getEndTime()));
        } catch (Exception e) {
            // 例外がスローされたのにNG期待値がない場合、失敗
            if (this.fixture.cause == null) {
                e.printStackTrace();
                fail();
            }

            // 例外内容を検証
            assertThat(e.getClass().getName(), is(this.fixture.cause.getName()));
            assertThat(e.getMessage(), is(this.fixture.causeMessage));

            // データ件数を検証
            assertThat(this.taskRepo.count(), is(0L));
        }
    }

}
