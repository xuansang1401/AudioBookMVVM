package com.t3h.basemvvm.data.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.t3h.basemvvm.data.model.api.Book

@Entity
data class History(
    @PrimaryKey val idHis:String,
    @Embedded val book : Book,
    val time: Long
)