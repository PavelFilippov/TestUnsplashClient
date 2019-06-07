package ru.com.testunsplashclient.core.dagger.module.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.com.testunsplashclient.app.Api;
import ru.com.testunsplashclient.core.data.DataProvider;

@Module(includes = {ApiModule.class})
public class DataProviderModule {
    @Provides
    @Singleton
    public DataProvider provideNetService(Api api) {
        return new DataProvider(api);
    }
}
