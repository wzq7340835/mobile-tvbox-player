package com.player.tvbox.activity;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;

/**
 * 二维码捕获 Activity（自定义）
 */
public class QrCaptureActivity extends CaptureActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 可以添加自定义配置
    }
}
