package ru.com.testunsplashclient.core.dagger.module.navigation;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.com.testunsplashclient.core.LocalCiceroneHolder;

@Module
public class LocalNavigationModule {

    @Provides
    @Singleton
    LocalCiceroneHolder provideLocalNavigationHolder() {
        return new LocalCiceroneHolder();
    }
}