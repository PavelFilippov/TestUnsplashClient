package ru.com.testunsplashclient.ui;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import ru.com.testunsplashclient.ui.screens.main.FavouritesFragment_;
import ru.com.testunsplashclient.ui.screens.main.InfoFragment_;
import ru.com.testunsplashclient.ui.screens.main.MainActivity_;
import ru.com.testunsplashclient.ui.screens.main.PhotoDetailsFragment;
import ru.com.testunsplashclient.ui.screens.main.PhotoDetailsFragment_;
import ru.com.testunsplashclient.ui.screens.main.PhotosFragment_;
import ru.com.testunsplashclient.ui.screens.main.TabContainerFragment;
import ru.com.testunsplashclient.ui.screens.splash.SplashActivity_;
import ru.terrakok.cicerone.android.support.SupportAppScreen;

public class Screens {

//-----------------------------Splash screen---------------------------------

    public static final class SplashScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, SplashActivity_.class);
        }
    }

//-----------------------------Main Container---------------------------------

    public static final class MainScreen extends SupportAppScreen {
        @Override
        public Intent getActivityIntent(Context context) {
            return new Intent(context, MainActivity_.class);
        }
    }

    public static final class TabScreen extends SupportAppScreen {
        private final int id;

        public TabScreen(int id) {
            this.id = id;
        }

        @Override
        public Fragment getFragment() {
            return TabContainerFragment.getNewInstance(id);
        }
    }

//-----------------------------Photos screen---------------------------------

    public static final class PhotosScreen extends SupportAppScreen {
        private Context context;

        @Override
        public Intent getActivityIntent(Context context) {
            this.context = context;
            return super.getActivityIntent(context);
        }

        @Override
        public Fragment getFragment() {
            return Fragment.instantiate(context, PhotosFragment_.class.getName());
        }
    }

//-----------------------------Favourites screen---------------------------------

    public static final class FavouritesScreen extends SupportAppScreen {
        private Context context;

        @Override
        public Intent getActivityIntent(Context context) {
            this.context = context;
            return super.getActivityIntent(context);
        }

        @Override
        public Fragment getFragment() {
            return Fragment.instantiate(context, FavouritesFragment_.class.getName());
        }
    }

//-----------------------------Photo details screen---------------------------------

    public static final class PhotoDetailsScreen extends SupportAppScreen {
        private Context context;
        private final String id;

        public PhotoDetailsScreen(String id) {
            this.id = id;
        }

        @Override
        public Intent getActivityIntent(Context context) {
            this.context = context;
            return super.getActivityIntent(context);
        }

        @Override
        public Fragment getFragment() {
            return Fragment.instantiate(context, PhotoDetailsFragment_.class.getName(), PhotoDetailsFragment.getBundle(id));
        }
    }

//-----------------------------Info screen---------------------------------

    public static final class InfoScreen extends SupportAppScreen {
        private Context context;

        @Override
        public Intent getActivityIntent(Context context) {
            this.context = context;
            return super.getActivityIntent(context);
        }

        @Override
        public Fragment getFragment() {
            return Fragment.instantiate(context, InfoFragment_.class.getName());
        }
    }

}
