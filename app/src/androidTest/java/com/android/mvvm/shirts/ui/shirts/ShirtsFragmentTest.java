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
import com.android.mvvm.shirts.binding.FragmentBindingAdapters;
import com.android.mvvm.shirts.testing.SingleFragmentActivity;
import com.android.mvvm.shirts.ui.common.NavigationController;
import com.android.mvvm.shirts.util.RecyclerViewMatcher;
import com.android.mvvm.shirts.util.TaskExecutorWithIdlingResourceRule;
import com.android.mvvm.shirts.util.ViewModelUtil;
import com.android.mvvm.shirts.vo.ShirtsRepo;
import com.android.mvvm.shirts.vo.Resource;
import com.android.mvvm.shirts.vo.ShirtsEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ShirtsFragmentTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);
    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();
    private MutableLiveData<Resource<ShirtsRepo>> repo = new MutableLiveData<>();
    private MutableLiveData<Resource<List<ShirtsEntity>>> contributors = new MutableLiveData<>();
    private ShirtFragment shirtFragment;
    private ShirtViewModel viewModel;

    private FragmentBindingAdapters fragmentBindingAdapters;
    private NavigationController navigationController;


    @Before
    public void init() {
        shirtFragment = ShirtFragment.create("a", "b");
        viewModel = mock(ShirtViewModel.class);
        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);
        navigationController = mock(NavigationController.class);

        when(viewModel.getRepo()).thenReturn(repo);
        when(viewModel.getShirts()).thenReturn(contributors);

        shirtFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        //shirtFragment.dataBindingComponent = () -> fragmentBindingAdapters;
        shirtFragment.navigationController = navigationController;

        activityRule.getActivity().setFragment(shirtFragment);
    }

    @Test
    public void testLoading() {
        repo.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testValueWhileLoading() {
        /*ShirtsRepo shirts = TestUtil.createRepo("yigit", "foo", "foo-bar");
        this.repo.postValue(Resource.loading(shirts));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.name)).check(matches(
                withText(getString(R.string.repo_full_name, "yigit", "foo"))));
        onView(withId(R.id.description)).check(matches(withText("foo-bar")));*/
    }

    @Test
    public void testLoaded() throws InterruptedException {
        /*ShirtsRepo shirts = TestUtil.createRepo("foo", "bar", "buzz");
        this.repo.postValue(Resource.success(shirts));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.name)).check(matches(
                withText(getString(R.string.repo_full_name, "foo", "bar"))));
        onView(withId(R.id.description)).check(matches(withText("buzz")));*/
    }

    @Test
    public void testError() throws InterruptedException {
        repo.postValue(Resource.error("foo", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).perform(click());
        verify(viewModel).retry();
        repo.postValue(Resource.loading(null));

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
        onView(withId(R.id.name)).check(matches(
                withText(getString(R.string.repo_full_name, "shirtColor", "name"))));
        onView(withId(R.id.description)).check(matches(withText("desc")));
    }

    @Test
    public void testContributors() {
        //setContributors("aa", "bb");
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("aa"))));
        onView(listMatcher().atPosition(1))
                .check(matches(hasDescendant(withText("bb"))));
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.contributor_list);
    }

    @Test
    public void testContributorClick() {
        //setContributors("aa", "bb", "cc");
        onView(withText("cc")).perform(click());
    }

    @Test
    public void nullRepo() {
        this.repo.postValue(null);
        onView(withId(R.id.name)).check(matches(not(isDisplayed())));
    }

    @Test
    public void nullContributors() {
        //setContributors("a", "b", "c");
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("a"))));
        contributors.postValue(null);
        onView(listMatcher().atPosition(0)).check(doesNotExist());
    }


    private String getString(@StringRes int id, Object... args) {
        return InstrumentationRegistry.getTargetContext().getString(id, args);
    }
}