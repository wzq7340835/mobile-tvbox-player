package com.player.tvbox.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.player.tvbox.R;
import com.player.tvbox.player.PlayerManager;

/**
 * 设置 Activity
 */
public class SettingsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, new SettingsFragment())
                    .commit();
        }
    }
    
    /**
     * 设置 Fragment
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_root, rootKey);
            
            // 绑定播放器类型设置
            findPreference("player_type").setOnPreferenceChangeListener(
                (preference, newValue) -> {
                    // 重启应用以应用新设置
                    // getActivity().recreate();
                    return true;
                });
        }
    }
}
