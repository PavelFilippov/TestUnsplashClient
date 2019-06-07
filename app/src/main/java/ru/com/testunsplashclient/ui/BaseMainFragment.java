package ru.com.testunsplashclient.ui;

import android.os.SystemClock;

import com.arellomobile.mvp.MvpAppCompatFragment;

import ru.com.testunsplashclient.ui.screens.main.MainActivity;

public class BaseMainFragment extends MvpAppCompatFragment {

    public MainActivity getMainActivity() {
        if (getActivity() != null) return ( (MainActivity) getActivity());
        return null;
    }

    public boolean isDoubleClick(long mLastClickTime) {
        return SystemClock.elapsedRealtime() - mLastClickTime < 1000;
    }

}
