package com.example.tion.finalproject;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DatabaseInterface {

    @Query("SELECT * FROM Product")
    List<Product> getAllItems();

    @Insert
    void addProduct(Product event);

    @Delete
    void deleteProduct(Product event);

    @Insert
    void insertAll(Product... Products);

    @Query("DELETE FROM Product")
    public void dropTheTable();

}
