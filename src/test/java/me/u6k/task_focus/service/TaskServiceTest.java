
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
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

@RunWith(Enclosed.class)
public class TaskServiceTest {

    @RunWith(Parameterized.class)
    @SpringBootTest
    public static class create {

        @Autowired
        private TaskService taskService;

        @Autowired
        private TaskRepository taskRepo;

        private TestParameter param;

        public create(TestParameter param) {
            this.param = param;
        }

        @Before
        public void setup() throws Exception {
            new TestContextManager(this.getClass()).prepareTestInstance(this);

            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

            this.taskRepo.deleteAllInBatch();
        }

        public static class TestParameter {

            String testName;

            Date date;

            String name;

            int estimatedTime;

            Date estimatedStartTime;

            Task result;

            Class<? extends Throwable> cause;

            String causeMessage;

            TestParameter(String testName) {
                this.testName = testName;
            }

            @Override
            public String toString() {
                return this.testName;
            }

            TestParameter setDate(Date date) {
                this.date = date;
                return this;
            }

            TestParameter setName(String name) {
                this.name = name;
                return this;
            }

            TestParameter setEstimatedTime(int estimatedTime) {
                this.estimatedTime = estimatedTime;
                return this;
            }

            TestParameter setEstimatedStartTime(Date estimatedStartTime) {
                this.estimatedStartTime = estimatedStartTime;
                return this;
            }

            TestParameter setResult(Task result) {
                this.result = result;
                return this;
            }

            TestParameter setCause(Class<? extends Throwable> cause) {
                this.cause = cause;
                return this;
            }

            TestParameter setCauseMessage(String causeMessage) {
                this.causeMessage = causeMessage;
                return this;
            }

        }

        @Parameters(name = "{0}")
        public static Iterable<TestParameter> getParameters() throws Exception {
            return Arrays.asList(
                new TestParameter("OK: 登録できる")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
                new TestParameter("OK: dateの時分秒ミリ秒がリセットされる")
                    .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
                new TestParameter("NG: dateがnull")
                    .setDate(null)
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.date is null."),
                new TestParameter("NG: nameがnull")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName(null)
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new TestParameter("NG: nameが空文字列")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new TestParameter("NG: nameが空白文字")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName(" ")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new TestParameter("OK: nameの前後に空白文字")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("     テスト作業    ")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
                new TestParameter("NG: estimatedTimeがマイナス")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("テスト作業")
                    .setEstimatedTime(-1)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.estimatedTime < 0"),
                new TestParameter("OK: estimatedTimeが0")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
                new TestParameter("NG: estimatedStartTimeがdateの前日")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.toDate(2017, 8, 16, 23, 59, 59, 999))
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.date and task.estimatedStartTime is not same day."),
                new TestParameter("OK: estimatedStartTimeとdateが同日(1)")
                    .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), null, null)),
                new TestParameter("OK: estimatedStartTimeとdateが同日(2)")
                    .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.toDate(2017, 8, 17, 23, 59, 59, 999))
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, DateUtil.toDate(2017, 8, 17, 23, 59, 59, 999), null, null)),
                new TestParameter("NG: estimatedStartTimeがdateの翌日")
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
                UUID id = this.taskService.create(this.param.date,
                    this.param.name,
                    this.param.estimatedTime,
                    this.param.estimatedStartTime);

                /*
                 * テスト結果検証
                 */
                // NGケースなのに正常終了した場合、失敗
                if (this.param.cause != null) {
                    fail();
                }

                // データ件数を検証
                assertThat(this.taskRepo.count(), is(1L));

                // データ内容を検証
                Task task = this.taskRepo.findOne(id);
                assertThat(task.getId(), is(id));
                assertThat(task.getDate(), is(this.param.result.getDate()));
                assertThat(task.getOrderOfDate(), is(this.param.result.getOrderOfDate()));
                assertThat(task.getName(), is(this.param.result.getName()));
                assertThat(task.getEstimatedTime(), is(this.param.result.getEstimatedTime()));
                assertThat(task.getEstimatedStartTime(), is(this.param.result.getEstimatedStartTime()));
                assertThat(task.getStartTime(), is(this.param.result.getStartTime()));
                assertThat(task.getEndTime(), is(this.param.result.getEndTime()));
            } catch (Exception e) {
                // 例外がスローされたのにNG期待値がない場合、失敗
                if (this.param.cause == null) {
                    e.printStackTrace();
                    fail();
                }

                // 例外内容を検証
                assertThat(e.getClass().getName(), is(this.param.cause.getName()));
                assertThat(e.getMessage(), is(this.param.causeMessage));

                // データ件数を検証
                assertThat(this.taskRepo.count(), is(0L));
            }
        }

    }

}
