/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.mvvm.shirts.ui.shirts;

import com.android.mvvm.shirts.R;
import com.android.mvvm.shirts.databinding.ShirtItemBinding;
import com.android.mvvm.shirts.ui.common.DataBoundListAdapter;
import com.android.mvvm.shirts.util.Objects;
import com.android.mvvm.shirts.vo.ShirtsEntity;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ShirtAdapter
        extends DataBoundListAdapter<ShirtsEntity, ShirtItemBinding> {

    private final DataBindingComponent dataBindingComponent;
    private final ShirtClickCallback callback;

    public ShirtAdapter(DataBindingComponent dataBindingComponent,
                        ShirtClickCallback callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
    }

    @Override
    protected ShirtItemBinding createBinding(ViewGroup parent) {
        ShirtItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.shirt_item, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            ShirtsEntity shirt = binding.getShirtsEntity();
            if (shirt != null && callback != null) {
                callback.onClick(shirt);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ShirtItemBinding binding, ShirtsEntity item) {
        binding.setShirtsEntity(item);
    }

    @Override
    protected boolean areItemsTheSame(ShirtsEntity oldItem, ShirtsEntity newItem) {
        return Objects.equals(oldItem.getTitle(), newItem.getTitle());
    }

    @Override
    protected boolean areContentsTheSame(ShirtsEntity oldItem, ShirtsEntity newItem) {
        return Objects.equals(oldItem.getOriginalTitle(), newItem.getOriginalTitle())
                && oldItem.getTitle() == newItem.getTitle();
    }

    public interface ShirtClickCallback {
        void onClick(ShirtsEntity contributor);
    }
}
