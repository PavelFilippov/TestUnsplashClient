package ru.com.testunsplashclient.ui.screens.main;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.mvp.main.main_activity.MainActivityPresenter;
import ru.com.testunsplashclient.mvp.main.main_activity.MainActivityView;
import ru.com.testunsplashclient.ui.BaseActivity;
import ru.com.testunsplashclient.ui.Screens;
import ru.com.testunsplashclient.ui.common.BackButtonListener;
import ru.com.testunsplashclient.ui.common.RouterProvider;
import ru.terrakok.cicerone.Router;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements MainActivityView, RouterProvider {

    private static final String TAG = "MainActivity";

//Set screen Views

    @ViewById
    View mainContainer;

    @ViewById
    BottomNavigationView bottomNavigationView;

//Set app navigation injections and it's management

    @Inject
    Router router;

    @InjectPresenter
    MainActivityPresenter presenter;

    @ProvidePresenter
    public MainActivityPresenter createBottomNavigationPresenter() {
        return new MainActivityPresenter(router);
    }

//Local variables

    int bottomMenuPositionId;

    @Override
    public Router getRouter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TheApplication.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            bottomMenuPositionId = R.id.navigationPhoto;
        }

    }

    @AfterViews
    void afterViews() {
        initViews();
    }

//Internal methods

    private void initViews() {

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigationPhoto:
                    selectTab(R.id.navigationPhoto);
                    return true;
                case R.id.navigationFavourites:
                    selectTab(R.id.navigationFavourites);
                    return true;
                case R.id.navigationInfo:
                    selectTab(R.id.navigationInfo);
                    return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(bottomMenuPositionId);

    }

    public void selectTab(int id) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = null;
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f.isVisible()) {
                    currentFragment = f;
                    break;
                }
            }
        }
        Fragment newFragment = fm.findFragmentByTag(String.valueOf(id));

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return;

        FragmentTransaction transaction = fm.beginTransaction();
        if (newFragment == null) {
            transaction.add(R.id.mainContainer, new Screens.TabScreen(id).getFragment(), String.valueOf(id));
        }

        if (currentFragment != null) {
            transaction.hide(currentFragment);
        }

        if (newFragment != null) {
            transaction.show(newFragment);
        }
        transaction.commitNow();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = null;
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f.isVisible()) {
                    fragment = f;
                    break;
                }
            }
        }
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            presenter.onBackPressed();
        }
    }

}
