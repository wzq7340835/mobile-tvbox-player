package com.fongmi.android.tv.engine

object QuickJsNative {

    init {
        System.loadLibrary("quickjs_engine")
    }

    fun init() {
        nativeInit()
    }

    fun createSpider(spiderKey: String, jsCode: String): Boolean {
        return nativeCreateSpider(spiderKey, jsCode)
    }

    fun callMethod(spiderKey: String, method: String, args: String = ""): String? {
        return nativeCallMethod(spiderKey, method, args)
    }

    fun removeSpider(spiderKey: String) {
        nativeRemoveSpider(spiderKey)
    }

    fun eval(jsCode: String): String? {
        return nativeEval(jsCode)
    }

    fun clear() {
        nativeClear()
    }

    private external fun nativeInit()
    private external fun nativeCreateSpider(spiderKey: String, jsCode: String): Boolean
    private external fun nativeCallMethod(spiderKey: String, method: String, args: String): String?
    private external fun nativeRemoveSpider(spiderKey: String)
    private external fun nativeEval(jsCode: String): String?
    private external fun nativeClear()
}
