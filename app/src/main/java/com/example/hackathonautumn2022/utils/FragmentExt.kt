package com.example.hackathonautumn2022.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.hackathonautumn2022.R

fun Fragment.navigateTo(@IdRes id: Int, bundle: Bundle? = null) {
//    findNavController().navigate(
//        id,
//        null,
//        NavOptions.Builder()
//            .setEnterAnim(R.anim.slide_out_left)
//            .setExitAnim(R.anim.slide_out_right)
//            .setPopEnterAnim(R.anim.slide_in_right)
//            .setExitAnim(R.anim.slide_in_left)
//            .build()
//    )
    findNavController().navigate(id, bundle)
}

//fun Fragment.popBackStack() {
//    findNavController().popBackStack(
//        id,
//        null,
//        NavOptions.Builder()
//            .setEnterAnim(R.anim.slide_in_right)
//            .setExitAnim(R.anim.slide_out_left)
//            .setPopEnterAnim(R.anim.slide_in_left)
//            .setPopExitAnim(R.anim.slide_out_right)
//            .build()
//    )
//}

/**
 * enterAnim = resources.getAnimation(R.anim.slide_in_right),
exitAnim = resources.getAnimation(R.anim.slide_out_left),
popEnterAnim = resources.getAnimation(R.anim.slide_in_left),
popExitAnim = resources.getAnimation(R.anim.slide_out_right)
 */