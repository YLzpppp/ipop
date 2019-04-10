package com.jasoncareter.ipop.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {ContactsEntity.class , MeInfoEntity.class}, version = 2 ,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

        private static final String databaseName = "mDataBase";
        private static final Object locker = new Object();
        private static volatile AppDataBase instance = null ;

        public static AppDataBase getInstance(Context context){
            synchronized (locker) {
                if (instance != null) {
                    return instance;
                } else {
                    if ( context == null){
                        Log.i("tag", "getInstance: "+"context is null");
                    }
                    instance = Room.databaseBuilder(context, AppDataBase.class, databaseName).build();
                    return instance;
                }
            }
        }
        public abstract ContactsDao contactsDao();
        public abstract MeInfoDao meInfoDao();
}
