package guru.qa.niffler.page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

/**
 * @author Alexander
 */
public class FriendsPage {
    private final SelenideElement friendsTab = $("[href=\"/people/friends\"]");
    private final SelenideElement allPeopleTab = $("[href=\"/people/all\"]");
    private final SelenideElement friendsTabPanel = $("#simple-tabpanel-friends");
    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement requestsTable = $("#requests");
    private final SelenideElement allPeopleTable = $("tbody#all");

    @Step("Switch to Friends tab")
    public FriendsPage switchToFriendsTab() {
        friendsTab.click();
        return this;
    }

    @Step("Switch to All people tab")
    public FriendsPage switchToAllPeopleTab() {
        allPeopleTab.click();
        return this;
    }

    @Step("Item [{0}] is present in Friends table")
    public void friendsTableContains(String userItemText) {
        friendsTable.$$("tr").shouldHave(CollectionCondition.itemWithText(userItemText));
    }

    @Step("Item [{0}] is present in Requests table")
    public void requestsTableContains(String userItemText) {
        requestsTable.$$("tr").shouldHave(CollectionCondition.itemWithText(userItemText));
    }

    @Step("Item [{0}] is present in All people table")
    public void allPeopleTableContains(String userItemText) {
        allPeopleTable.$$("tr").shouldHave(CollectionCondition.itemWithText(userItemText));
    }

    @Step("Friends tab panel is empty")
    public void fiendsTabPanelIsEmpty() {
        friendsTabPanel.shouldHave(text("There are no users yet"));
    }

}
