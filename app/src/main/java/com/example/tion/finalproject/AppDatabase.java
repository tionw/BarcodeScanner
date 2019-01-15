package com.example.tion.finalproject;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

// bump version number if your schema changes
@Database(entities = {Product.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Declare your data access objects as abstract
    public abstract DatabaseInterface databaseInterface();
    // Database name to be used
    private static AppDatabase INSTANCE;
    public static final String NAME = "SavedProducts";

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, NAME).build();
        }
        return INSTANCE;
    }
}