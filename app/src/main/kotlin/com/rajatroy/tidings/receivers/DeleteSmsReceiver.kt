package com.rajatroy.tidings.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rajatroy.tidings.extensions.deleteMessage
import com.rajatroy.tidings.extensions.updateLastConversationMessage
import com.rajatroy.tidings.helpers.IS_MMS
import com.rajatroy.tidings.helpers.MESSAGE_ID
import com.rajatroy.tidings.helpers.THREAD_ID
import com.rajatroy.tidings.helpers.refreshConversations
import com.rajatroy.tidings.helpers.refreshMessages
import org.fossify.commons.extensions.notificationManager
import org.fossify.commons.helpers.ensureBackgroundThread

class DeleteSmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val threadId = intent.getLongExtra(THREAD_ID, 0L)
        val messageId = intent.getLongExtra(MESSAGE_ID, 0L)
        val isMms = intent.getBooleanExtra(IS_MMS, false)
        context.notificationManager.cancel(threadId.hashCode())
        ensureBackgroundThread {
            context.deleteMessage(messageId, isMms)
            context.updateLastConversationMessage(threadId)
            refreshMessages()
            refreshConversations()
        }
    }
}
