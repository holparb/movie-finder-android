package com.holparb.moviefinder.core.domain.util.toast_display

interface ToastDisplay {
    fun showShortToast(message: String)
    fun showLongToast(message: String)
}