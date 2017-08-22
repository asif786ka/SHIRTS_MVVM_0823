package com.android.mvvm.shirts.ui.common;

import com.android.mvvm.shirts.MainActivity;
import com.android.mvvm.shirts.R;
import com.android.mvvm.shirts.ui.shirts.ShirtFragment;


import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }


    public void navigateToShirts(String color, String name) {
        ShirtFragment fragment = ShirtFragment.create(color, name);
        String tag = "shirt" + "/" + color + "/" + name;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }

}
