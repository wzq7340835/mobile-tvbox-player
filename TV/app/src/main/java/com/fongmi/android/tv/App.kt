package com.fongmi.android.tv

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.fongmi.android.tv.rust.TvCoreNative
import com.fongmi.android.tv.engine.QuickJsNative
import com.google.gson.Gson

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        handler = Handler(Looper.getMainLooper())
        gson = Gson()
        TvCoreNative.init()
        QuickJsNative.init()
    }

    companion object {
        lateinit var instance: App
            private set
        lateinit var handler: Handler
            private set
        lateinit var gson: Gson
            private set

        fun get(): App = instance
        fun handler(): Handler = handler
        fun gson(): Gson = gson
    }
}
