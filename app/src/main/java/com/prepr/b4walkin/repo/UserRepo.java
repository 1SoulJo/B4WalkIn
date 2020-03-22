package com.prepr.b4walkin.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.prepr.b4walkin.data.AppDatabase;
import com.prepr.b4walkin.data.user.User;
import com.prepr.b4walkin.data.user.UserDao;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepo {
    private UserDao dao;
    private LiveData<List<User>> mAllUsers;

    public UserRepo(Application application) {
        AppDatabase db = AppDatabase.get(application);
        dao = db.userDao();
        mAllUsers = dao.getUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public long insert(User user) {
        Callable<Long> insertCallable = () -> dao.insert(user);

        long id = -1;
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(insertCallable);
        try {
            id = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }

        return id;
    }

    public User findById(Integer id) {
        Callable<User> search = () -> dao.searchById(id);

        User user = null;
        Future<User> future = AppDatabase.databaseWriteExecutor.submit(search);
        try {
            user = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }

        return user;
    }

    public User findByEmail(String email) {
        Callable<User> search = () -> dao.searchByEmail(email);

        User user = null;
        Future<User> future = AppDatabase.databaseWriteExecutor.submit(search);
        try {
            user = future.get();
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }

        return user;
    }
}
