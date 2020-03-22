package com.prepr.b4walkin.data.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(User user);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getUsers();

    @Query("SELECT * FROM users WHERE id = :id")
    User searchById(Integer id);

    @Query("SELECT * FROM users WHERE email = :email")
    User searchByEmail(String email);
}
