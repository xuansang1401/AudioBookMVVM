package com.t3h.basemvvm.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.t3h.basemvvm.data.model.api.Book
import com.t3h.basemvvm.data.room.History

@Dao
interface DatabaseDao {

    //History
    @Query("Select * from History ORDER BY time DESC limit 20")
    fun getAllHistory(): LiveData<List<Book>>
    @Query("Select * from History ORDER BY time DESC limit 3")
    fun getThreeHistory(): LiveData<List<History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDbHis(history: History): Long

    @Delete
    fun deleteHistory(history: History)

    @Query("DELETE FROM History WHERE time < :time")
    fun deleteHisByTime(time: Long)

    @Query("SELECT * FROM History WHERE idHis = :idHis")
    fun findHisById(idHis: String): List<History>
}