
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
public class AccountUIController {

    @Autowired
    private Twitter twitter;

    @Autowired
    private ConnectionRepository connectionRepository;

    private static final Logger L = LoggerFactory.getLogger(AccountUIController.class);

    @RequestMapping(value = "/accounts/signup", method = RequestMethod.GET)
    public String index() {
        L.debug("#index");

        /*
         * SNS連携状態により、処理を分岐
         */
        if (this.connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            // SNS連携していない場合、サインアップ画面を表示
            L.debug("Twitter unconnect");
            return "connect/signup";
        } else {
            // SNS連携している場合、サインアップを行い、トップ画面にリダイレクト
            L.debug("twitter: id={}", this.twitter.userOperations().getProfileId());
            return "redirect:/";
        }
    }

}
