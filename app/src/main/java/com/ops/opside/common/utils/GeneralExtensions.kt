package com.ops.opside.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

inline fun tryOrPrintException(f: () -> Unit) {
    return try {
        f()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun AppCompatActivity.launchFragment(
    fragment: Fragment,
    fragmentManager: FragmentManager,
    containerId: Int,
    bundle: Bundle? = null
) {
    tryOrPrintException {
        bundle?.let { fragment.arguments = it }

        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(containerId, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

fun AppCompatActivity.launchActivity(
    activity: Activity,
    context: Context,
    bundle: Bundle? = null
) {
    tryOrPrintException {
        val intent = Intent(
            context,
            activity::class.java
        )

        bundle?.let { intent.putExtras(bundle) }
        startActivity(intent)
    }
}

fun Fragment.launchActivity(
    activity: Activity,
    context: Context,
    bundle: Bundle? = null
) {
    tryOrPrintException {
        val intent = Intent(
            context,
            activity::class.java
        )

        bundle?.let { intent.putExtras(bundle) }
        startActivity(intent)
    }
}

