package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final SelenideElement spendingTable = $("#spendings");
  private final SelenideElement statChart = $("#stat");
  private final SelenideElement menu = $("[aria-label=Menu]");
  private final SelenideElement profileMenuItem = $("[href=\"/profile\"]");
  private final SelenideElement friendsMenuItem = $(".nav-link[href=\"/people/friends\"]");

  @Step("Statistics and History of Spendings is displayed")
  public MainPage checkThatPageLoaded() {
    statChart.shouldBe(visible);
    spendingTable.should(visible);
    return this;
  }

  @Step("Edit spending: {0}")
  public EditSpendingPage editSpending(String description) {
    spendingTable.$$("tbody tr").find(text(description))
        .$$("td")
        .get(5)
        .click();
    return new EditSpendingPage();
  }

  @Step("Spending with description [{0}] is displayed")
  public void checkThatTableContainsSpending(String description) {
    spendingTable.$$("tbody tr").find(text(description))
        .should(visible);
  }

  @Step("Open Profile")
  public ProfilePage openProfilePage() {
    menu.click();
    profileMenuItem.click();
    return new ProfilePage();
  }

  @Step("Open Friends")
  public FriendsPage openFriendsPage() {
    menu.click();
    friendsMenuItem.click();
    return new FriendsPage();
  }
}
