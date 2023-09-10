package com.example.tagorganizer.ui;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tagorganizer.R;

import java.io.File;
public class DirectoryItemAdapter extends RecyclerView.Adapter<DirectoryItemAdapter.ViewHolder>{
    Context context;
    File[] filesAndFolders;
    boolean selected = false;
    public DirectoryItemAdapter(Context context, File[] filesAndFolders){
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_directory_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File selectedFile = filesAndFolders[position];
        holder.textView.setText(selectedFile.getName());
        //toggle button vs file icon
        if(selectedFile.isDirectory()){
            holder.imageView.setVisibility(View.GONE);
            holder.toggleButton.setVisibility(View.VISIBLE);
            //toggle button show subdirectory
            if(holder.toggleButton.isChecked())
            {
                holder.toggleButton.setBackgroundResource(R.drawable.folder_arrow_open);
                holder.subDirectory.setVisibility(View.VISIBLE);

                holder.subDirectory = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.activity_file_list, null);
// Access the RecyclerView and TextView in the RelativeLayout
                RecyclerView recyclerView = holder.subDirectory.findViewById(R.id.recycler_view);
                TextView noFilesText = holder.subDirectory.findViewById(R.id.nofiles_textview);
// Retrieve the path from the selected file
                String path = selectedFile.getAbsolutePath();
// Set up the RecyclerView and its adapter
                File root = new File(path);
                File[] filesAndFolders = root.listFiles();
                if (filesAndFolders == null || filesAndFolders.length == 0) {
                    noFilesText.setVisibility(View.VISIBLE);
                } else {
                    noFilesText.setVisibility(View.INVISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new DirectoryItemAdapter(context, filesAndFolders));
                }

            }else{
                holder.subDirectory.setVisibility(View.GONE);
                holder.toggleButton.setBackgroundResource(R.drawable.folder_arrow_closed);
            }
        }else{
            holder.subDirectory.setVisibility(View.GONE);
            holder.toggleButton.setVisibility(View.GONE);
            //get image, video, music thumbnails
            //audio, video, txt, search, link, misc icons
            holder.imageView.setImageResource(R.drawable.folder_arrow_closed);
        }
        // Add an OnCheckedChangeListener to the toggle button to listen for changes in its state
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the visibility of the recycler view based on the new state of the toggle button
                holder.subDirectory.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        //open folder/file
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedFile.isDirectory()){
                    Intent intent = new Intent(context, FileListActivity.class);
                    String path = selectedFile.getAbsolutePath();
                    intent.putExtra("path",path);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    //open the file
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "image/*";
                        intent.setDataAndType(Uri.parse(selectedFile.getAbsolutePath()), type);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(context.getApplicationContext(),"Cannot open the file",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        //selection mode
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.getMenu().add("RENAME"); //edit if txt
                popupMenu.getMenu().add("DETAILS");

                popupMenu.getMenu().add("TAG");
                popupMenu.getMenu().add("MOVE");
                popupMenu.getMenu().add("DELETE");
                popupMenu.getMenu().add("SHARE");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("DELETE")){
                            boolean deleted = selectedFile.delete();
                            if(deleted){
                                Toast.makeText(context.getApplicationContext(),"DELETED ",Toast.LENGTH_SHORT).show();
                                v.setVisibility(View.GONE);
                            }
                        }
                        if(item.getTitle().equals("MOVE")){
                            Toast.makeText(context.getApplicationContext(),"MOVED ",Toast.LENGTH_SHORT).show();
                        }
                        if(item.getTitle().equals("RENAME")){
                            Toast.makeText(context.getApplicationContext(),"RENAME ",Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        ToggleButton toggleButton;
        RelativeLayout subDirectory;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.file_name_text_view);
            imageView = itemView.findViewById(R.id.icon_view);
            toggleButton = itemView.findViewById(R.id.toggleButton);
            subDirectory = itemView.findViewById(R.id.subDirectoryComponent);
        }
    }
}
