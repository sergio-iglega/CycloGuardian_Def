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

/**
 * Dao de usuario
 */
@Dao
public interface UserDao {
    /**
     * Selecciona todos los usuarios de la base de datos
     * @return
     */
    @Query("SELECT * FROM users")
    List<UserEntity> getAll();

    /**
     * Inserta un usuario en la base de datos
     * @param userEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity userEntity);

    /**
     * Elimina un usuario
     * @param userEntity
     */
    @Delete
    void deleteUser(UserEntity userEntity);

    /**
     * Selecciona un usuario
     * @param idUser
     * @return
     */
    @Query("select * from users where idUser like :idUser")
    UserEntity getUserById(int idUser);
}
