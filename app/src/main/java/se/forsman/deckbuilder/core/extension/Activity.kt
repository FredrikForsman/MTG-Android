package se.forsman.deckbuilder.core.extension

import android.app.Activity
import android.view.WindowManager

fun Activity.makeStatusBarTransparent() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}