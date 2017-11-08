
package me.u6k.task_focus.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
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
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

@RunWith(Enclosed.class)
public class TaskServiceTest {

    // TODO: DB初期化、比較はDBUnitで実施する

    @RunWith(Parameterized.class)
    @SpringBootTest
    public static class add {

        @Autowired
        private TaskService taskService;

        @Autowired
        private TaskRepository taskRepo;

        @Before
        public void setup() throws Exception {
            new TestContextManager(this.getClass()).prepareTestInstance(this);

            Locale.setDefault(Locale.JAPANESE);
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));

            this.taskRepo.deleteAllInBatch();
        }

        @Parameters(name = "{0}")
        public static Collection<Object[]> parameters() {
            return Arrays.asList(new Object[][] {
                {
                    "OK: 登録できる",
                    "テスト作業",
                    DateUtil.parseFullDatetime("2017-08-17 23:45:56.000"),
                    60,
                    new Task(null, "テスト作業", DateUtil.parseFullDatetime("2017-08-17 23:45:56.000"), 60, null, null),
                    null
                },
                {
                    "NG: nameがnull",
                    null,
                    DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"),
                    60,
                    null,
                    new IllegalArgumentException("name is blank")
                },
                {
                    "NG: nameが空文字列",
                    "",
                    DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"),
                    60,
                    null,
                    new IllegalArgumentException("name is blank")
                },
                {
                    "NG: nameが空白文字",
                    " ",
                    DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"),
                    60,
                    null,
                    new IllegalArgumentException("name is blank")
                },
                {
                    "OK: nameの前後に空白文字",
                    "     テスト作業    ",
                    DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"),
                    60,
                    new Task(null, "テスト作業", DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), 60, null, null),
                    null
                },
                {
                    "OK: estimatedStartTimeがnull",
                    "テスト作業",
                    null,
                    60,
                    null,
                    new IllegalArgumentException("estimatedStartTime is null")
                },
                {
                    "OK: estimatedTimeがnull",
                    "テスト作業",
                    DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"),
                    null,
                    new Task(null, "テスト作業", DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), null, null, null),
                    null
                },
                {
                    "NG: estimatedTimeがマイナス",
                    "テスト作業",
                    DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"),
                    -123,
                    null,
                    new IllegalArgumentException("estimatedTime < 0: estimatedTime=-123")
                },
                {
                    "OK: estimatedTimeが0",
                    "テスト作業",
                    DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"),
                    0,
                    new Task(null, "テスト作業", DateUtil.parseFullDatetime("2017-08-17 00:00:00.000"), 0, null, null),
                    null
                },
            });
        }

        @Parameter(0)
        public String testName;

        @Parameter(1)
        public String name;

        @Parameter(2)
        public Date estimatedStartTime;

        @Parameter(3)
        public Integer estimatedTime;

        @Parameter(4)
        public Task result;

        @Parameter(5)
        public Exception cause;

        @Test
        public void test() {
            try {
                /*
                 * テスト実行
                 */
                UUID id = this.taskService.add(this.name,
                    this.estimatedStartTime,
                    this.estimatedTime);

                /*
                 * テスト結果検証
                 */
                // NGケースなのに正常終了した場合、失敗
                if (this.cause != null) {
                    fail();
                }

                // データ件数を検証
                assertThat(this.taskRepo.count(), is(1L));

                // データ内容を検証
                Task task = this.taskRepo.findOne(id);
                assertThat(task.getId(), is(id));
                assertThat(task.getName(), is(this.result.getName()));
                assertThat(task.getEstimatedStartTime().getTime(), is(this.result.getEstimatedStartTime().getTime()));
                if (this.result != null) {
                    assertThat(task.getEstimatedTime(), is(this.result.getEstimatedTime()));
                    if (task.getActualStartTime() != null) {
                        assertThat(task.getActualStartTime().getTime(), is(this.result.getActualStartTime().getTime()));
                    } else {
                        assertNull(this.result.getActualStartTime());
                    }
                    assertThat(task.getActualTime(), is(this.result.getActualTime()));
                }
            } catch (Exception e) {
                // 例外がスローされたのにNG期待値がない場合、失敗
                if (this.cause == null) {
                    e.printStackTrace();
                    fail();
                }

                // 例外内容を検証
                assertThat(e.getClass().getName(), is(this.cause.getClass().getName()));
                assertThat(e.getMessage(), is(this.cause.getMessage()));

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

        @Before
        public void setup() throws Exception {
            // DI準備
            new TestContextManager(this.getClass()).prepareTestInstance(this);

            Locale.setDefault(Locale.JAPANESE);
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));

            // 事前条件クリーンアップ
            this.taskRepo.deleteAllInBatch();

            // 事前条件準備
            UUID id = this.taskService.add("テスト作業0", DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), 0);
            this.task1 = this.taskRepo.findOne(id);

            id = this.taskService.add("テスト作業1", DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), 0);
            this.task2 = this.taskRepo.findOne(id);

            id = this.taskService.add("テスト作業2", DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), 0);
            this.task3 = this.taskRepo.findOne(id);

            id = this.taskService.add("テスト作業3", DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), 0);
            this.task4 = this.taskRepo.findOne(id);

            id = this.taskService.add("テスト作業4", DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), 0);
            this.task5 = this.taskRepo.findOne(id);
        }

        @Parameters(name = "{0}")
        public static Collection<Object[]> parameters() {
            return Arrays.asList(new Object[][] {
                {
                    "OK: 更新できる",
                    "テスト作業 - 更新済み",
                    DateUtil.parseFullDatetime("2017-08-11 01:02:03.000"),
                    123,
                    DateUtil.parseFullDatetime("2017-08-11 02:03:04.000"),
                    23,
                    new Task(null,
                        "テスト作業 - 更新済み",
                        DateUtil.parseFullDatetime("2017-08-11 01:02:03.000"),
                        123,
                        DateUtil.parseFullDatetime("2017-08-11 02:03:04.000"),
                        23),
                    null
                }
            });
            // FIXME: テストを追加
        }

        private Task task1;

        private Task task2;

        private Task task3;

        private Task task4;

        private Task task5;

        @Parameter(0)
        public String testName;

        @Parameter(1)
        public String name;

        @Parameter(2)
        public Date estimatedStartTime;

        @Parameter(3)
        public Integer estimatedTime;

        @Parameter(4)
        public Date actualStartTime;

        @Parameter(5)
        public Integer actualTime;

        @Parameter(6)
        public Task result;

        @Parameter(7)
        public Exception cause;

        @Test
        public void test() {
            try {
                /*
                 * テスト実行
                 */
                this.taskService.update(this.task1.getId(),
                    this.name,
                    this.estimatedStartTime,
                    this.estimatedTime,
                    this.actualStartTime,
                    this.actualTime);

                /*
                 * テスト結果検証
                 */
                // NGケースなのに正常終了した場合、失敗
                if (this.cause != null) {
                    fail();
                }

                // データ件数を検証
                assertThat(this.taskRepo.count(), is(5L));

                // データ内容を検証
                Task task = this.taskRepo.findOne(this.task1.getId());
                assertThat(task.getName(), is(this.result.getName()));
                assertThat(task.getEstimatedStartTime().getTime(), is(this.result.getEstimatedStartTime().getTime()));

                if (this.result != null) {
                    assertThat(task.getEstimatedTime(), is(this.result.getEstimatedTime()));
                    if (task.getActualStartTime() != null) {
                        assertThat(task.getActualStartTime().getTime(), is(this.result.getActualStartTime().getTime()));
                    } else {
                        assertNull(this.result.getActualStartTime());
                    }
                    assertThat(task.getActualTime(), is(this.result.getActualTime()));
                }

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
                if (this.cause == null) {
                    e.printStackTrace();
                    fail();
                }

                // 例外内容を検証
                assertThat(e.getClass().getName(), is(this.cause.getClass().getName()));
                assertThat(e.getMessage(), is(this.cause.getMessage()));

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
    public static class remove {

        @Autowired
        private TaskService taskService;

        @Autowired
        private TaskRepository taskRepo;

        @Before
        public void setup() throws Exception {
            // DI準備
            new TestContextManager(this.getClass()).prepareTestInstance(this);

            Locale.setDefault(Locale.JAPANESE);
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));

            // 事前条件クリーンアップ
            this.taskRepo.deleteAllInBatch();

            // 事前条件準備
            this.task1 = new Task(UUID.fromString("f37826b7-759a-471e-ab98-e4b7fc406d7a"), "テスト作業1", DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), null, null, null);
            this.taskRepo.save(this.task1);

            this.task2 = new Task(UUID.fromString("ef089e3b-c51c-4dbf-aec4-0ebbfb00a5c0"), "テスト作業2", DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), null, null, null);
            this.taskRepo.save(this.task2);

            this.task3 = new Task(UUID.fromString("fb018cd8-85a0-4108-99d8-12eb56edfa29"), "テスト作業3", DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), null, null, null);
            this.taskRepo.save(this.task3);

            this.task4 = new Task(UUID.fromString("e9c212b0-40bf-4cc6-bc9f-f51277f8f5d8"), "テスト作業4", DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), null, null, null);
            this.taskRepo.save(this.task4);

            this.task5 = new Task(UUID.fromString("7d5d8e20-2f6a-4aea-9069-b80b54068c02"), "テスト作業5", DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), null, null, null);
            this.taskRepo.save(this.task5);
        }

        @Parameters(name = "{0}")
        public static Collection<Object[]> parameters() {
            return Arrays.asList(new Object[][] {
                {
                    "OK: 削除できる",
                    UUID.fromString("ef089e3b-c51c-4dbf-aec4-0ebbfb00a5c0"),
                    4L,
                    null
                },
                {
                    "NG: idがnull",
                    null,
                    5L,
                    new IllegalArgumentException("id is null")
                },
                {
                    "NG: 作業が存在しない",
                    UUID.fromString("0df2f094-721a-4ad9-946a-34d523d5ee10"),
                    5L,
                    new IllegalArgumentException("task not found: id=0df2f094-721a-4ad9-946a-34d523d5ee10")
                }
            });
        }

        private Task task1;

        private Task task2;

        private Task task3;

        private Task task4;

        private Task task5;

        @Parameter(0)
        public String testName;

        @Parameter(1)
        public UUID id;

        @Parameter(2)
        public long result;

        @Parameter(3)
        public Throwable cause;

        @Test
        public void test() {
            try {
                /*
                 * テスト実行
                 */
                this.taskService.remove(this.id);

                /*
                 * テスト結果検証
                 */
                // NGケースなのに正常終了した場合、失敗
                if (this.cause != null) {
                    fail();
                }

                // データ件数を検証
                assertThat(this.taskRepo.count(), is(this.result));
            } catch (Exception e) {
                // 例外がスローされたのにNG期待値がない場合、失敗
                if (this.cause == null) {
                    e.printStackTrace();
                    fail();
                }

                // 例外内容を検証
                assertThat(e.getClass().getName(), is(this.cause.getClass().getName()));
                assertThat(e.getMessage(), is(this.cause.getMessage()));

                // データ件数を検証
                assertThat(this.taskRepo.count(), is(this.result));
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

        @Before
        public void setup() throws Exception {
            new TestContextManager(this.getClass()).prepareTestInstance(this);

            Locale.setDefault(Locale.JAPANESE);
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tokyo"));

            this.taskRepo.deleteAllInBatch();

            this.taskService.add("テスト作業0", DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), null);
            this.taskService.add("テスト作業1", DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), null);
            this.taskService.add("テスト作業2", DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), null);
            this.taskService.add("テスト作業3", DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), null);
            this.taskService.add("テスト作業4", DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), null);
        }

        @Parameters(name = "{0}")
        public static Collection<Object[]> parameters() {
            return Arrays.asList(new Object[][] {
                {
                    "OK: データが0件",
                    true,
                    new Date(),
                    Arrays.asList()
                },
                {
                    "OK: 2017/8/11のデータが0件",
                    false,
                    DateUtil.parseFullDatetime("2017-08-11 23:59:59.999"),
                    Arrays.asList()
                },
                {
                    "OK: 2017/8/12のデータがヒット(1)",
                    false,
                    DateUtil.parseFullDatetime("2017-08-12 00:00:00.000"),
                    Arrays.asList(
                        new Task(null, "テスト作業0", DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), null, null, null))
                },
                {
                    "OK: 2017/8/12のデータがヒット(2)",
                    false,
                    DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"),
                    Arrays.asList(
                        new Task(null, "テスト作業0", DateUtil.parseFullDatetime("2017-08-12 23:59:59.999"), null, null, null))
                },
                {
                    "OK: 2017/8/13のデータがヒット(1)",
                    false,
                    DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"),
                    Arrays.asList(
                        new Task(null, "テスト作業1", DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), null, null, null),
                        new Task(null, "テスト作業2", DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), null, null, null),
                        new Task(null, "テスト作業3", DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), null, null, null))
                },
                {
                    "OK: 2017/8/13のデータがヒット(2)",
                    false,
                    DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"),
                    Arrays.asList(
                        new Task(null, "テスト作業1", DateUtil.parseFullDatetime("2017-08-13 00:00:00.000"), null, null, null),
                        new Task(null, "テスト作業2", DateUtil.parseFullDatetime("2017-08-13 12:34:56.987"), null, null, null),
                        new Task(null, "テスト作業3", DateUtil.parseFullDatetime("2017-08-13 23:59:59.999"), null, null, null))
                },
                {
                    "OK: 2017/8/14のデータがヒット(1)",
                    false,
                    DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"),
                    Arrays.asList(
                        new Task(null, "テスト作業4", DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), null, null, null))
                },
                {
                    "OK: 2017/8/14のデータがヒット(2)",
                    false,
                    DateUtil.parseFullDatetime("2017-08-14 23:59:59.999"),
                    Arrays.asList(
                        new Task(null, "テスト作業4", DateUtil.parseFullDatetime("2017-08-14 00:00:00.000"), null, null, null))
                },
                {
                    "OK: 2017/8/15のデータが0件",
                    false,
                    DateUtil.parseFullDatetime("2017-08-15 00:00:00.000"),
                    Arrays.asList()
                }
            });
        }

        @Parameter(0)
        public String testName;

        @Parameter(1)
        public boolean beforeCleanup = false;

        @Parameter(2)
        public Date date;

        @Parameter(3)
        public List<Task> result;

        @Test
        public void test() {
            this.taskRepo.findAll().forEach(System.out::println); // FIXME

            /*
             * 事前準備
             */
            // 必要な場合、事前クリーンアップ
            if (this.beforeCleanup) {
                this.taskRepo.deleteAllInBatch();
                assertThat(this.taskRepo.count(), is(0L));
            }

            /*
             * テスト実行
             */
            List<Task> taskList = this.taskService.findByDate(this.date);

            /*
             * テスト結果検証
             */
            // データ件数を検証
            assertThat(taskList.size(), is(this.result.size()));

            // データ内容を検証
            for (int i = 0; i < this.result.size(); i++) {
                Task expected = this.result.get(i);
                Task actual = taskList.get(i);

                assertThat(expected.getName(), is(actual.getName()));
                assertThat(expected.getEstimatedStartTime().getTime(), is(actual.getEstimatedStartTime().getTime()));
                assertThat(expected.getEstimatedTime(), is(actual.getEstimatedTime()));
                if (expected.getActualStartTime() != null) {
                    assertThat(expected.getActualStartTime().getTime(), is(actual.getActualStartTime().getTime()));
                } else {
                    assertNull(actual.getActualStartTime());
                }
                assertThat(expected.getActualTime(), is(expected.getActualTime()));
            }
        }

    }

}
