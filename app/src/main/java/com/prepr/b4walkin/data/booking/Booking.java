package com.prepr.b4walkin.data.booking;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "bookings")
public class Booking {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "clinic_id")
    private Integer clinicId;

    @ColumnInfo(name = "clinic_name")
    private String clinicName;

    @ColumnInfo(name = "user")
    private Integer userId;

    @ColumnInfo(name = "date")
    private Date date;
}
