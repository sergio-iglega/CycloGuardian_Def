package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by sergi on 10/05/2018.
 */

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<UserEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity userEntity);

    @Delete
    void deleteUser(UserEntity userEntity);
}
