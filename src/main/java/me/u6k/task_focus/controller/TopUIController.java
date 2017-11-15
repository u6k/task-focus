
package me.u6k.task_focus.controller;

import me.u6k.task_focus.model.User;
import me.u6k.task_focus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TopUIController {

    @Autowired
    private Twitter twitter;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserService userService;

    private static final Logger L = LoggerFactory.getLogger(TopUIController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        L.debug("#index");

        /*
         * SNS連携状態を確認
         */
        // SNS未連携の場合、サインアップ画面にリダイレクトする。
        if (this.connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            L.debug("Twitter unconnect");
            return "redirect:/accounts/signup";
        }

        L.debug("twitter: id={}", this.twitter.userOperations().getProfileId());

        // 未サインアップの場合、サインアップ画面にリダイレクトする。
        User user = this.userService.findBySocialAccount(this.twitter);
        if (user == null) {
            L.debug("user not found");
            return "redirect:/accounts/signup";
        }

        /*
         * SNS連携済みの場合、タスク一覧ページにリダイレクト
         */
        String path = "redirect:/ui/users/" + user.getId() + "/tasks";

        L.debug("path={}", path);
        return path;
    }

}
