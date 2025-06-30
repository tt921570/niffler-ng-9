package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.api.ApiClient.CFG;

/**
 * @author Alexander
 */
@WebTest
public class ProfileTest {

    @User(
            username = "goose",
            categories = @Category(archived = true)
    )
    @Test
    @DisplayName("Archived category is displayed in Category list")
    void archivedCategoryShouldPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(category.username(), "67890")
                .submit()
                .openProfilePage()
                .switchShowArchived()
                .categoryIsPresentInList(category.name());
    }

    @User(username = "penguin")
    @Test
    @DisplayName("Active category is displayed in Category list")
    void activeCategoryShouldPresentInCategoryList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(category.username(), "54321")
                .submit()
                .openProfilePage()
                .categoryIsPresentInList(category.name());
    }

}
