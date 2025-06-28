package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * @author Alexander
 */
public class ProfilePage {
    private final SelenideElement goToMainPageLink = $("[href=\"/main\"]");
    private final SelenideElement newSpendingButton = $("[href=\"/spending\"]");
    private final SelenideElement menuButton = $("[aria-label=\"Menu\"] img ");
    private final SelenideElement uploadNewPictureInput = $("#image__input");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveChangesButton = $("#\\:r7\\:");
    private final SelenideElement showArchivedSwitcher = $(".PrivateSwitchBase-input");
    private final SelenideElement categoryButton = $("#category");

    @Step("Switch show archived")
    public ProfilePage switchShowArchived() {
        showArchivedSwitcher.shouldNotBe(checked).click();
        return this;
    }

    @Step("Category [{0}] is present in list")
    public void categoryIsPresentInList(String categoryName) {
        $$(".MuiGrid-item").find(text(categoryName)).shouldBe(visible);
    }
}
