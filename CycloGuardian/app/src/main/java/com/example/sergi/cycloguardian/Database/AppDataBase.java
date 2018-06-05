package com.example.sergi.cycloguardian.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.sergi.cycloguardian.Models.Incidence;

/**
 * Created by sergi on 23/04/2018.
 */

@Database(entities = {SessionEntity.class, PhotoEntity.class, IncidenceEntity.class, UserEntity.class}, version = 4)
public abstract class AppDataBase extends RoomDatabase {

    public abstract SessionDao sessionDao();
    public abstract IncidenceDao incidenceDao();
    public abstract PhotoDao photoDao();
    public abstract UserDao userDao();
    private static AppDataBase myAppDataBase = null;



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

    public  void destroyInstance() {
        myAppDataBase = null;
    }
}
