
package me.u6k.task_focus.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.u6k.task_focus.model.Task;
import me.u6k.task_focus.service.TaskService;
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

        Date date = new Date();
        List<Task> taskList = this.taskService.findByDate(date);
        L.debug("taskService.findByDate: taskList={}", taskList);

        model.addAttribute("date", date);
        model.addAttribute("taskList", taskList);
        L.debug("setup model: model={}", model);

        L.debug("return");
        return "tasks";
    }

    @RequestMapping(value = "/ui/tasks/add", method = RequestMethod.GET)
    public String addInit(@ModelAttribute("form") TaskVO form, Model model) {
        L.debug("#addInit: form={}, model={}", form, model);

        form.setDate(new Date());
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
            this.taskService.create(form.getDate(), form.getName(), form.getEstimatedTime(), form.getEstimatedStartTime());
            L.debug("taskService.create: success");
        } catch (IllegalArgumentException e) {
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

            this.taskService.update(taskId, form.getDate(), form.getName(), form.getEstimatedTime(), form.getEstimatedStartTime(), form.getStartTime(), form.getEndTime());
            L.debug("taskService.edit: success");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            L.debug("setup model: model={}", model);

            L.debug("return");
            return "tasks-edit";
        }

        L.debug("return");
        return "redirect:/ui/tasks";
    }

}
