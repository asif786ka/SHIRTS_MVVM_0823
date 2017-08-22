

package com.android.mvvm.shirts.repository;

import com.android.mvvm.shirts.AppExecutors;
import com.android.mvvm.shirts.api.ApiResponse;
import com.android.mvvm.shirts.api.ShirtService;
import com.android.mvvm.shirts.db.ShirtsDao;
import com.android.mvvm.shirts.db.ShirtsDb;
import com.android.mvvm.shirts.util.RateLimiter;
import com.android.mvvm.shirts.vo.Resource;
import com.android.mvvm.shirts.vo.Shirt;
import com.android.mvvm.shirts.vo.ShirtsEntity;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Repository that handles ShirtsRepo instances.
 *
 * unfortunate naming :/ .
 * ShirtsRepo - value object name
 * Repository - type of this class.
 */
@Singleton
public class ShirtsRepository {

    private final ShirtsDb db;

    private final ShirtsDao shirtsDao;

    private final ShirtService shirtService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public ShirtsRepository(AppExecutors appExecutors, ShirtsDb db, ShirtsDao shirtsDao,
                            ShirtService shirtService) {
        this.db = db;
        this.shirtsDao = shirtsDao;
        this.shirtService = shirtService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<ShirtsEntity>>> loadShirts(String color, String name) {
        return new NetworkBoundResource<List<ShirtsEntity>, List<Shirt>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Shirt> shirts) {


                List<ShirtsEntity> shirtEntities = new ArrayList<ShirtsEntity>();

                ShirtsEntity ShirtsEntity;
                    for (int i = 0; i < shirts.size(); i++) {
                        Shirt shirt = shirts.get(i);
                        ShirtsEntity = new ShirtsEntity(shirt.getId(),shirt.getPicture(),shirt.getName(),shirt.getColour());
                        shirtEntities.add(ShirtsEntity);
                    }


                    shirtsDao.insertShirts(shirtEntities);

                Timber.d("rece saved contributors to db");
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ShirtsEntity> data) {
                Timber.d("rece contributor list from db: %s", data);
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<ShirtsEntity>> loadFromDb() {
                return shirtsDao.loadShirts();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Shirt>>> createCall() {
                return shirtService.loadShirts();
            }
        }.asLiveData();
    }

}
