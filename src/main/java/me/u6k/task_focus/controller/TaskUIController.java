
package me.u6k.task_focus.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.service.TaskService;
import me.u6k.task_focus.util.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaskUIController {

    private static final Logger L = LoggerFactory.getLogger(TaskUIController.class);

    @Autowired
    private TaskService taskService;

    // FIXME: 日付を指定できるため、findTodayはメソッド名として不適切
    @RequestMapping(value = "/ui/tasks", method = RequestMethod.GET)
    public String findToday(@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
        @ModelAttribute("taskAddForm") TaskAddVO taskAddForm,
        @ModelAttribute("changeDateForm") ChangeDateVO changeDateForm,
        Model model) {
        L.debug("#list: date={}, taskAddForm={}, changeDateForm={}, model={}", date, taskAddForm, changeDateForm, model);

        /*
         * ページ内容、フォームを構築
         */
        // ヘッダーフォームを構築
        changeDateForm.setTargetDate(date);

        // タスク一覧部分を構築
        Date targetDate = date;
        if (targetDate == null) {
            targetDate = new Date();
        }
        model.addAttribute("date", targetDate);

        List<Task> taskList = this.taskService.findByDate(targetDate);
        L.debug("taskService.findByDate: taskList={}", taskList);

        List<Task> availableTaskList = taskList.stream()
            .filter(x -> (x.getActualStartTime() == null || x.getActualTime() == null))
            .collect(Collectors.toList());
        L.debug("availableTaskList.size={}", availableTaskList.size());
        model.addAttribute("availableTaskList", availableTaskList);

        List<Task> finishedTaskList = taskList.stream()
            .filter(x -> (x.getActualStartTime() != null && x.getActualTime() != null))
            .collect(Collectors.toList());
        L.debug("finishedTaskList.size={}", finishedTaskList.size());
        model.addAttribute("finishedTaskList", finishedTaskList);

        // タスク追加フォームを構築
        taskAddForm.setDate(targetDate);

        L.debug("setup model: model={}", model);

        L.debug("return");
        return "tasks";
    }

    @RequestMapping(value = "/ui/tasks/{id}", method = RequestMethod.GET)
    public String findById(@PathVariable String id, @ModelAttribute("changeDateForm") ChangeDateVO changeDateForm, Model model) {
        L.debug("#findById: id={}, changeDateForm={}", id, changeDateForm);

        /*
         * ページ内容を構築
         */
        // タスクをモデルに設定
        Task task = this.taskService.findById(UUID.fromString(id));
        model.addAttribute("task", task);
        L.debug("setup model: model={}", model);

        // 作業日フォームを設定
        changeDateForm.setTargetDate(task.getEstimatedStartTime());
        L.debug("setup form: changeDateForm={}", changeDateForm);

        L.debug("return");
        return "task";
    }

    @RequestMapping(value = "/ui/tasks/add", method = RequestMethod.POST)
    public String add(@Validated @ModelAttribute("form") TaskAddVO form, BindingResult result, Model model) {
        L.debug("add: form={}, result={}, model={}", form, result, model);

        /*
         * 入力チェック
         */
        L.debug("validate: hasErrors={}", result.hasErrors());
        if (result.hasErrors()) {
            L.debug("return");
            return redirectTasks(null);
        }

        /*
         * タスク追加サービスを実行
         */
        Date estimatedStartTime = DateUtils.truncate(form.getDate(), Calendar.DAY_OF_MONTH);

        this.taskService.add(form.getName(), estimatedStartTime, null);
        L.debug("taskService.create: success");

        /*
         * タスク一覧ページにリダイレクト
         */
        L.debug("return");
        return redirectTasks(form.getDate());
    }

    @RequestMapping(value = "/ui/tasks/{id}/update", method = RequestMethod.GET)
    public String updateInit(@PathVariable String id,
        @ModelAttribute("taskUpdateForm") TaskUpdateVO taskUpdateForm,
        @ModelAttribute("changeDateForm") ChangeDateVO changeDateForm,
        Model model) {
        L.debug("#updateInit: id={}, taskUpdateForm={}, changeDateForm={}, model={}", id, taskUpdateForm, changeDateForm, model);

        /*
         * ページ内容、フォームを構築
         */
        // 更新対象タスクを検索、フォームに設定
        Task task = this.taskService.findById(UUID.fromString(id));
        taskUpdateForm.setDate(task.getEstimatedStartTime());
        taskUpdateForm.setName(task.getName());
        taskUpdateForm.setEstimatedStartTimePart(task.getEstimatedStartTime());
        taskUpdateForm.setEstimatedTime(task.getEstimatedTime());
        taskUpdateForm.setActualStartTimePart(task.getActualStartTime());
        taskUpdateForm.setActualTime(task.getActualTime());
        taskUpdateForm.setDescription(task.getDescription());
        L.debug("setup form: taskUpdateForm={}", taskUpdateForm);

        // 作業日フォームを設定
        changeDateForm.setTargetDate(task.getEstimatedStartTime());
        L.debug("setup form: changeDateForm={}", changeDateForm);

        // ページ内容を設定
        model.addAttribute("id", id);

        L.debug("setup model: model={}", model);

        L.debug("return");
        return "tasks-update";
    }

    @RequestMapping(value = "/ui/tasks/{id}/update", method = RequestMethod.POST)
    public String update(@PathVariable String id,
        @Validated @ModelAttribute("taskUpdateForm") TaskUpdateVO taskUpdateForm,
        @ModelAttribute("changeDateForm") ChangeDateVO changeDateForm,
        BindingResult result,
        Model model) {
        L.debug("update: id={}, taskUpdateForm={}, changeDateForm={}, result={}, model={}", id, taskUpdateForm, changeDateForm, result, model);

        /*
         * 入力チェック
         */
        L.debug("validate: hasErrors={}", result.hasErrors());
        if (result.hasErrors()) {
            L.debug("return");
            return "tasks-update";
        }

        /*
         * タスク更新サービスを実行
         */
        // 入力内容をサービス引数に合わせて変換
        UUID taskId = UUID.fromString(id);

        Date estimatedStartTime = DateUtil.buildDatetime(taskUpdateForm.getDate(), taskUpdateForm.getEstimatedStartTimePart());
        Date actualStartTime = null;
        if (taskUpdateForm.getActualStartTimePart() != null) {
            actualStartTime = DateUtil.buildDatetime(taskUpdateForm.getDate(), taskUpdateForm.getActualStartTimePart());
        }

        // サービスを実行
        this.taskService.update(taskId,
            taskUpdateForm.getName(),
            estimatedStartTime,
            taskUpdateForm.getEstimatedTime(),
            actualStartTime,
            taskUpdateForm.getActualTime(),
            taskUpdateForm.getDescription());
        L.debug("taskService.update: success");

        /*
         * タスク一覧ページにリダイレクト
         */
        L.debug("return");
        return "redirect:/ui/tasks/" + id;
    }

    @RequestMapping(value = "/ui/tasks/{id}/remove", method = RequestMethod.POST)
    public String remove(@PathVariable String id) {
        L.debug("remove: id={}", id);

        /*
         * タスク削除サービスを実行
         */
        UUID taskId = UUID.fromString(id);

        Task task = this.taskService.findById(taskId);

        this.taskService.remove(taskId);
        L.debug("taskService.remove: success");

        /*
         * タスク一覧ページにリダイレクト
         */
        L.debug("return");
        return redirectTasks(task.getEstimatedStartTime());
    }

    @RequestMapping(value = "/ui/tasks/changeDate", method = RequestMethod.POST)
    public String changeDate(@Validated @ModelAttribute ChangeDateVO changeDateForm, BindingResult result, Model model) {
        L.debug("changeDate: changeDateForm={}, model={}", changeDateForm, model);

        /*
         * 入力チェック
         */
        L.debug("validate: hasErrors={}", result.hasErrors());
        if (result.hasErrors()) {
            L.debug("return");
            return redirectTasks(null);
        }

        /*
         * タスク一覧ページにリダイレクト
         */
        return redirectTasks(changeDateForm.getTargetDate());
    }

    private String redirectTasks(Date date) {
        String query = "";
        if (date != null && !DateUtils.isSameDay(date, new Date())) {
            query = "?date=" + DateUtil.formatDate(date);
        }

        String path = "redirect:/ui/tasks" + query;
        L.debug("path={}", path);

        return path;
    }

}
