package com.example.tagorganizer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.tagorganizer.ui.SearchbarFragment;
import com.example.tagorganizer.ui.FileListFragment;
import com.example.tagorganizer.ui.MediaControlFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermission()){
            //permission allowed
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            String path = Environment.getExternalStorageDirectory().getPath();
            intent.putExtra("path",path);
            startActivity(intent);
        }else{
            //permission not allowed
            requestPermission();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.searchbar_container, new SearchbarFragment()).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.file_list_container, new FileListFragment()).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.media_control_container, new MediaControlFragment()).commit();

        }
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"Storage permission is required, please grant permission from Android settings",Toast.LENGTH_SHORT).show();
        }
        else
            ActivityCompat.requestPermissions(
                    MainActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
    }
}