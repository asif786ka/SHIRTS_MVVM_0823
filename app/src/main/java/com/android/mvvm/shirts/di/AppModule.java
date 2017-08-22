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

package com.android.mvvm.shirts.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.android.mvvm.shirts.api.ShirtService;
import com.android.mvvm.shirts.db.ShirtsDao;
import com.android.mvvm.shirts.db.ShirtsDb;
import com.android.mvvm.shirts.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton @Provides
    ShirtService provideShirtService() {
        return new Retrofit.Builder()
                .baseUrl("http://mock-shirt-backend.getsandbox.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(ShirtService.class);
    }

    @Singleton @Provides
    ShirtsDb provideDb(Application app) {
        return Room.databaseBuilder(app, ShirtsDb.class,"shirts.db").build();
    }


    @Singleton @Provides
    ShirtsDao provideShirtsDao(ShirtsDb db) {
        return db.shirtsDao();
    }
}
