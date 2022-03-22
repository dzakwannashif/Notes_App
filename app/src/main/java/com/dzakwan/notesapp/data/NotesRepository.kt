package com.dzakwan.notesapp.data

import androidx.lifecycle.LiveData
import com.dzakwan.notesapp.data.entity.Notes
import com.dzakwan.notesapp.data.room.NotesDao

class NotesRepository(private val notesDao: NotesDao) {

//    val getAllData : LiveData<List<Notes>> = notesDao.getAllData()
    fun getAllData() : LiveData<List<Notes>> = notesDao.getAllData()

    suspend fun insertNotes(notes: Notes){
        notesDao.insertNotes(notes)
    }

    fun sortByHighPriority() : LiveData<List<Notes>> = notesDao.sortByHighPriority()

    fun sortByLowPriority() : LiveData<List<Notes>> = notesDao.sortByLowPriority()

    suspend fun deleteAllData() = notesDao.deleteAllData()
}