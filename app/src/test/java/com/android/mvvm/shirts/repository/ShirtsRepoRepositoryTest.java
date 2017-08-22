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

package com.android.mvvm.shirts.repository;

import com.android.mvvm.shirts.api.ApiResponse;
import com.android.mvvm.shirts.api.ShirtService;
import com.android.mvvm.shirts.api.ShirtsResponse;
import com.android.mvvm.shirts.db.ShirtsDb;
import com.android.mvvm.shirts.db.ShirtsDao;
import com.android.mvvm.shirts.util.AbsentLiveData;
import com.android.mvvm.shirts.util.InstantAppExecutors;
import com.android.mvvm.shirts.util.TestUtil;
import com.android.mvvm.shirts.vo.ShirtsRepo;
import com.android.mvvm.shirts.vo.RepoSearchResult;
import com.android.mvvm.shirts.vo.Resource;
import com.android.mvvm.shirts.vo.Shirt;
import com.android.mvvm.shirts.vo.ShirtsEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.android.mvvm.shirts.util.ApiUtil.successCall;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(JUnit4.class)
public class ShirtsRepoRepositoryTest {
    private ShirtsRepository repository;
    private ShirtsDao dao;
    private ShirtService service;
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Before
    public void init() {
        dao = mock(ShirtsDao.class);
        service = mock(ShirtService.class);
        ShirtsDb db = mock(ShirtsDb.class);
        when(db.shirtsDao()).thenReturn(dao);
        repository = new ShirtsRepository(new InstantAppExecutors(), db, dao, service);
    }

    @Test
    public void loadShirtsFromNetwork() throws IOException {
       MutableLiveData<List<ShirtsEntity>> dbData = new MutableLiveData<>();
        when(dao.loadShirts()).thenReturn(dbData);

        Shirt shirt1 = TestUtil.createShirts(1,"TEST2a","TEST2a",24);
        Shirt shirt2 = TestUtil.createShirts(2,"TEST2b","TEST2b",25);
        List<Shirt> shirts = new ArrayList<Shirt>();
        shirts.add(shirt1);
        shirts.add(shirt2);

        List<ShirtsEntity> shirtsEntities = new ArrayList<ShirtsEntity>();
        ShirtsEntity shirtEntity;
        for(int i = 0;i <shirts.size(); i++ )
        {
            shirtEntity = new ShirtsEntity(shirts.get(i).getId(),shirts.get(i).getPicture(),shirts.get(i).getName(),shirts.get(i).getPicture());
            shirtsEntities.add(shirtEntity);
        }

        LiveData<ApiResponse<List<Shirt>>> call = successCall(shirts);
        when(service.loadShirts()).thenReturn(call);

        LiveData<Resource<List<ShirtsEntity>>> data = repository.loadShirts("test1","test2");
        verify(dao).loadShirts();
        verifyNoMoreInteractions(service);

        Observer observer = mock(Observer.class);
        data.observeForever(observer);
        verifyNoMoreInteractions(service);
        verify(observer).onChanged(Resource.loading(null));
        MutableLiveData<List<ShirtsEntity>> updatedDbData = new MutableLiveData<>();
        when(dao.loadShirts()).thenReturn(updatedDbData);

        dbData.postValue(null);
        verify(service).loadShirts();
        //verify(dao).insertShirts(shirts);

        updatedDbData.postValue(shirtsEntities);
        verify(observer).onChanged(Resource.success(shirtsEntities));
    }

    @Test
    public void loadShirts() throws IOException {
        MutableLiveData<List<ShirtsEntity>> dbData = new MutableLiveData<>();
        when(dao.loadShirts()).thenReturn(dbData);

        LiveData<Resource<List<ShirtsEntity>>> data = repository.loadShirts("foo",
                "bar");
        verify(dao).loadShirts();

        Observer<Resource<List<ShirtsEntity>>> observer = mock(Observer.class);
        data.observeForever(observer);

        verify(observer).onChanged(Resource.loading( null));

        MutableLiveData<List<ShirtsEntity>> updatedDbData = new MutableLiveData<>();
        when(dao.loadShirts()).thenReturn(updatedDbData);
        dbData.setValue(Collections.emptyList());

        verify(service).loadShirts().getValue().body.get(0).setName("Test");
        ArgumentCaptor<List<ShirtsEntity>> inserted = ArgumentCaptor.forClass((Class) List.class);
        verify(dao).insertShirts(inserted.capture());


        assertThat(inserted.getValue().size(), is(1));
        ShirtsEntity first = inserted.getValue().get(0);
        assertThat(first.getTitle(), is("Test"));

    }

}