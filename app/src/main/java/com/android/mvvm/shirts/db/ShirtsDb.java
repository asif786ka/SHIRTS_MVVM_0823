package com.android.mvvm.shirts.db;


import com.android.mvvm.shirts.vo.ShirtsRepo;
import com.android.mvvm.shirts.vo.RepoSearchResult;
import com.android.mvvm.shirts.vo.ShirtsEntity;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Main database description.
 */
@Database(entities = {ShirtsRepo.class,
        ShirtsEntity.class}, version = 3)
public abstract class ShirtsDb extends RoomDatabase {

    abstract public ShirtsDao shirtsDao();
}
