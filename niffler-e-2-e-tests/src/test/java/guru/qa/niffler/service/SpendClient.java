package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.UUID;

/**
 * @author Alexander
 */
public interface SpendClient {
    SpendJson createSpend(SpendJson spend);

    CategoryJson createCategory(CategoryJson category);

    SpendJson updateSpend(SpendJson spendJson);

    CategoryJson findCategoryById(UUID id);

    CategoryJson findCategoryByUsernameAndCategoryName(String username, String name);

    SpendJson findSpendById(UUID id);

    SpendJson findSpendByUsernameAndDescription(String username, String description);

    void deleteSpend(SpendJson spend);

    void deleteCategory(CategoryJson category);
}
