package com.practice.searchengine.ui.arch

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.lang.reflect.ParameterizedType

abstract class MvvmFragment<
        Host : FragmentContract.Host?,
        VIEW_MODEL : FragmentContract.ViewModel
        >: Fragment() {

    var callBack: Host? = null
        private set

    protected lateinit var model: VIEW_MODEL
        private set

    fun hasCallBack(): Boolean {
        return callBack != null
    }

    fun noHost(): Boolean {
        return callBack == null
    }

    fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun showToast(resId: Int) {
        Toast.makeText(activity, resId, Toast.LENGTH_LONG).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // keep the call back
        try {
            callBack = context as Host
        } catch (e: Throwable) {
            val hostClassName = ((javaClass.genericSuperclass as ParameterizedType?)
                ?.actualTypeArguments?.get(1) as Class<*>).canonicalName
            throw RuntimeException(
                "Activity must implement " + hostClassName
                        + " to attach " + this.javaClass.simpleName, e
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setModel(createModel())
    }

    override fun onDetach() {
        super.onDetach()
        callBack = null
    }

    protected abstract fun createModel(): VIEW_MODEL

    protected fun setModel(model: VIEW_MODEL) {
        this.model = model
    }
}
