package com.player.tvbox.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * 二维码扫描 Activity
 */
public class QrScanActivity extends AppCompatActivity {
    
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 检查相机权限
        if (checkSelfPermission(Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            startScan();
        }
    }
    
    /**
     * 请求相机权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }
    
    /**
     * 开始扫描
     */
    private void startScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("扫描 TVBox 源二维码");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(true);
        integrator.captureActivity(QrCaptureActivity.class);
        integrator.initiateScan();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, 
                                          @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScan();
            } else {
                Toast.makeText(this, R.string.permission_denied, 
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(
                    requestCode, resultCode, data);
            
            if (result.getContents() != null) {
                // 扫描成功
                String url = result.getContents();
                Intent intent = new Intent();
                intent.putExtra("url", url);
                setResult(RESULT_OK, intent);
            } else {
                // 扫描取消或失败
                setResult(RESULT_CANCELED);
            }
            
            finish();
        }
    }
}
