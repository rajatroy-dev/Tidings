package com.rajatroy.tidings.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rajatroy.tidings.extensions.rescheduleAllScheduledMessages
import org.fossify.commons.helpers.ensureBackgroundThread

/**
 * Reschedules alarms after boot/package updates.
 */
class RescheduleAlarmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        ensureBackgroundThread {
            context.rescheduleAllScheduledMessages()
            pendingResult.finish()
        }
    }
}
