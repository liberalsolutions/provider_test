package com.ph.testtt.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "random_strings")
data class RandomStringEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: String,
    val length: Int,
    val created: String
)
