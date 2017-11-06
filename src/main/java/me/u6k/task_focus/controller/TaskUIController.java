
package me.u6k.task_focus.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.service.TaskService;
import me.u6k.task_focus.util.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TaskUIController {

    private static final Logger L = LoggerFactory.getLogger(TaskUIController.class);

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        L.debug("#index:");

        /*
         * タスク一覧ページにリダイレクト
         */
        L.debug("return");
        return "redirect:/ui/tasks";
    }

    @RequestMapping(value = "/ui/tasks", method = RequestMethod.GET)
    public String findToday(@ModelAttribute("form") TaskAddVO form, Model model) {
        L.debug("#list: form={}, model={}", form, model);

        /*
         * ページ内容、フォームを構築
         */
        // タスク一覧部分を構築
        Date date = new Date();
        List<Task> taskList = this.taskService.findByDate(date);
        L.debug("taskService.findByDate: taskList={}", taskList);

        model.addAttribute("date", date);
        model.addAttribute("taskList", taskList);
        L.debug("setup model: model={}", model);

        L.debug("return");
        return "tasks";
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
            return "redirect:/ui/tasks";
        }

        /*
         * タスク追加サービスを実行
         */
        Date estimatedStartTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);

        this.taskService.add(form.getName(), estimatedStartTime, null);
        L.debug("taskService.create: success");

        /*
         * タスク一覧ページにリダイレクト
         */
        L.debug("return");
        return "redirect:/ui/tasks";
    }

    @RequestMapping(value = "/ui/tasks/{id}/update", method = RequestMethod.GET)
    public String updateInit(@PathVariable String id, @ModelAttribute("form") TaskUpdateVO form, Model model) {
        L.debug("#updateInit: id={}, form={}, model={}", id, form, model);

        /*
         * ページ内容、フォームを構築
         */
        // 更新対象タスクを検索、フォームに設定
        Task task = this.taskService.findById(UUID.fromString(id));
        form.setDate(task.getEstimatedStartTime());
        form.setName(task.getName());
        form.setEstimatedStartTimePart(task.getEstimatedStartTime());
        form.setEstimatedTime(task.getEstimatedTime());
        form.setActualStartTimePart(task.getActualStartTime());
        form.setActualTime(task.getActualTime());
        L.debug("setup form: form={}", form);

        // ページ内容を設定
        model.addAttribute("id", id);
        L.debug("setup model: model={}", model);

        L.debug("return");
        return "tasks-update";
    }

    @RequestMapping(value = "/ui/tasks/{id}/update", method = RequestMethod.POST)
    public String update(@PathVariable String id, @Validated @ModelAttribute("form") TaskUpdateVO form, BindingResult result, Model model) {
        L.debug("update: id={}, form={}, result={}, model={}", id, form, result, model);

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

        Date estimatedStartTime = DateUtil.buildDatetime(form.getDate(), form.getEstimatedStartTimePart());
        Date actualStartTime = null;
        if (form.getActualStartTimePart() != null) {
            actualStartTime = DateUtil.buildDatetime(form.getDate(), form.getActualStartTimePart());
        }

        // サービスを実行
        this.taskService.update(taskId,
            form.getName(),
            estimatedStartTime,
            form.getEstimatedTime(),
            actualStartTime,
            form.getActualTime());
        L.debug("taskService.update: success");

        /*
         * タスク一覧ページにリダイレクト
         */
        L.debug("return");
        return "redirect:/ui/tasks";
    }

    @RequestMapping(value = "/ui/tasks/{id}/remove", method = RequestMethod.POST)
    public String remove(@PathVariable String id) {
        L.debug("remove: id={}", id);

        /*
         * タスク削除サービスを実行
         */
        UUID taskId = UUID.fromString(id);

        this.taskService.remove(taskId);
        L.debug("taskService.remove: success");

        /*
         * タスク一覧ページにリダイレクト
         */
        L.debug("return");
        return "redirect:/ui/tasks";
    }

}
