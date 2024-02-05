package com.example.tagorganizer.ui;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagorganizer.MainActivity;
import com.example.tagorganizer.R;

import java.io.File;

public class FileListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        if(checkPermission()){
            //permission allowed
            Intent intent = new Intent(FileListActivity.this, FileListActivity.class);
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
        int result = ContextCompat.checkSelfPermission(FileListActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                FileListActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(FileListActivity.this,"Storage permission is required, please grant permission from Android settings",Toast.LENGTH_SHORT).show();
        }
        else
            ActivityCompat.requestPermissions(
                    FileListActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
    }
}