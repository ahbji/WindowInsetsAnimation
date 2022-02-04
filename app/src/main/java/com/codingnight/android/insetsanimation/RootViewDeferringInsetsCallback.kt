package com.codingnight.android.insetsanimation

import android.view.View
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.WindowInsetsCompat

class RootViewDeferringInsetsCallback(
    val persistentInsetTypes: Int,
    val deferredInsetTypes: Int
): OnApplyWindowInsetsListener {

    init {
        require(persistentInsetTypes and deferredInsetTypes == 0) {
            "persistentInsetTypes and deferredInsetTypes can not contain any of " +
                    " same WindowInsetsCompat.Type values"
        }
    }

    private var deferredInsets = false

    override fun onApplyWindowInsets(
        v: View,
        windowInsets: WindowInsetsCompat
    ): WindowInsetsCompat {
        val types = when {
            // 只应用 systemBars() insets
            deferredInsets -> persistentInsetTypes
            // systemBars() and ime() insets 组合应用
            else -> persistentInsetTypes or deferredInsetTypes
        }

        // Finally we apply the resolved insets by setting them as padding
        val typeInsets = windowInsets.getInsets(types)
        v.setPadding(typeInsets.left, typeInsets.top, typeInsets.right, typeInsets.bottom)

        // 阻止 insets 被分派到视图层次结构中
        return WindowInsetsCompat.CONSUMED
    }

}