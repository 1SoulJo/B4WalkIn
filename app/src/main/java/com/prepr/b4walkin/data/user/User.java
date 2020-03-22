package com.prepr.b4walkin.data.user;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "f_name")
    private String fName;

    @ColumnInfo(name = "l_name")
    private String lName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;
}
