
package me.u6k.task_focus.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.service.TaskService;
import me.u6k.task_focus.util.DateUtil;
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

        L.debug("return");
        return "redirect:/ui/tasks";
    }

    @RequestMapping(value = "/ui/tasks", method = RequestMethod.GET)
    public String list(Model model) {
        L.debug("#list: model={}", model);

        List<Task> taskList = this.taskService.findByDate(new Date());
        L.debug("taskService.findByDate: taskList={}", taskList);

        model.addAttribute("taskList", taskList);
        L.debug("setup model: taskList={}", taskList);

        L.debug("return");
        return "tasks";
    }

    @RequestMapping(value = "/ui/tasks/add", method = RequestMethod.GET)
    public String addInit(@ModelAttribute("form") TaskVO form, Model model) {
        L.debug("#addInit: form={}, model={}", form, model);

        form.setDate(DateUtil.formatDate(Optional.of(new Date())).get());
        L.debug("setup form: form={}", form);

        L.debug("return");
        return "tasks-add";
    }

    @RequestMapping(value = "/ui/tasks/add", method = RequestMethod.POST)
    public String add(@Validated @ModelAttribute("form") TaskVO form, BindingResult result, Model model) {
        L.debug("add: form={}, result={}, model={}", form, result, model);

        L.debug("validate: hasErrors={}", result.hasErrors());
        if (result.hasErrors()) {
            L.debug("return");
            return "tasks-add";
        }

        try {
            Date date = DateUtil.parseDate(Optional.ofNullable(form.getDate())).orElse(null);
            Date estimatedStartTime = DateUtil.parseHourMinute(Optional.ofNullable(form.getEstimatedStartTime())).orElse(null);

            this.taskService.create(date, form.getName(), form.getEstimatedTime(), estimatedStartTime);
            L.debug("taskService.create: success");
        } catch (IllegalArgumentException | ParseException e) {
            model.addAttribute("error", e.getMessage());
            L.debug("setup model: model={}", model);

            L.debug("return");
            return "tasks-add";
        }

        L.debug("return");
        return "redirect:/ui/tasks";
    }

    @RequestMapping(value = "/ui/tasks/{id}/edit", method = RequestMethod.GET)
    public String editInit(@PathVariable String id, @ModelAttribute("form") TaskEditVO form, Model model) {
        L.debug("#editInit: id={}, form={}, model={}", id, form, model);

        Task task = this.taskService.findById(UUID.fromString(id));
        form.setDate(DateUtil.formatDate(Optional.of(task.getDate())).orElse(null));
        form.setName(task.getName());
        form.setEstimatedTime(task.getEstimatedTime());
        form.setEstimatedStartTime(DateUtil.formatHourMinute(Optional.ofNullable(task.getEstimatedStartTime())).orElse(null));
        form.setStartTime(DateUtil.formatHourMinute(Optional.ofNullable(task.getStartTime())).orElse(null));
        form.setEndTime(DateUtil.formatHourMinute(Optional.ofNullable(task.getEndTime())).orElse(null));
        L.debug("setup form: form={}", form);

        model.addAttribute("id", id);
        L.debug("setup model: model={}", model);

        L.debug("return");
        return "tasks-edit";
    }

    @RequestMapping(value = "/ui/tasks/{id}/edit", method = RequestMethod.POST)
    public String edit(@PathVariable String id, @Validated @ModelAttribute("form") TaskEditVO form, BindingResult result, Model model) {
        L.debug("edit: id={}, form={}, result={}, model={}", id, form, result, model);

        L.debug("validate: hasErrors={}", result.hasErrors());
        if (result.hasErrors()) {
            L.debug("return");
            return "tasks-edit";
        }

        try {
            UUID taskId = UUID.fromString(id);
            Date date = DateUtil.parseDate(Optional.of(form.getDate())).orElse(null);
            Date estimatedStartTime = DateUtil.parseHourMinute(Optional.ofNullable(form.getEstimatedStartTime())).orElse(null);
            Date startTime = DateUtil.parseHourMinute(Optional.ofNullable(form.getStartTime())).orElse(null);
            Date endTime = DateUtil.parseHourMinute(Optional.ofNullable(form.getEndTime())).orElse(null);

            this.taskService.update(taskId, date, form.getName(), form.getEstimatedTime(), estimatedStartTime, startTime, endTime);
            L.debug("taskService.edit: success");
        } catch (IllegalArgumentException | ParseException e) {
            model.addAttribute("error", e.getMessage());
            L.debug("setup model: model={}", model);

            L.debug("return");
            return "tasks-edit";
        }

        L.debug("return");
        return "redirect:/ui/tasks";
    }

}
