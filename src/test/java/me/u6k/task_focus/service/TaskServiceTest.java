
package me.u6k.task_focus.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), 0, "テスト作業", 60, null, null, null)),
                new CreateTestParameter("OK: dateの時分秒ミリ秒がリセットされる")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 23:45:56.987"))
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), 0, "テスト作業", 60, null, null, null)),
                new CreateTestParameter("NG: dateがnull")
                    .setDate(null)
                    .setName("テスト作業")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.date is null."),
                new CreateTestParameter("NG: nameがnull")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName(null)
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new CreateTestParameter("NG: nameが空文字列")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName("")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new CreateTestParameter("NG: nameが空白文字")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName(" ")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.name is blank."),
                new CreateTestParameter("OK: nameの前後に空白文字")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName("     テスト作業    ")
                    .setEstimatedTime(60)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), 0, "テスト作業", 60, null, null, null)),
                new CreateTestParameter("NG: estimatedTimeがマイナス")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName("テスト作業")
                    .setEstimatedTime(-1)
                    .setEstimatedStartTime(null)
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.estimatedTime < 0"),
                new CreateTestParameter("OK: estimatedTimeが0")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(null)
                    .setResult(new Task(null, DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), 0, "テスト作業", 0, null, null, null)),
                new CreateTestParameter("NG: estimatedStartTimeがdateの前日")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.parseFullDatetime("2017-08-16 23:59:59.999"))
                    .setCause(IllegalStateException.class)
                    .setCauseMessage("task.date and task.estimatedStartTime is not same day."),
                new CreateTestParameter("OK: estimatedStartTimeとdateが同日(1)")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 23:45:56.987"))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setResult(new Task(null, DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), 0, "テスト作業", 0, DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), null, null)),
                new CreateTestParameter("OK: estimatedStartTimeとdateが同日(2)")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 23:45:56.987"))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.parseFullDatetime("2017-08-17 23:59:59.999"))
                    .setResult(new Task(null, DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), 0, "テスト作業", 0, DateUtil.parseFullDatetime("2017-08-17 23:59:59.999"), null, null)),
                new CreateTestParameter("NG: estimatedStartTimeがdateの翌日")
                    .setDate(DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"))
                    .setName("テスト作業")
                    .setEstimatedTime(0)
                    .setEstimatedStartTime(DateUtil.parseFullDatetime("2017-08-18 00:00:00.000"))
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
                assertThat(task.getDate() != null ? task.getDate().getTime() : null,
                    is(this.param.result.getDate() != null ? this.param.result.getDate().getTime() : null));
                assertThat(task.getOrderOfDate(), is(this.param.result.getOrderOfDate()));
                assertThat(task.getName(), is(this.param.result.getName()));
                assertThat(task.getEstimatedTime(), is(this.param.result.getEstimatedTime()));
                assertThat(task.getEstimatedStartTime() != null ? task.getEstimatedStartTime().getTime() : null,
                    is(this.param.result.getEstimatedStartTime() != null ? this.param.result.getEstimatedStartTime().getTime() : null));
                assertThat(task.getStartTime() != null ? task.getStartTime().getTime() : null,
                    is(this.param.result.getStartTime() != null ? this.param.result.getStartTime().getTime() : null));
                assertThat(task.getEndTime() != null ? task.getEndTime().getTime() : null,
                    is(this.param.result.getEndTime() != null ? this.param.result.getEndTime().getTime() : null));
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
            UUID id = this.taskService.create(DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), "テスト作業0", 0, null);
            this.task1 = this.taskRepo.findOne(id);

            id = this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), "テスト作業1", 0, null);
            this.task2 = this.taskRepo.findOne(id);

            id = this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), "テスト作業2", 0, null);
            this.task3 = this.taskRepo.findOne(id);

            id = this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), "テスト作業3", 0, null);
            this.task4 = this.taskRepo.findOne(id);

            id = this.taskService.create(DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), "テスト作業4", 0, null);
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
                    .setDate(DateUtil.parseFullDatetime("2017-08-11 01:02:03.456"))
                    .setName("テスト作業 - 更新済み")
                    .setEstimatedTime(123)
                    .setEstimatedStartTime(DateUtil.parseFullDatetime("2017-08-11 02:03:04.567"))
                    .setStartTime(DateUtil.parseFullDatetime("2017-08-11 03:04:05.678"))
                    .setEndTime(DateUtil.parseFullDatetime("2017-08-11 23:59:59.999"))
                    .setResult(new Task(null,
                        DateUtil.parseFullDatetime("2017-08-11 00:00:00.000"),
                        0,
                        "テスト作業 - 更新済み",
                        123,
                        DateUtil.parseFullDatetime("2017-08-11 02:03:04.567"),
                        DateUtil.parseFullDatetime("2017-08-11 03:04:05.678"),
                        DateUtil.parseFullDatetime("2017-08-11 23:59:59.999"))));
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
                assertThat(task.getDate() != null ? task.getDate().getTime() : null,
                    is(this.param.result.getDate() != null ? this.param.result.getDate().getTime() : null));
                assertThat(task.getOrderOfDate(), is(this.param.result.getOrderOfDate()));
                assertThat(task.getName(), is(this.param.result.getName()));
                assertThat(task.getEstimatedTime(), is(this.param.result.getEstimatedTime()));
                assertThat(task.getEstimatedStartTime() != null ? task.getEstimatedStartTime().getTime() : null,
                    is(this.param.result.getEstimatedStartTime() != null ? this.param.result.getEstimatedStartTime().getTime() : null));
                assertThat(task.getStartTime() != null ? task.getStartTime().getTime() : null,
                    is(this.param.result.getStartTime() != null ? this.param.result.getStartTime().getTime() : null));
                assertThat(task.getEndTime() != null ? task.getEndTime().getTime() : null,
                    is(this.param.result.getEndTime() != null ? this.param.result.getEndTime().getTime() : null));

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

    @RunWith(Parameterized.class)
    @SpringBootTest
    public static class delete {

        @Autowired
        private TaskService taskService;

        @Autowired
        private TaskRepository taskRepo;

        private DeleteTestParameter param;

        private Task task1;

        private Task task2;

        private Task task3;

        private Task task4;

        private Task task5;

        public delete(DeleteTestParameter param) {
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
            this.task1 = new Task(UUID.fromString("f37826b7-759a-471e-ab98-e4b7fc406d7a"), DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), 0, "テスト作業1", 0, null, null, null);
            this.taskRepo.save(this.task1);

            this.task2 = new Task(UUID.fromString("ef089e3b-c51c-4dbf-aec4-0ebbfb00a5c0"), DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), 0, "テスト作業2", 0, null, null, null);
            this.taskRepo.save(this.task2);

            this.task3 = new Task(UUID.fromString("fb018cd8-85a0-4108-99d8-12eb56edfa29"), DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), 0, "テスト作業3", 0, null, null, null);
            this.taskRepo.save(this.task3);

            this.task4 = new Task(UUID.fromString("e9c212b0-40bf-4cc6-bc9f-f51277f8f5d8"), DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), 0, "テスト作業4", 0, null, null, null);
            this.taskRepo.save(this.task4);

            this.task5 = new Task(UUID.fromString("7d5d8e20-2f6a-4aea-9069-b80b54068c02"), DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), 0, "テスト作業5", 0, null, null, null);
            this.taskRepo.save(this.task5);
        }

        public static class DeleteTestParameter {

            String testName;

            UUID id;

            long result;

            Class<? extends Throwable> cause;

            String causeMessage;

            DeleteTestParameter(String testName) {
                this.testName = testName;
            }

            @Override
            public String toString() {
                return this.testName;
            }

            public DeleteTestParameter setTestName(String testName) {
                this.testName = testName;
                return this;
            }

            public DeleteTestParameter setId(UUID id) {
                this.id = id;
                return this;
            }

            public DeleteTestParameter setResult(long result) {
                this.result = result;
                return this;
            }

            public DeleteTestParameter setCause(Class<? extends Throwable> cause) {
                this.cause = cause;
                return this;
            }

            public DeleteTestParameter setCauseMessage(String causeMessage) {
                this.causeMessage = causeMessage;
                return this;
            }

        }

        @Parameters(name = "{0}")
        public static Iterable<DeleteTestParameter> getParameters() throws Exception {
            return Arrays.asList(
                new DeleteTestParameter("OK: 削除できる")
                    .setId(UUID.fromString("ef089e3b-c51c-4dbf-aec4-0ebbfb00a5c0"))
                    .setResult(4L),
                new DeleteTestParameter("NG: idがnull")
                    .setId(null)
                    .setResult(5L)
                    .setCause(IllegalArgumentException.class)
                    .setCauseMessage("id is null."),
                new DeleteTestParameter("NG: 作業が存在しない")
                    .setId(UUID.fromString("0df2f094-721a-4ad9-946a-34d523d5ee10"))
                    .setResult(5L)
                    .setCause(IllegalArgumentException.class)
                    .setCauseMessage("task not found. id=0df2f094-721a-4ad9-946a-34d523d5ee10"));
        }

        @Test
        public void test() {
            try {
                /*
                 * テスト実行
                 */
                this.taskService.delete(this.param.id);

                /*
                 * テスト結果検証
                 */
                // NGケースなのに正常終了した場合、失敗
                if (this.param.cause != null) {
                    fail();
                }

                // データ件数を検証
                assertThat(this.taskRepo.count(), is(this.param.result));
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
                assertThat(this.taskRepo.count(), is(this.param.result));
            }
        }

    }

    @RunWith(Parameterized.class)
    @SpringBootTest
    public static class findByDate {

        @Autowired
        private TaskService taskService;

        @Autowired
        private TaskRepository taskRepo;

        private FindByDateTestParameter param;

        public findByDate(FindByDateTestParameter param) {
            this.param = param;
        }

        @Before
        public void setup() throws Exception {
            new TestContextManager(this.getClass()).prepareTestInstance(this);

            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

            this.taskRepo.deleteAllInBatch();

            this.taskService.create(DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), "テスト作業0", 0, null);
            this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), "テスト作業1", 0, null);
            this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), "テスト作業2", 0, null);
            this.taskService.create(DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), "テスト作業3", 0, null);
            this.taskService.create(DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), "テスト作業4", 0, null);
        }

        public static class FindByDateTestParameter {

            String testName;

            boolean beforeCleanup = false;

            Date date;

            List<Task> result;

            FindByDateTestParameter(String testName) {
                this.testName = testName;
            }

            @Override
            public String toString() {
                return this.testName;
            }

            FindByDateTestParameter setBeforeCleanup(boolean beforeCleanup) {
                this.beforeCleanup = beforeCleanup;
                return this;
            }

            FindByDateTestParameter setDate(Date date) {
                this.date = date;
                return this;
            }

            FindByDateTestParameter setResult(List<Task> result) {
                this.result = result;
                return this;
            }

        }

        @Parameters(name = "{0}")
        public static Iterable<FindByDateTestParameter> getParameters() throws Exception {
            return Arrays.asList(
                new FindByDateTestParameter("OK: データが0件")
                    .setBeforeCleanup(true)
                    .setDate(new Date())
                    .setResult(Arrays.asList()),
                new FindByDateTestParameter("OK: 2017/8/11のデータが0件")
                    .setDate(DateUtil.parseFullDatetime("2017-08-11 23:59:59.999"))
                    .setResult(Arrays.asList()),
                new FindByDateTestParameter("OK: 2017/8/12のデータがヒット(1)")
                    .setDate(DateUtil.parseFullDatetime("2017-08-12 00:00:00.000"))
                    .setResult(Arrays.asList(
                        new Task(null, DateUtil.parseFullDatetime("2017-08-12 00:00:00.000"), 0, "テスト作業0", 0, null, null, null))),
                new FindByDateTestParameter("OK: 2017/8/12のデータがヒット(2)")
                    .setDate(DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"))
                    .setResult(Arrays.asList(
                        new Task(null, DateUtil.parseFullDatetime("2017-08-12 00:00:00.000"), 0, "テスト作業0", 0, null, null, null))),
                new FindByDateTestParameter("OK: 2017/8/13のデータがヒット(1)")
                    .setDate(DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"))
                    .setResult(Arrays.asList(
                        new Task(null, DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), 0, "テスト作業1", 0, null, null, null),
                        new Task(null, DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), 0, "テスト作業2", 0, null, null, null),
                        new Task(null, DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), 0, "テスト作業3", 0, null, null, null))),
                new FindByDateTestParameter("OK: 2017/8/13のデータがヒット(2)")
                    .setDate(DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"))
                    .setResult(Arrays.asList(
                        new Task(null, DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), 0, "テスト作業1", 0, null, null, null),
                        new Task(null, DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), 0, "テスト作業2", 0, null, null, null),
                        new Task(null, DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), 0, "テスト作業3", 0, null, null, null))),
                new FindByDateTestParameter("OK: 2017/8/14のデータがヒット(1)")
                    .setDate(DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"))
                    .setResult(Arrays.asList(
                        new Task(null, DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), 0, "テスト作業4", 0, null, null, null))),
                new FindByDateTestParameter("OK: 2017/8/14のデータがヒット(2)")
                    .setDate(DateUtil.parseFullDatetime("2017-08-14 23:59:59.999"))
                    .setResult(Arrays.asList(
                        new Task(null, DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), 0, "テスト作業4", 0, null, null, null))),
                new FindByDateTestParameter("OK: 2017/8/15のデータが0件")
                    .setDate(DateUtil.parseFullDatetime("2017-08-15 00:00:00.000"))
                    .setResult(Arrays.asList()));
        }

        @Test
        public void test() {
            /*
             * 事前準備
             */
            // 必要な場合、事前クリーンアップ
            if (this.param.beforeCleanup) {
                this.taskRepo.deleteAllInBatch();
                assertThat(this.taskRepo.count(), is(0L));
            }

            /*
             * テスト実行
             */
            List<Task> taskList = this.taskService.findByDate(this.param.date);

            /*
             * テスト結果検証
             */
            // データ件数を検証
            assertThat(taskList.size(), is(this.param.result.size()));

            // データ内容を検証
            for (int i = 0; i < this.param.result.size(); i++) {
                Task expected = this.param.result.get(i);
                Task actual = taskList.get(i);

                assertThat(expected.getDate() != null ? expected.getDate().getTime() : null,
                    is(actual.getDate() != null ? actual.getDate().getTime() : null));
                assertThat(expected.getOrderOfDate(), is(actual.getOrderOfDate()));
                assertThat(expected.getName(), is(actual.getName()));
                assertThat(expected.getEstimatedTime(), is(actual.getEstimatedTime()));
                assertThat(expected.getEstimatedStartTime() != null ? expected.getEstimatedStartTime().getTime() : null,
                    is(actual.getEstimatedStartTime() != null ? actual.getEstimatedStartTime().getTime() : null));
                assertThat(expected.getStartTime() != null ? expected.getStartTime().getTime() : null,
                    is(actual.getStartTime() != null ? actual.getStartTime().getTime() : null));
                assertThat(expected.getEndTime() != null ? expected.getEndTime().getTime() : null,
                    is(actual.getEndTime() != null ? actual.getEndTime().getTime() : null));
            }
        }

    }

}
