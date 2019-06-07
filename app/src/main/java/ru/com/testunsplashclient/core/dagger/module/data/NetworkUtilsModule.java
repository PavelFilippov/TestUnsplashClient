package ru.com.testunsplashclient.core.dagger.module.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.com.testunsplashclient.core.utils.NetworkUtils;

@Module
public class NetworkUtilsModule {
    @Provides
    @Singleton
    public NetworkUtils getNetworkUtils() {
        return new NetworkUtils();
    }
}
