package com.sjl.core.mvvm.util

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType


object ViewModelUtils {

    fun <VM : ViewModel> createViewModel(
            activity: ComponentActivity,
            factory: ViewModelProvider.Factory? = null,
            position: Int
    ): VM {
        val vbClass =
                (activity.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<*>>()
        val viewModel = vbClass[position] as Class<VM>
        return factory?.let {
            ViewModelProvider(
                    activity,
                    factory
            ).get(viewModel)
        } ?: let {
            ViewModelProvider(activity).get(viewModel)
        }
    }

    fun <VM : ViewModel> createViewModel(
            fragment: Fragment,
            factory: ViewModelProvider.Factory? = null,
            position: Int
    ): VM {
        val vbClass =
                (fragment.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<*>>()
        val viewModel = vbClass[position] as Class<VM>
        return factory?.let {
            ViewModelProvider(
                    fragment,
                    factory
            ).get(viewModel)
        } ?: let {
            ViewModelProvider(fragment).get(viewModel)
        }
    }

    /**
     * 直接创建
     */
    fun <VM : ViewModel> createActivityViewModel(
            fragment: Fragment,
            factory: ViewModelProvider.Factory? = null,
            position: Int
    ): VM {
        val vbClass =
                (fragment.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<*>>()
        val viewModel = vbClass[position] as Class<VM>
        return factory?.let {
            ViewModelProvider(
                    fragment.requireActivity(),
                    factory
            ).get(viewModel)
        } ?: let {
            ViewModelProvider(fragment.requireActivity()).get(viewModel)
        }
    }


}