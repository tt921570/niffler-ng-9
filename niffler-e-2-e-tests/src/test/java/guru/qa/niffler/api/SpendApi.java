package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spending);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spending);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(@Path("id") String id,
                           @Query("username") String username);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getAllSpends(@Query("username") String username,
                                     @Query("CurrencyValues filterCurrency") CurrencyValues filterCurrency,
                                     @Query("from") String from,
                                     @Query("to") String to);

  @DELETE("internal/spends/remove")
  Call<Void> deleteSpend(@Query("username") String username,
                         @Query("ids") List<String> ids);

  @GET("internal/categories/all")
  Call<List<CategoryJson>> getCategories(@Query("username") String username,
                                         @Query("excludeArchived") Boolean excludeArchived);

  @PATCH("internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);
}
