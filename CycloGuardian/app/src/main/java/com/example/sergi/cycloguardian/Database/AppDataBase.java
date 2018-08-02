package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.sergi.cycloguardian.Models.Incidence;

/**
 * Created by sergi on 23/04/2018.
 */

/**
 * Clase que instancia la base de datos local
 */
@Database(entities = {SessionEntity.class, PhotoEntity.class, IncidenceEntity.class, UserEntity.class}, version = 6, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    /**
     * Instancia para el DAO de sesión
     * @return SessionDao
     */
    public abstract SessionDao sessionDao();

    /**
     * Instancia para el DAO de incidencia
     * @return IncidenceDao
     */
    public abstract IncidenceDao incidenceDao();

    /**
     * Instancia para el DAO de photo
     * @return PhotoDao
     */
    public abstract PhotoDao photoDao();

    /**
     * Instancia para el DAO de usuario
     * @return UserDao
     */
    public abstract UserDao userDao();

    /**
     * Instancia de la base de datos
     */
    private static AppDataBase myAppDataBase = null;


    /**
     * Obtiene la instancia de la Base de datos
     * Singleton --> una única instancia
     * @param context
     * @return DataBase
     */
    public static AppDataBase getAppDataBase(Context context) {
        if (myAppDataBase == null) {
            myAppDataBase = Room.databaseBuilder(context.getApplicationContext(),
                        AppDataBase.class, "cycloguardian-database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return myAppDataBase;
    }

    /**
     * Destruye la instancia de la Base de datos
     */
    public  void destroyInstance() {
        myAppDataBase = null;
    }
}
