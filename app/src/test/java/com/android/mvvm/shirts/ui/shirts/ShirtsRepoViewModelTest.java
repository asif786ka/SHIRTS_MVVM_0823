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

import com.android.mvvm.shirts.repository.ShirtsRepository;
import com.android.mvvm.shirts.vo.ShirtsRepo;
import com.android.mvvm.shirts.vo.Resource;
import com.android.mvvm.shirts.vo.ShirtsEntity;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(JUnit4.class)
public class ShirtsRepoViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ShirtsRepository repository;
    private ShirtViewModel shirtViewModel;

    @Before
    public void setup() {
        repository = mock(ShirtsRepository.class);
        shirtViewModel = new ShirtViewModel(repository);
    }


    @Test
    public void testNull() {
        assertThat(shirtViewModel.getShirts(), notNullValue());
    }

    @Test
    public void dontFetchWithoutObservers() {
        shirtViewModel.setId("a", "b");
        verify(repository, never()).loadShirts(anyString(), anyString());
    }


    @Test
    public void shirts() {
        Observer<Resource<List<ShirtsEntity>>> observer = mock(Observer.class);
        shirtViewModel.getShirts().observeForever(observer);
        verifyNoMoreInteractions(observer);
        verifyNoMoreInteractions(repository);
        shirtViewModel.setId("foo", "bar");
        verify(repository).loadShirts("foo", "bar");
    }

    @Test
    public void resetId() {
        Observer<ShirtViewModel.ShirtId> observer = mock(Observer.class);
        shirtViewModel.shirtId.observeForever(observer);
        verifyNoMoreInteractions(observer);
        shirtViewModel.setId("foo", "bar");
        verify(observer).onChanged(new ShirtViewModel.ShirtId("foo", "bar"));
        reset(observer);
        shirtViewModel.setId("foo", "bar");
        verifyNoMoreInteractions(observer);
        shirtViewModel.setId("a", "b");
        verify(observer).onChanged(new ShirtViewModel.ShirtId("a", "b"));
    }

}