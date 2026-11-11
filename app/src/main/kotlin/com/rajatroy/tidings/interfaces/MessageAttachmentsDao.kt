package com.rajatroy.tidings.interfaces

import androidx.room.Dao
import androidx.room.Query
import com.rajatroy.tidings.models.MessageAttachment

@Dao
interface MessageAttachmentsDao {
    @Query("SELECT * FROM message_attachments")
    fun getAll(): List<MessageAttachment>
}
