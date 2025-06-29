package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.api.ApiClient.CFG;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.*;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.*;

/**
 * @author Alexander
 */
@WebTest
public class FiendsWebTest {

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .openFriendsPage()
                .friendsTableContains("swan\n\nGenry\n\nUnfriend");
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .openFriendsPage()
                .fiendsTabPanelIsEmpty();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .openFriendsPage()
                .requestsTableContains("duck\n\nduck\n\nAccept\nDecline");
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit()
                .openFriendsPage()
                .switchToAllPeopleTab()
                .allPeopleTableContains("penguin\n\nSkipper\n\nWaiting...");
    }

}
