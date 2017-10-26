
package me.u6k.task_focus.controller;

import java.util.Date;
import java.util.List;
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
    public String list(@ModelAttribute("form") TaskAddVO form, Model model) {
        L.debug("#list: form={}, model={}", form, model);

        Date date = new Date();
        List<Task> taskList = this.taskService.findByDate(date);
        L.debug("taskService.findByDate: taskList={}", taskList);

        model.addAttribute("date", date);
        model.addAttribute("taskList", taskList);
        L.debug("setup model: model={}", model);

        form.setDate(date);
        L.debug("setup form: form={}", form);

        L.debug("return");
        return "tasks";
    }

    @RequestMapping(value = "/ui/tasks/add", method = RequestMethod.POST)
    public String add(@Validated @ModelAttribute("form") TaskAddVO form, BindingResult result, Model model) {
        L.debug("add: form={}, result={}, model={}", form, result, model);

        Date date = form.getDate();
        List<Task> taskList = this.taskService.findByDate(date);
        L.debug("taskService.findByDate: taskList={}", taskList);

        model.addAttribute("date", date);
        model.addAttribute("taskList", taskList);
        L.debug("setup model: model={}", model);

        L.debug("validate: hasErrors={}", result.hasErrors());
        if (result.hasErrors()) {
            L.debug("return");
            return "tasks";
        }

        try {
            this.taskService.add(form.getDate(), form.getName(), 0, null);
            L.debug("taskService.create: success");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            L.debug("setup model: model={}", model);

            L.debug("return");
            return "tasks";
        }

        L.debug("return");
        return "redirect:/ui/tasks";
    }

    @RequestMapping(value = "/ui/tasks/{id}/update", method = RequestMethod.GET)
    public String updateInit(@PathVariable String id, @ModelAttribute("form") TaskUpdateVO form, Model model) {
        L.debug("#updateInit: id={}, form={}, model={}", id, form, model);

        Task task = this.taskService.findById(UUID.fromString(id));
        form.setDate(task.getDate());
        form.setName(task.getName());
        form.setEstimatedTime(task.getEstimatedTime());
        form.setEstimatedStartTime(task.getEstimatedStartTime());
        form.setStartTime(task.getStartTime());
        form.setEndTime(task.getEndTime());
        L.debug("setup form: form={}", form);

        model.addAttribute("id", id);
        L.debug("setup model: model={}", model);

        L.debug("return");
        return "tasks-update";
    }

    @RequestMapping(value = "/ui/tasks/{id}/update", method = RequestMethod.POST)
    public String update(@PathVariable String id, @Validated @ModelAttribute("form") TaskUpdateVO form, BindingResult result, Model model) {
        L.debug("update: id={}, form={}, result={}, model={}", id, form, result, model);

        L.debug("validate: hasErrors={}", result.hasErrors());
        if (result.hasErrors()) {
            L.debug("return");
            return "tasks-update";
        }

        try {
            UUID taskId = UUID.fromString(id);

            this.taskService.update(taskId,
                form.getDate(),
                form.getName(),
                form.getEstimatedTime(),
                form.getDate() != null ? DateUtil.toDatetime(form.getDate(), form.getEstimatedStartTime()) : null,
                form.getDate() != null ? DateUtil.toDatetime(form.getDate(), form.getStartTime()) : null,
                form.getDate() != null ? DateUtil.toDatetime(form.getDate(), form.getEndTime()) : null);
            L.debug("taskService.update: success");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            L.debug("setup model: model={}", model);

            L.debug("return");
            return "tasks-update";
        }

        L.debug("return");
        return "redirect:/ui/tasks";
    }

    @RequestMapping(value = "/ui/tasks/{id}/delete", method = RequestMethod.POST)
    public String delete(@PathVariable String id) {
        L.debug("delete: id={}", id);

        UUID taskId = UUID.fromString(id);

        this.taskService.delete(taskId);
        L.debug("taskService.delete: success");

        L.debug("return");
        return "redirect:/ui/tasks";
    }

}
