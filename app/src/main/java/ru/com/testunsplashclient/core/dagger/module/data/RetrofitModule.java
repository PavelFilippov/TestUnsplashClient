package ru.com.testunsplashclient.core.dagger.module.data;

import android.text.TextUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.com.testunsplashclient.BuildConfig;
import ru.com.testunsplashclient.app.NoNetworkException;
import ru.com.testunsplashclient.app.PlatformHttpException;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.configuration.GsonConfiguredFactory;
import ru.com.testunsplashclient.core.data.model.BaseRESTModel;
import ru.com.testunsplashclient.core.utils.Constants;

@Module
public class RetrofitModule {

    private static final String AUTHORIZATION = "Authorization";
    private Cache cache;

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Retrofit.Builder builder) {
        return builder.baseUrl(Constants.API_URL).build();
    }

    @Provides
    @Singleton
    public Retrofit.Builder provideRetrofitBuilder(Converter.Factory converterFactory) {
        return new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonConfiguredFactory.getGson()))
                .validateEagerly(true)
                .client(provideHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    @Provides
    @Singleton
    public Converter.Factory provideConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .setFieldNamingStrategy(new CustomFieldNamingPolicy())
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .serializeNulls()
                .create();
    }

    private static class CustomFieldNamingPolicy implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            String name = FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES.translateName(field);
            name = name.substring(2, name.length()).toLowerCase();
            return name;
        }
    }

    private OkHttpClient provideHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.BASIC);


        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(buildCache())
                .addInterceptor(chain -> {
                    Request authorization = chain.request().newBuilder()
                            .build();
                    Response response;
                    try {
                        response = chain.proceed(authorization);
                    } catch (IOException e) {
                        try {
                            response = chain.proceed(authorization);
                        } catch (IOException e1) {
                            throw new NoNetworkException();
                        }
                    }
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxAge(2, TimeUnit.MINUTES)
                            .build();


                    Response build = response.newBuilder()
                            .header("Cache-Control", cacheControl.toString())
                            .build();

                    if (!build.isSuccessful()) {
                        ResponseBody body = build.body();
                        if (body != null) {
                            String string = body.string();
                            if (!TextUtils.isEmpty(string)) {
                                BaseRESTModel restModel = new Gson().fromJson(string, BaseRESTModel.class);
                                throw new PlatformHttpException(restModel);
                            }
                        }
                        return build;
                    } else {
                        return build;
                    }
                })
                .followRedirects(false)
                .build();
    }

    private Cache buildCache() {
        if (cache == null) {
            cache = new Cache(new File(TheApplication.INSTANCE.getCacheDir(), "http-cache"), 10 * 1024 * 1024);
        }
        return cache;
    }

}
