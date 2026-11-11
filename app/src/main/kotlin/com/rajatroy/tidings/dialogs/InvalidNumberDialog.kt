package com.rajatroy.tidings.dialogs

import com.rajatroy.tidings.databinding.DialogInvalidNumberBinding
import org.fossify.commons.activities.BaseSimpleActivity
import org.fossify.commons.extensions.getAlertDialogBuilder
import org.fossify.commons.extensions.setupDialogStuff

class InvalidNumberDialog(val activity: BaseSimpleActivity, val text: String) {
    init {
        val binding = DialogInvalidNumberBinding.inflate(activity.layoutInflater).apply {
            dialogInvalidNumberDesc.text = text
        }

        activity.getAlertDialogBuilder()
            .setPositiveButton(org.fossify.commons.R.string.ok) { _, _ -> }
            .apply {
                activity.setupDialogStuff(binding.root, this)
            }
    }
}
