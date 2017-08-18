
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

        private CreateTestParameter param;

        public create(CreateTestParameter param) {
            this.param = param;
        }

        @Before
        public void setup() throws Exception {
            new TestContextManager(this.getClass()).prepareTestInstance(this);

            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

            this.taskRepo.deleteAllInBatch();
        }

        public static class CreateTestParameter {

            String testName;

            Date date;

            String name;

            int estimatedTime;

            Date estimatedStartTime;

            Task result;

            Class<? extends Throwable> cause;

            String causeMessage;

            CreateTestParameter(String testName) {
                this.testName = testName;
            }

            @Override
            public String toString() {
                return this.testName;
            }

            CreateTestParameter setDate(Date date) {
                this.date = date;
                return this;
            }

            CreateTestParameter setName(String name) {
                this.name = name;
                return this;
            }

            CreateTestParameter setEstimatedTime(int estimatedTime) {
                this.estimatedTime = estimatedTime;
                return this;
            }

            CreateTestParameter setEstimatedStartTime(Date estimatedStartTime) {
                this.estimatedStartTime = estimatedStartTime;
                return this;
            }

            CreateTestParameter setResult(Task result) {
                this.result = result;
                return this;
            }

            CreateTestParameter setCause(Class<? extends Throwable> cause) {
                this.cause = cause;
                return this;
            }

            CreateTestParameter setCauseMessage(String causeMessage) {
                this.causeMessage = causeMessage;
                return this;
            }

        }

        @Parameters(name = "{0}")
        public static Iterable<CreateTestParameter> getParameters() throws Exception {
            return Arrays.asList(
                new CreateTestParameter("OK: 登録できる")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
                new CreateTestParameter("OK: dateの時分秒ミリ秒がリセットされる")
                    .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
                new CreateTestParameter("NG: dateがnull")
                    .setDate(null)
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.date is null."),
                new CreateTestParameter("NG: nameがnull")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName(null)
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new CreateTestParameter("NG: nameが空文字列")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new CreateTestParameter("NG: nameが空白文字")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName(" ")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new CreateTestParameter("OK: nameの前後に空白文字")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("     テスト作業    ")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
                new CreateTestParameter("NG: estimatedTimeがマイナス")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("テスト作業")
                    .setEstimatedTime(-1)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.estimatedTime < 0"),
                new CreateTestParameter("OK: estimatedTimeが0")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, null, null, null)),
                new CreateTestParameter("NG: estimatedStartTimeがdateの前日")
                    .setDate(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.toDate(2017, 8, 16, 23, 59, 59, 999))
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.date and task.estimatedStartTime is not same day."),
                new CreateTestParameter("OK: estimatedStartTimeとdateが同日(1)")
                    .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0))
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), null, null)),
                new CreateTestParameter("OK: estimatedStartTimeとdateが同日(2)")
                    .setDate(DateUtil.toDate(2017, 8, 17, 23, 45, 56, 987))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.toDate(2017, 8, 17, 23, 59, 59, 999))
                    .setResult(new Task(null, DateUtil.toDate(2017, 8, 17, 0, 0, 0, 0), 0, "テスト作業", 60, DateUtil.toDate(2017, 8, 17, 23, 59, 59, 999), null, null)),
                new CreateTestParameter("NG: estimatedStartTimeがdateの翌日")
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

    @RunWith(Parameterized.class)
    @SpringBootTest
    public static class update {

        @Autowired
        private TaskService taskService;

        @Autowired
        private TaskRepository taskRepo;

        private UpdateTestParameter param;

        private Task task1;

        private Task task2;

        private Task task3;

        private Task task4;

        private Task task5;

        public update(UpdateTestParameter param) {
            this.param = param;
        }

        @Before
        public void setup() throws Exception {
            // DI準備
            new TestContextManager(this.getClass()).prepareTestInstance(this);

            // 環境設定
            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

            // 事前条件クリーンアップ
            this.taskRepo.deleteAllInBatch();

            // 事前条件準備
            UUID id = this.taskService.create(DateUtil.toDate(2017, 8, 12, 23, 59, 59, 999), "テスト作業0", 0, null);
            this.task1 = this.taskRepo.findOne(id);

            id = this.taskService.create(DateUtil.toDate(2017, 8, 13, 0, 0, 0, 0), "テスト作業1", 0, null);
            this.task2 = this.taskRepo.findOne(id);

            id = this.taskService.create(DateUtil.toDate(2017, 8, 13, 12, 34, 56, 987), "テスト作業2", 0, null);
            this.task3 = this.taskRepo.findOne(id);

            id = this.taskService.create(DateUtil.toDate(2017, 8, 13, 23, 59, 59, 999), "テスト作業3", 0, null);
            this.task4 = this.taskRepo.findOne(id);

            id = this.taskService.create(DateUtil.toDate(2017, 8, 14, 0, 0, 0, 0), "テスト作業4", 0, null);
            this.task5 = this.taskRepo.findOne(id);
        }

        public static class UpdateTestParameter {

            String testName;

            Date date;

            String name;

            int estimatedTime;

            Date estimatedStartTime;

            Date startTime;

            Date endTime;

            Task result;

            Class<? extends Throwable> cause;

            String causeMessage;

            UpdateTestParameter(String testName) {
                this.testName = testName;
            }

            @Override
            public String toString() {
                return this.testName;
            }

            public UpdateTestParameter setTestName(String testName) {
                this.testName = testName;
                return this;
            }

            public UpdateTestParameter setDate(Date date) {
                this.date = date;
                return this;
            }

            public UpdateTestParameter setName(String name) {
                this.name = name;
                return this;
            }

            public UpdateTestParameter setEstimatedTime(int estimatedTime) {
                this.estimatedTime = estimatedTime;
                return this;
            }

            public UpdateTestParameter setEstimatedStartTime(Date estimatedStartTime) {
                this.estimatedStartTime = estimatedStartTime;
                return this;
            }

            public UpdateTestParameter setStartTime(Date startTime) {
                this.startTime = startTime;
                return this;
            }

            public UpdateTestParameter setEndTime(Date endTime) {
                this.endTime = endTime;
                return this;
            }

            public UpdateTestParameter setResult(Task result) {
                this.result = result;
                return this;
            }

            public UpdateTestParameter setCause(Class<? extends Throwable> cause) {
                this.cause = cause;
                return this;
            }

            public UpdateTestParameter setCauseMessage(String causeMessage) {
                this.causeMessage = causeMessage;
                return this;
            }

        }

        @Parameters(name = "{0}")
        public static Iterable<UpdateTestParameter> getParameters() throws Exception {
            return Arrays.asList(
                new UpdateTestParameter("OK: 更新できる")
                    .setDate(DateUtil.toDate(2017, 8, 11, 1, 2, 3, 456))
                    .setName("テスト作業 - 更新済み")
                    .setEstimatedTime(123)
                    .setEstimatedStartTime(DateUtil.toDate(2017, 8, 11, 2, 3, 4, 567))
                    .setStartTime(DateUtil.toDate(2017, 8, 11, 3, 4, 5, 678))
                    .setEndTime(DateUtil.toDate(2017, 8, 11, 23, 59, 59, 999))
                    .setResult(new Task(null,
                        DateUtil.toDate(2017, 8, 11, 0, 0, 0, 0),
                        0,
                        "テスト作業 - 更新済み",
                        123,
                        DateUtil.toDate(2017, 8, 11, 2, 3, 4, 567),
                        DateUtil.toDate(2017, 8, 11, 3, 4, 5, 678),
                        DateUtil.toDate(2017, 8, 11, 23, 59, 59, 999))));
            // FIXME: テストを追加
        }

        @Test
        public void test() {
            try {
                /*
                 * テスト実行
                 */
                this.taskService.update(this.task1.getId(),
                    this.param.date,
                    this.param.name,
                    this.param.estimatedTime,
                    this.param.estimatedStartTime,
                    this.param.startTime,
                    this.param.endTime);

                /*
                 * テスト結果検証
                 */
                // NGケースなのに正常終了した場合、失敗
                if (this.param.cause != null) {
                    fail();
                }

                // データ件数を検証
                assertThat(this.taskRepo.count(), is(5L));

                // データ内容を検証
                Task task = this.taskRepo.findOne(this.task1.getId());
                assertThat(task.getDate(), is(this.param.result.getDate()));
                assertThat(task.getOrderOfDate(), is(this.param.result.getOrderOfDate()));
                assertThat(task.getName(), is(this.param.result.getName()));
                assertThat(task.getEstimatedTime(), is(this.param.result.getEstimatedTime()));
                assertThat(task.getEstimatedStartTime(), is(this.param.result.getEstimatedStartTime()));
                assertThat(task.getStartTime(), is(this.param.result.getStartTime()));
                assertThat(task.getEndTime(), is(this.param.result.getEndTime()));

                task = this.taskRepo.findOne(this.task2.getId());
                assertThat(task, is(this.task2));

                task = this.taskRepo.findOne(this.task3.getId());
                assertThat(task, is(this.task3));

                task = this.taskRepo.findOne(this.task4.getId());
                assertThat(task, is(this.task4));

                task = this.taskRepo.findOne(this.task5.getId());
                assertThat(task, is(this.task5));
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
                assertThat(this.taskRepo.count(), is(5L));

                // データ内容を検証
                Task task = this.taskRepo.findOne(this.task1.getId());
                assertThat(task, is(this.task1));

                task = this.taskRepo.findOne(this.task2.getId());
                assertThat(task, is(this.task2));

                task = this.taskRepo.findOne(this.task3.getId());
                assertThat(task, is(this.task3));

                task = this.taskRepo.findOne(this.task4.getId());
                assertThat(task, is(this.task4));

                task = this.taskRepo.findOne(this.task5.getId());
                assertThat(task, is(this.task5));
            }
        }

    }

}
