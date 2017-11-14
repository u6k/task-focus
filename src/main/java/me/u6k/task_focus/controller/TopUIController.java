
package me.u6k.task_focus.controller;

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

    private static final Logger L = LoggerFactory.getLogger(TopUIController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        L.debug("#index");

        /*
         * SNS連携状態を確認
         */
        if (this.connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            L.debug("Twitter unconnect");
            return "redirect:/accounts/signup";
        }

        L.debug("twitter: id={}", this.twitter.userOperations().getProfileId());

        /*
         * SNS連携済みの場合、タスク一覧ページにリダイレクト
         */
        L.debug("redirect:/ui/tasks");
        return "redirect:/ui/tasks";
    }

}
