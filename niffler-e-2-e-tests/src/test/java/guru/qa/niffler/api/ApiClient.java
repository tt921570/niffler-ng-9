package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alexander
 */
public abstract class ApiClient {

    public static final Config CFG = Config.getInstance();

    protected final Retrofit retrofit;
    protected final String baseUrl;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    protected <T> T executeApiCall(Supplier<Call<T>> spendApiSupplier, int status) {
        Response<T> response;
        try {
            response = spendApiSupplier.get().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(status, response.code());
        return response.body();
    }
}
