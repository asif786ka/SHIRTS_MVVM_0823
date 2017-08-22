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

package com.android.mvvm.shirts.db;

import com.android.mvvm.shirts.util.TestUtil;
import com.android.mvvm.shirts.vo.ShirtsEntity;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;

import java.util.List;

import static com.android.mvvm.shirts.util.LiveDataTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class ShirtsRepoDaoTest extends DbTest {


    @Test
    public void testInsertShirts() throws InterruptedException {
        ShirtsEntity shirts = TestUtil.createShirtsEntity(1,"https://unsplash.it/128/128","Southview Clarke","brown");
        ShirtsEntity shirts2 = TestUtil.createShirtsEntity(2,"https://unsplash.it/128/128","Barronett Higgins","green");

        db.beginTransaction();
        try {
            db.shirtsDao().insert(shirts);
            db.shirtsDao().insert(shirts2);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        List<ShirtsEntity> list = getValue(db.shirtsDao().loadShirts());
        assertThat(list.size(), is(2));
        ShirtsEntity first = list.get(0);

        assertThat(first.getId(), is(1));
        assertThat(first.getPosterPath(), is("https://unsplash.it/128/128"));

        ShirtsEntity  second = list.get(1);
        assertThat(second.getId(), is(2));
        assertThat(first.getPosterPath(), is("https://unsplash.it/128/128"));
    }

    @Test
    public void testCreateIfNotExists_exists() throws InterruptedException {
        ShirtsEntity shirts = TestUtil.createShirtsEntity(1,"https://unsplash.it/128/128","Southview Clarke","brown");;
        db.shirtsDao().insert(shirts);
        assertThat(db.shirtsDao().createShirtsIfNotExists(shirts), is(-1L));
    }

    @Test
    public void testCreateIfNotExists_doesNotExist() {
        ShirtsEntity shirts = TestUtil.createShirtsEntity(2,"https://unsplash.it/128/128","Barronett Higgins","green");

        assertThat(db.shirtsDao().createShirtsIfNotExists(shirts), is(1L));
    }


}
