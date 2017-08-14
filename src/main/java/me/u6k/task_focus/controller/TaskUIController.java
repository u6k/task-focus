
package me.u6k.task_focus.controller;

import java.util.Date;
import java.util.List;

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

}
