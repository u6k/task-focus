
package me.u6k.task_focus.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.beans.SamePropertyValuesAs.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.u6k.task_focus.model.SocialAccount;
import me.u6k.task_focus.model.SocialAccountPK;
import me.u6k.task_focus.model.SocialAccountRepository;
import me.u6k.task_focus.model.User;
import me.u6k.task_focus.model.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.social.twitter.api.AccountSettings;
import org.springframework.social.twitter.api.BlockOperations;
import org.springframework.social.twitter.api.DirectMessageOperations;
import org.springframework.social.twitter.api.FriendOperations;
import org.springframework.social.twitter.api.GeoOperations;
import org.springframework.social.twitter.api.ListOperations;
import org.springframework.social.twitter.api.RateLimitStatus;
import org.springframework.social.twitter.api.ResourceFamily;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.StreamingOperations;
import org.springframework.social.twitter.api.SuggestionCategory;
import org.springframework.social.twitter.api.TimelineOperations;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.UserOperations;
import org.springframework.social.twitter.api.impl.AccountSettingsData;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SocialAccountRepository socialAccountRepo;

    private User user1;

    private SocialAccount socialAccount1;

    private User user2;

    private SocialAccount socialAccount2;

    private User user3;

    private SocialAccount socialAccount3;

    @Before
    public void setup() {
        // 事前条件クリーンアップ
        this.socialAccountRepo.deleteAllInBatch();
        this.userRepo.deleteAllInBatch();

        // 事前条件準備
        this.user1 = new User(UUID.randomUUID(), "foo", "ja", "Asia/Tokyo");
        this.userRepo.save(this.user1);

        this.socialAccount1 = new SocialAccount(new SocialAccountPK("twitter", "11111"), "foo", this.user1);
        this.socialAccountRepo.save(this.socialAccount1);

        this.user2 = new User(UUID.randomUUID(), "bar", "ja", "Asia/Tokyo");
        this.userRepo.save(this.user2);

        this.socialAccount2 = new SocialAccount(new SocialAccountPK("twitter", "22222"), "bar", this.user2);
        this.socialAccountRepo.save(this.socialAccount2);

        this.user3 = new User(UUID.randomUUID(), "boo", "ja", "Asia/Tokyo");
        this.userRepo.save(this.user3);

        this.socialAccount3 = new SocialAccount(new SocialAccountPK("twitter", "33333"), "bar", this.user3);
        this.socialAccountRepo.save(this.socialAccount3);
    }

    @Test
    public void add_twitter_正常に登録できる() {
        /*
         * テスト条件設定
         */
        Twitter twitter = new TwitterImpl(44444L, "hoge", "en", "UTC");

        /*
         * テスト実行
         */
        UUID userId = this.userService.add(twitter);

        /*
         * テスト結果検証
         */
        assertThat(this.userRepo.count(), is(4));

        User actualUser1 = this.userRepo.findOne(this.user1.getId());
        assertThat(actualUser1, is(samePropertyValuesAs(this.user1)));
        assertThat(actualUser1.getSocialAccounts().size(), is(1));
        assertThat(actualUser1.getSocialAccounts().get(0), is(samePropertyValuesAs(this.socialAccount1)));

        User actualUser2 = this.userRepo.findOne(this.user2.getId());
        assertThat(actualUser2, is(samePropertyValuesAs(this.user2)));
        assertThat(actualUser2.getSocialAccounts().size(), is(1));
        assertThat(actualUser2.getSocialAccounts().get(0), is(samePropertyValuesAs(this.socialAccount2)));

        User actualUser3 = this.userRepo.findOne(this.user3.getId());
        assertThat(actualUser3, is(samePropertyValuesAs(this.user3)));
        assertThat(actualUser3.getSocialAccounts().size(), is(1));
        assertThat(actualUser3.getSocialAccounts().get(0), is(samePropertyValuesAs(this.socialAccount3)));

        User actualUser4 = this.userRepo.findOne(userId);
        assertThat(actualUser4.getLocation(), is("en"));
        assertThat(actualUser4.getName(), is("hoge"));
        assertThat(actualUser4.getTimeZone(), is("UTC"));
        assertThat(actualUser4.getSocialAccounts().size(), is(1));
        assertThat(actualUser4.getSocialAccounts().get(1).getKey().getProviderId(), is("twitter"));
        assertThat(actualUser4.getSocialAccounts().get(1).getKey().getProviderUserId(), is("44444"));
        assertThat(actualUser4.getSocialAccounts().get(1).getName(), is("hoge"));
    }

    @Test
    public void add_twitter_サインアップ済みの場合は既存ユーザーを返す() {
        /*
         * テスト条件設定
         */
        Twitter twitter = new TwitterImpl(22222L, "aaaaa", "bbbbb", "ccccc");

        /*
         * テスト実行
         */
        UUID userId = this.userService.add(twitter);

        /*
         * テスト結果検証
         */
        assertThat(userId, is(this.user2.getId()));

        assertThat(this.userRepo.count(), is(3));

        User actualUser1 = this.userRepo.findOne(this.user1.getId());
        assertThat(actualUser1, is(samePropertyValuesAs(this.user1)));
        assertThat(actualUser1.getSocialAccounts().size(), is(1));
        assertThat(actualUser1.getSocialAccounts().get(0), is(samePropertyValuesAs(this.socialAccount1)));

        User actualUser2 = this.userRepo.findOne(this.user2.getId());
        assertThat(actualUser2, is(samePropertyValuesAs(this.user2)));
        assertThat(actualUser2.getSocialAccounts().size(), is(1));
        assertThat(actualUser2.getSocialAccounts().get(0), is(samePropertyValuesAs(this.socialAccount2)));

        User actualUser3 = this.userRepo.findOne(this.user3.getId());
        assertThat(actualUser3, is(samePropertyValuesAs(this.user3)));
        assertThat(actualUser3.getSocialAccounts().size(), is(1));
        assertThat(actualUser3.getSocialAccounts().get(0), is(samePropertyValuesAs(this.socialAccount3)));
    }

    @Test
    public void add_twitter_引数がnull() {
        try {
            this.userService.add(null);

            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("twitter is null"));
        }
    }

    @Test
    public void findBySocialAccount_twitter_取得できる() {
        /*
         * テスト条件設定
         */
        Twitter twitter = new TwitterImpl(22222L, null, null, null);

        /*
         * テスト実行
         */
        User actualUser = this.userService.findBySocialAccount(twitter);

        /*
         * テスト結果検証
         */
        assertThat(actualUser, is(samePropertyValuesAs(this.user2)));
    }

    @Test
    public void findBySocialAccount_twitter_ユーザーが存在しない場合はnullを返す() {
        /*
         * テスト条件設定
         */
        Twitter twitter = new TwitterImpl(12345L, null, null, null);

        /*
         * テスト実行
         */
        User actualUser = this.userService.findBySocialAccount(twitter);

        /*
         * テスト結果検証
         */
        assertNull(actualUser);
    }

    @Test
    public void findBySocialAccount_twitter_引数がnull() {
        try {
            this.userService.findBySocialAccount(null);

            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("twitter is null"));
        }
    }

    private class TwitterImpl implements Twitter {

        private UserOperations userOperations;

        TwitterImpl(long profileId, String name, String location, String timeZone) {
            this.userOperations = new UserOperationsImpl(profileId, name, location, timeZone);
        }

        @Override
        public UserOperations userOperations() {
            return this.userOperations;
        }

        @Override
        public boolean isAuthorized() {
            throw new UnsupportedOperationException();
        }

        @Override
        public BlockOperations blockOperations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public DirectMessageOperations directMessageOperations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public FriendOperations friendOperations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public GeoOperations geoOperations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListOperations listOperations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public SearchOperations searchOperations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public StreamingOperations streamingOperations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TimelineOperations timelineOperations() {
            throw new UnsupportedOperationException();
        }

        @Override
        public RestOperations restOperations() {
            throw new UnsupportedOperationException();
        }

        private class UserOperationsImpl implements UserOperations {

            private TwitterProfile userProfile;

            UserOperationsImpl(long profileId, String name, String location, String timeZone) {
                this.userProfile = new TwitterProfileEx(profileId, name, location, timeZone);
            }

            @Override
            public long getProfileId() {
                return this.userProfile.getId();
            }

            @Override
            public TwitterProfile getUserProfile() {
                return this.userProfile;
            }

            @Override
            public String getScreenName() {
                throw new UnsupportedOperationException();
            }

            @Override
            public TwitterProfile getUserProfile(String screenName) {
                throw new UnsupportedOperationException();
            }

            @Override
            public TwitterProfile getUserProfile(long userId) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<TwitterProfile> getUsers(long... userIds) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<TwitterProfile> getUsers(String... screenNames) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<TwitterProfile> searchForUsers(String query) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<TwitterProfile> searchForUsers(String query, int page, int pageSize) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<SuggestionCategory> getSuggestionCategories() {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<TwitterProfile> getSuggestions(String slug) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Map<ResourceFamily, List<RateLimitStatus>> getRateLimitStatus(ResourceFamily... resources) {
                throw new UnsupportedOperationException();
            }

            @Override
            public AccountSettings getAccountSettings() {
                throw new UnsupportedOperationException();
            }

            @Override
            public AccountSettings updateAccountSettings(AccountSettingsData accountSettingsData) {
                throw new UnsupportedOperationException();
            }

            @SuppressWarnings("serial")
            private class TwitterProfileEx extends TwitterProfile {

                private String timeZone;

                TwitterProfileEx(long profileId, String name, String location, String timeZone) {
                    super(profileId, null, name, null, null, null, location, null);

                    this.timeZone = timeZone;
                }

                @Override
                public String getTimeZone() {
                    return this.timeZone;
                }

            }

        }

    }

}
