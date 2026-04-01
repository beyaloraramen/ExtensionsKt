package com.ramen.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import android.util.TypedValue

/**
 * Dispatches a custom broadcast intent to a dedicated debugging application.
 * @param tag The identifier for the source or category of the log.
 * @param msg The specific debug message or data to be recorded.
 */
fun Context.sendDebugLog(tag: String, msg: String) {
    val intent = Intent("com.ramen.debug.action.SEND_LOG")
    intent.setPackage("com.ramen.debug")
    intent.putExtra("from", packageName)
    intent.putExtra("time", System.currentTimeMillis())
    intent.putExtra("tag", tag)
    intent.putExtra("msg", msg)
    
    // Broadcast the intent for the system to deliver to the registered receiver
    sendBroadcast(intent)
}

/*
 * Displays a toast and enforces a 35-character limit by truncating at the last full word.
 * @param textStr The message string to be displayed.
 * @param showLong If true, uses Toast.LENGTH_LONG; otherwise Toast.LENGTH_SHORT.
*/
fun Context.showToast(textStr: String, showLong: Boolean = false) {
    if (textStr.isEmpty()) return

    val textMsg = if (textStr.length > 35) {
        val truncated = textStr.substring(0, 35)
        val lastSpace = truncated.lastIndexOf(' ')
        if (lastSpace != -1) truncated.substring(0, lastSpace) else truncated
    } else { textStr }

    val length = if (showLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, textMsg, length).show()
}

/*
 * Displays a toast and enforces a 35-character limit by truncating at the last full word.
 * @param textSeq The character sequence to be converted, truncated, and displayed.
 * @param showLong If true, the toast displays for a longer duration; defaults to short.
*/
fun Context.showToast(textSeq: CharSequence, showLong: Boolean = false) {
    val textStr = textSeq.toString().ifEmpty { return }
    showToast(textStr, showLong)
}

/*
 * Displays a toast and enforces a 35-character limit by truncating at the last full word.
 * @param textRes The string resource ID (R.string.name) to be fetched and displayed.
 * @param showLong If true, the toast displays for a longer duration; defaults to short.
*/
fun Context.showToast(@StringRes textRes: Int, showLong: Boolean = false) {
    val textStr = getString(textRes) ?: return
    showToast(textStr, showLong)
}

/**
 * Retrieves a color from the current theme based on a specified attribute.
 * Uses MaterialColors to ensure compatibility with Material You and dynamic themes.
 * @param attrRes The attribute resource ID to resolve (e.g., com.google.android.material.R.attr.colorPrimary).
 * @param colorFb The fallback color to return if the attribute cannot be resolved.
 * @return The resolved color integer.
 */
@ColorInt
fun Context.getMaterialColor(@AttrRes attrRes: Int, @ColorInt colorFb: Int = Color.BLACK): Int {
    return MaterialColors.getColor(this, attrRes, colorFb)
}

/**
 * Retrieves a color from the theme context of the current View.
 * Ideal for custom views where the context might be a ContextThemeWrapper.
 * @param attrRes The attribute resource ID to resolve (e.g., com.google.android.material.R.attr.colorSurface).
 * @param colorFb The fallback color to return if the attribute cannot be resolved.
 * @return The resolved color integer.
 */
@ColorInt
fun View.getMaterialColor(@AttrRes attrRes: Int, @ColorInt colorFb: Int = Color.BLACK): Int {
    return MaterialColors.getColor(this, attrRes, colorFb)
}

/**
 * Converts a Float value in Density-Independent Pixels (dp) to its equivalent value in Pixels (px).
 * @param context The context used to retrieve the device's display metrics.
 * @return The converted value in pixels as a Float.
 */
fun Float.toPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this, context.resources.displayMetrics
    )
}

/**
 * Converts an Int value in Density-Independent Pixels (dp) to its equivalent value in Pixels (px).
 * @param context The context used to retrieve the device's display metrics.
 * @return The converted value in pixels as an Int.
 */
fun Int.toPx(context: Context): Int {
    return this.toFloat().toPx(context).toInt()
}

/**
 * Extension function for Context to convert dp values to px.
 * Useful for programmatic layout adjustments where a Context is readily available.
 * @param dp The value in density-independent pixels.
 * @return The converted value in pixels as a Float.
 */
fun Context.dpToPx(dp: Float): Float {
    return dp.toPx(this)
}

/**
 * Extension function for Context to convert integer dp values to integer px.
 * Useful for setting dimensions that require whole pixel values.
 * @param dp The value in density-independent pixels.
 * @return The converted value in pixels as an Int.
 */
fun Context.dpToPx(dp: Int): Int {
    return dp.toPx(this)
}

/**
 * Copies the provided text to the Android system clipboard.
 * @param type A user-visible label for the clip data (e.g., "URL", "Address").
 * @param text The actual string content to be copied to the clipboard.
 * @return True if the text was successfully copied to the clipboard, false otherwise.
 */
fun Context.copyText(type: String?, text: String): Boolean {
    if (text.isEmpty()) return false
    val cb = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    val cd = ClipData.newPlainText(type, text)
    return cb?.let { it.setPrimaryClip(cd); true } ?: false
}

/**
 * Hides the IME (keyboard) associated with this TextView.
 * Works with EditText, AppCompatEditText, and TextInputEditText.
 * @param unFocus If true, clears focus from the view after hiding the keyboard.
 * @return True if the keyboard was visible and is now being hidden.
 */
fun TextView.hideKeyboard(unFocus: Boolean = true): Boolean {
    if (unFocus) clearFocus()
    val controller = ViewCompat.getWindowInsetsController(this)
    val rootInsets = ViewCompat.getRootWindowInsets(this)
    val imeVisible = rootInsets?.isVisible(WindowInsetsCompat.Type.ime()) == true
    return if (!imeVisible) { false } else {
        controller?.hide(WindowInsetsCompat.Type.ime())
        true
    }
}

/**
 * Shows the IME (keyboard) for this TextView.
 * Ensures the view is focused before requesting the input method.
 * @return True if the keyboard was hidden and is now being shown.
 */
fun TextView.showKeyboard(): Boolean {
    requestFocus()
    val controller = ViewCompat.getWindowInsetsController(this)
    val rootInsets = ViewCompat.getRootWindowInsets(this)
    val imeVisible = rootInsets?.isVisible(WindowInsetsCompat.Type.ime()) == true
    return if (imeVisible) { false } else {
        controller?.show(WindowInsetsCompat.Type.ime())
        true
    }
}

/**
 * Sets up a touch listener on a target view to hide the keyboard when interacted with.
 * @param target The view e.g. Root view or any other, that triggers the keyboard hide when touched.
 */
fun TextView.hideKbdOnInteract(target: View) {
    target.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            this@hideKbdOnInteract.hideKeyboard()
        }
        false 
    }
}

/**
 * Displays a Material Design Snackbar from the current View.
 * @param textStr The message to display.
 * @param showLong If true, uses Snackbar.LENGTH_LONG; otherwise, Snackbar.LENGTH_SHORT.
 * @param actionText The label for the optional action button.
 * @param actionCall The lambda function to execute when the action button is clicked.
 */
fun View.showSnackbar(
    textStr: String,
    showLong: Boolean = true,
    actionText: String? = null,
    actionCall: View.OnClickListener? = null
) {
    if (textStr.isBlank()) return
    
    val length = if (showLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    val snackbar = Snackbar.make(this, textStr, length)
    
    if (actionText != null && actionCall != null) {
        snackbar.setAction(actionText, actionCall)
    }
    
    snackbar.show()
}