package com.rajatroy.tidings.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rajatroy.tidings.extensions.conversationsDB
import com.rajatroy.tidings.extensions.markThreadMessagesRead
import com.rajatroy.tidings.helpers.MARK_AS_READ
import com.rajatroy.tidings.helpers.THREAD_ID
import com.rajatroy.tidings.helpers.refreshConversations
import org.fossify.commons.extensions.notificationManager
import org.fossify.commons.helpers.ensureBackgroundThread

class MarkAsReadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            MARK_AS_READ -> {
                val threadId = intent.getLongExtra(THREAD_ID, 0L)
                context.notificationManager.cancel(threadId.hashCode())
                ensureBackgroundThread {
                    context.markThreadMessagesRead(threadId)
                    context.conversationsDB.markRead(threadId)
                    refreshConversations()
                }
            }
        }
    }
}
