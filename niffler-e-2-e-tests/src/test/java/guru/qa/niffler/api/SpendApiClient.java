package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.eclipse.jetty.http.HttpStatus;

import java.util.List;

public class SpendApiClient extends ApiClient {

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendApiClient() {
    super(CFG.spendUrl());
  }

  public SpendJson addSpend(SpendJson spendJson) {
    return executeApiCall(() -> spendApi.addSpend(spendJson), HttpStatus.CREATED_201);
  }

  public SpendJson editSpend(SpendJson spendJson) {
    return executeApiCall(() -> spendApi.editSpend(spendJson), HttpStatus.OK_200);
  }

  public SpendJson getSpend(String id, String username) {
    return executeApiCall(() -> spendApi.getSpend(id, username), HttpStatus.OK_200);
  }

  public List<SpendJson> getAllSpends(String username, CurrencyValues filterCurrency, String from, String to) {
    return executeApiCall(() -> spendApi.getAllSpends(username, filterCurrency, from, to), HttpStatus.OK_200);
  }

  public void deleteSpend(String username, List<String> ids) {
    executeApiCall(() -> spendApi.deleteSpend(username, ids), HttpStatus.ACCEPTED_202);
  }

  public List<CategoryJson> getCategories(String username, Boolean excludeArchived) {
    return executeApiCall(() -> spendApi.getCategories(username, excludeArchived), HttpStatus.OK_200);
  }

  public CategoryJson updateCategory(CategoryJson categoryJson) {
    return executeApiCall(() -> spendApi.updateCategory(categoryJson), HttpStatus.OK_200);
  }

  public CategoryJson addCategory(CategoryJson categoryJson) {
    return executeApiCall(() -> spendApi.addCategory(categoryJson), HttpStatus.OK_200);
  }
}
