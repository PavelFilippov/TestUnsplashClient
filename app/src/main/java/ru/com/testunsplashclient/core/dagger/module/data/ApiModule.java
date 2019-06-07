package ru.com.testunsplashclient.core.dagger.module.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import ru.com.testunsplashclient.app.Api;

@Module(includes = {RetrofitModule.class})
public class ApiModule {
    @Provides
    @Singleton
    public Api provideAuthApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }
}