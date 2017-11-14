
package me.u6k.task_focus.service;

import java.util.UUID;

import me.u6k.task_focus.model.SocialAccountRepository;
import me.u6k.task_focus.model.User;
import me.u6k.task_focus.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private static final Logger L = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SocialAccountRepository socialAccountRepo;

    public UUID add(Twitter twitter) {
        throw new UnsupportedOperationException();
    }

    public User findBySocialAccount(Twitter twitter) {
        throw new UnsupportedOperationException();
    }

}
