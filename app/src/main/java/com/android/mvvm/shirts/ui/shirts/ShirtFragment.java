package com.android.mvvm.shirts.ui.shirts;

import com.android.mvvm.shirts.R;
import com.android.mvvm.shirts.binding.FragmentDataBindingComponent;
import com.android.mvvm.shirts.databinding.ShirtsFragmentBinding;
import com.android.mvvm.shirts.di.Injectable;
import com.android.mvvm.shirts.ui.common.NavigationController;
import com.android.mvvm.shirts.util.AutoClearedValue;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;

import javax.inject.Inject;

/**
 * The UI Controller for displaying a ShirtsRepo's information with its shirts.
 */
public class ShirtFragment extends Fragment implements LifecycleRegistryOwner, Injectable {

    private static final String SHIRT_COLOR_KEY = "shirt_color";

    private static final String SHIRT_NAME_KEY = "shirt_name";

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ShirtViewModel shirtViewModel;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<ShirtsFragmentBinding> binding;
    AutoClearedValue<ShirtAdapter> adapter;

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shirtViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShirtViewModel.class);
        Bundle args = getArguments();
        if (args != null && args.containsKey(SHIRT_COLOR_KEY) &&
                args.containsKey(SHIRT_NAME_KEY)) {
            shirtViewModel.setId(args.getString(SHIRT_COLOR_KEY),
                    args.getString(SHIRT_NAME_KEY));
        } else {
            shirtViewModel.setId(null, null);
        }

        ShirtAdapter adapter = new ShirtAdapter(dataBindingComponent,
                contributor -> navigationController.navigateToShirts("",""));
        this.adapter = new AutoClearedValue<>(this, adapter);
        binding.get().contributorList.setAdapter(adapter);
        initShirtsList(shirtViewModel);
    }

    private void initShirtsList(ShirtViewModel viewModel) {
        viewModel.getShirts().observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                adapter.get().replace(listResource.data);
            } else {
                //noinspection ConstantConditions
                adapter.get().replace(Collections.emptyList());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        ShirtsFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.shirts_fragment, container, false);
        dataBinding.setRetryCallback(() -> shirtViewModel.retry());
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    public static ShirtFragment create(String owner, String name) {
        ShirtFragment shirtFragment = new ShirtFragment();
        Bundle args = new Bundle();
        args.putString(SHIRT_COLOR_KEY, owner);
        args.putString(SHIRT_NAME_KEY, name);
        shirtFragment.setArguments(args);
        return shirtFragment;
    }
}
