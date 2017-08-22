package com.android.mvvm.shirts.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.android.mvvm.shirts.ui.shirts.ShirtViewModel;

import com.android.mvvm.shirts.viewmodel.ShirtsMVVMViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ShirtViewModel.class)
    abstract ViewModel bindShirtViewModel(ShirtViewModel shirtViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ShirtsMVVMViewModelFactory factory);
}
