package com.rajatroy.tidings.interfaces

import androidx.room.Dao
import androidx.room.Query
import com.rajatroy.tidings.models.Attachment

@Dao
interface AttachmentsDao {
    @Query("SELECT * FROM attachments")
    fun getAll(): List<Attachment>
}
