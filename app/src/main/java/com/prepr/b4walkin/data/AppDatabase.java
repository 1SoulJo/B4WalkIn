package com.prepr.b4walkin.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.prepr.b4walkin.data.booking.Booking;
import com.prepr.b4walkin.data.booking.BookingDao;
import com.prepr.b4walkin.data.clinic.Clinic;
import com.prepr.b4walkin.data.clinic.ClinicDao;
import com.prepr.b4walkin.data.user.User;
import com.prepr.b4walkin.data.user.UserDao;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton class for app database
 */
@Database(entities = { Booking.class, Clinic.class, User.class }, version = 1,
        exportSchema = false)
@TypeConverters({ DateConverters.class })
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookingDao bookingDao();
    public abstract ClinicDao clinicDao();
    public abstract UserDao userDao();

    private static AppDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase get(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .addCallback(sAppDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sAppDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            databaseWriteExecutor.execute(() -> {
                ClinicDao clinicDao = INSTANCE.clinicDao();
                clinicDao.deleteAll();
                clinicDao.insert(new Clinic(null, "Clinic #1", 1,
                        "619 Lakeshore Rd E, Mississauga, ON L5G 1H9, Canada",
                        "416-857-0486", "9AM - 5PM", 43.575279, -79.571297));
                clinicDao.insert(new Clinic(null, "Clinic #2", 2,
                        "619 Lakeshore Rd E, Mississauga, ON L5G 1H9, Canada",
                        "416-857-0486", "9AM - 5PM", 43.568016, -79.568996));
                clinicDao.insert(new Clinic(null, "Clinic #3", 3,
                        "619 Lakeshore Rd E, Mississauga, ON L5G 1H9, Canada",
                        "416-857-0486", "9AM - 5PM", 43.570551, -79.565864));
            });
        }
    };
}
