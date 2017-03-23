
package me.u6k.task_focus.controller;

import java.util.Date;

import me.u6k.task_focus.service.TaskService;
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

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/ui/tasks/add", method = RequestMethod.GET)
    public String addInit(@ModelAttribute("form") TaskVO form, Model model) {
        form.setDate(new Date());

        return "tasks-add";
    }

    @RequestMapping(value = "/ui/tasks/add", method = RequestMethod.POST)
    public String add(@Validated @ModelAttribute("form") TaskVO form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "tasks-add";
        }

        try {
            this.taskService.create(form.getDate(), form.getName(), form.getEstimatedTime(), form.getEstimatedStartTime());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "tasks-add";
        }

        return "redirect:/ui/tasks";
    }

}
