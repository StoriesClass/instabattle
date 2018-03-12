package me.instabattle.app.utils

import android.content.Context
import org.jetbrains.anko.toast

object Utils {
    fun showToast(ctx: Context, message: String) = ctx.toast(message)
}
