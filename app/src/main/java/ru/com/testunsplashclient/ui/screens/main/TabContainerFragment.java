package ru.com.testunsplashclient.ui.screens.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import javax.annotation.Nullable;
import javax.inject.Inject;

import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.LocalCiceroneHolder;
import ru.com.testunsplashclient.ui.Screens;
import ru.com.testunsplashclient.ui.common.BackButtonListener;
import ru.com.testunsplashclient.ui.common.RouterProvider;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import ru.terrakok.cicerone.commands.Command;

public class TabContainerFragment extends Fragment implements RouterProvider, BackButtonListener {

    private static final String TAG = "TabContainerFragment";
    private static final String TAB_ID = "tab_id";

    private Navigator navigator;

    @Inject
    LocalCiceroneHolder ciceroneHolder;

    public static TabContainerFragment getNewInstance(int tabId) {
        TabContainerFragment fragment = new TabContainerFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(TAB_ID, tabId);
        fragment.setArguments(arguments);

        return fragment;
    }

    private int getTabId() {
        return getArguments().getInt(TAB_ID);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        TheApplication.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_container, container, false);
    }

    private Cicerone<Router> getCicerone() {
        return ciceroneHolder.getCicerone(String.valueOf(getTabId()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getChildFragmentManager().findFragmentById(R.id.ftcContainer) == null) {
            if (getTabId() == R.id.navigationPhoto) {
                getCicerone().getRouter().replaceScreen(new Screens.PhotosScreen());
            } else if (getTabId() == R.id.navigationFavourites) {
                getCicerone().getRouter().replaceScreen(new Screens.FavouritesScreen());
            } else if (getTabId() == R.id.navigationInfo) {
                getCicerone().getRouter().replaceScreen(new Screens.InfoScreen());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCicerone().getNavigatorHolder().setNavigator(getNavigator());
    }

    @Override
    public void onPause() {
        getCicerone().getNavigatorHolder().removeNavigator();
        super.onPause();
    }

    private Navigator getNavigator() {
        if (navigator == null) {
            navigator = new SupportAppNavigator(getActivity(), getChildFragmentManager(), R.id.ftcContainer) {
                @Override
                protected void setupFragmentTransaction(Command command, Fragment currentFragment, Fragment nextFragment, FragmentTransaction fragmentTransaction) {
                    super.setupFragmentTransaction(command, currentFragment, nextFragment, fragmentTransaction);
                    fragmentTransaction.setCustomAnimations(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right,
                            R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
            };
        }
        return navigator;
    }

    @Override
    public Router getRouter() {
        return getCicerone().getRouter();
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.ftcContainer);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return true;
        } else {
            ((RouterProvider) getActivity()).getRouter().exit();
            return true;
        }
    }
}
