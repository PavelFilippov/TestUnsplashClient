package ru.com.testunsplashclient.core.dagger.module.data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.com.testunsplashclient.core.data.PhotosDb;

@Module
public class RealmModule {
    @Provides
    @Singleton
    public PhotosDb provideDbService() {
            return new PhotosDb();
    }
}
