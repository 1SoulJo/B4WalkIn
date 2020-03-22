package com.prepr.b4walkin.data.clinic;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(tableName = "clinics")
public class Clinic {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "doctor")
    private Integer doctorId;

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "operation_time")
    private String operationTime;

    @ColumnInfo(name = "lat")
    private Double latitude = 0d;

    @ColumnInfo(name = "long")
    private Double longitude = 0d;

}
