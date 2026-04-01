```gradle
dependencies {
    implementation 'com.github.beyaloraramen:ExtensionsKt:1.0.0'
}
```

# Gradle Dependency ↑↑↑

```kt
// Imports

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView

import com.google.android.material.R.attr as Attr // e.g. Attr.colorPrimary

import com.ramen.extensions.sendDebugLog
import com.ramen.extensions.showToast
import com.ramen.extensions.getMaterialColor
import com.ramen.extensions.toPx
import com.ramen.extensions.dpToPx
import com.ramen.extensions.copyText
import com.ramen.extensions.hideKeyboard
import com.ramen.extensions.showKeyboard
import com.ramen.extensions.hideKbdOnInteract
import com.ramen.extensions.showSnackbar

// Extensions

Context.sendDebugLog(tag: String, msg: String)

Context.showToast(textStr: String, showLong: Boolean = false)

Context.showToast(textSeq: CharSequence, showLong: Boolean = false)

Context.showToast(@androidx.annotation.StringRes textRes: Int, showLong: Boolean = false)

val color: Int = Context.getMaterialColor(@androidx.annotation.AttrRes attrRes: Int, @androidx.annotation.ColorInt colorFb: Int = Color.BLACK)

val color: Int = View.getMaterialColor(@androidx.annotation.AttrRes attrRes: Int, @androidx.annotation.ColorInt colorFb: Int = Color.BLACK)

val px: Float = Float.toPx(context: Context)

val px: Int = Int.toPx(context: Context)

val px: Float = Context.dpToPx(dp: Float)

val px: Int = Context.dpToPx(dp: Int)

val success: Boolean = Context.copyText(type: String?, text: String)

val success: Boolean = TextView.hideKeyboard(unFocus: Boolean = true)

val success: Boolean = TextView.showKeyboard()

TextView.hideKbdOnInteract(target: View)

View.showSnackbar(
    textStr: String,
    showLong: Boolean = true,
    actionText: String? = null,
    actionCall: View.OnClickListener? = null
)
```

# Public Extensions ↑↑↑

>sendDebugLog Used to Send log Using Broadcast Intent to [Debug](https://github.com/beyaloraramen/Debug) Application to Receive and View logs. Must be installed before use that extension.
