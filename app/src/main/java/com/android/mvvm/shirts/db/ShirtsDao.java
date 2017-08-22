package com.android.mvvm.shirts.db;

import com.android.mvvm.shirts.vo.ShirtsRepo;
import com.android.mvvm.shirts.vo.RepoSearchResult;
import com.android.mvvm.shirts.vo.ShirtsEntity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.util.SparseIntArray;

import java.util.Collections;
import java.util.List;

/**
 * Interface for database access on ShirtsRepo related operations.
 */
@Dao
public abstract class ShirtsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ShirtsEntity... shirtses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertShirts(List<ShirtsEntity> shirts);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createShirtsIfNotExists(ShirtsEntity shirts);

    @Query("SELECT * FROM shirts")
    public abstract LiveData<List<ShirtsEntity>> loadShirts();

    @Query("SELECT * FROM ShirtsRepo WHERE id in (:repoIds)")
    protected abstract LiveData<List<ShirtsRepo>> loadById(List<Integer> repoIds);

}
