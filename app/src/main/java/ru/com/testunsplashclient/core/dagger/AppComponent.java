package ru.com.testunsplashclient.core.dagger;

import javax.inject.Singleton;

import dagger.Component;
import ru.com.testunsplashclient.app.NetworkConnectivityAware;
import ru.com.testunsplashclient.core.dagger.module.data.DataProviderModule;
import ru.com.testunsplashclient.core.dagger.module.data.NetworkUtilsModule;
import ru.com.testunsplashclient.core.dagger.module.data.RealmModule;
import ru.com.testunsplashclient.core.dagger.module.navigation.LocalNavigationModule;
import ru.com.testunsplashclient.core.dagger.module.navigation.NavigationModule;
import ru.com.testunsplashclient.core.data.DataProvider;
import ru.com.testunsplashclient.mvp.main.favourites.FavouritesFragmentPresenter;
import ru.com.testunsplashclient.mvp.main.info.InfoFragmentPresenter;
import ru.com.testunsplashclient.mvp.main.photo_details.PhotoDetailsFragmentPresenter;
import ru.com.testunsplashclient.mvp.main.photos.PhotosFragmentPresenter;
import ru.com.testunsplashclient.ui.screens.main.FavouritesFragment;
import ru.com.testunsplashclient.ui.screens.main.InfoFragment;
import ru.com.testunsplashclient.ui.screens.main.MainActivity;
import ru.com.testunsplashclient.ui.screens.main.PhotoDetailsFragment;
import ru.com.testunsplashclient.ui.screens.main.PhotosFragment;
import ru.com.testunsplashclient.ui.screens.splash.SplashActivity;
import ru.com.testunsplashclient.ui.screens.main.TabContainerFragment;

@Singleton
@Component(modules = {
        NavigationModule.class,
        LocalNavigationModule.class,
        DataProviderModule.class,
        RealmModule.class,
        NetworkUtilsModule.class

})

public interface AppComponent {

//Navigation injections

    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(TabContainerFragment fragment);

    void inject(PhotosFragment fragment);

    void inject(FavouritesFragment fragment);

    void inject(PhotoDetailsFragment fragment);

    void inject(InfoFragment fragment);

//Presenter injections

    void inject(PhotosFragmentPresenter presenter);

    void inject(FavouritesFragmentPresenter presenter);

    void inject(PhotoDetailsFragmentPresenter presenter);

    void inject(InfoFragmentPresenter presenter);

//Other injections

    void inject(DataProvider dataProvider);

    void inject(NetworkConnectivityAware connectivityAware);

}
