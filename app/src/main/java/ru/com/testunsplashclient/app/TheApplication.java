package ru.com.testunsplashclient.app;

import androidx.multidex.MultiDexApplication;

import org.androidannotations.annotations.EApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import lombok.Getter;
import lombok.Setter;
import ru.com.testunsplashclient.core.dagger.AppComponent;
import ru.com.testunsplashclient.core.dagger.DaggerAppComponent;

@EApplication
public class TheApplication extends MultiDexApplication {

    public static TheApplication INSTANCE;

    private AppComponent appComponent;

    @Getter
    @Setter
    private boolean hasNetworkConnection = true;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().build();
        }
        return appComponent;
    }

}
