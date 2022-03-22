package com.dzakwan.notesapp.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dzakwan.notesapp.data.entity.Notes

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotes(notes: Notes)

    @Query("SELECT * FROM notes_table")
    fun getAllData() : LiveData<List<Notes>>

    @Query("SELECT * FROM notes_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority() : LiveData<List<Notes>>

    @Query("SELECT * FROM notes_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority() : LiveData<List<Notes>>

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllData()
}