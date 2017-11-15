
package me.u6k.task_focus.service;

import java.util.UUID;

import me.u6k.task_focus.model.SocialAccount;
import me.u6k.task_focus.model.SocialAccountPK;
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
        L.debug("#add: twitter={}", twitter);

        /*
         * 前提条件確認
         */
        if (twitter == null) {
            throw new IllegalArgumentException("twitter is null");
        }

        /*
         * 既存ユーザー確認
         */
        User existUser = this.findBySocialAccount(twitter);
        L.debug("existUser={}", existUser);

        if (existUser != null) {
            return existUser.getId();
        }

        /*
         * ユーザーを登録
         */
        User newUser = new User(UUID.randomUUID(),
            twitter.userOperations().getUserProfile().getName(),
            twitter.userOperations().getUserProfile().getLocation(),
            twitter.userOperations().getUserProfile().getTimeZone());
        this.userRepo.save(newUser);
        L.debug("saved: newUser={}", newUser);

        SocialAccount socialAccount = new SocialAccount(
            new SocialAccountPK("twitter", Long.toString(twitter.userOperations().getProfileId())),
            twitter.userOperations().getUserProfile().getName(),
            newUser);
        this.socialAccountRepo.save(socialAccount);
        L.debug("saved: socialAccount={}", socialAccount);

        L.debug("return: {}", newUser.getId());
        return newUser.getId();
    }

    public User findBySocialAccount(Twitter twitter) {
        L.debug("#findBySocialAccount: twitter={}", twitter);

        /*
         * 前提条件確認
         */
        if (twitter == null) {
            throw new IllegalArgumentException("twitter is null");
        }

        /*
         * ユーザーを検索
         */
        SocialAccount socialAccount = this.socialAccountRepo.findOne(
            new SocialAccountPK(
                "twitter",
                Long.toString(twitter.userOperations().getProfileId())));
        L.debug("findOne: socialAccount={}", socialAccount);

        if (socialAccount != null) {
            L.debug("return: {}", socialAccount.getUser());
            return socialAccount.getUser();
        } else {
            L.debug("return: null");
            return null;
        }
    }

}
