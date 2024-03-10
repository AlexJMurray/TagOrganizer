package com.example.tagorganizer.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.tagorganizer.R;
import java.io.File;

public class FileListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Path";

    // TODO: Rename and change types of parameters
    public static String Path;

    public FileListFragment() {
    }
    // TODO: Rename and change types and number of parameters
    public static FileListFragment newInstance(String newPath) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putString("path", newPath);
        Path = newPath;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Path = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_file_list, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        TextView noFilesText = rootView.findViewById(R.id.nofiles_textview);
            if (Path != null) {
                File root = new File(Path);
                File[] filesAndFolders = root.listFiles();
                if (filesAndFolders == null || filesAndFolders.length == 0) {
                    noFilesText.setVisibility(View.VISIBLE);
                    return rootView;
                }
                noFilesText.setVisibility(View.INVISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(new DirectoryItemAdapter(getActivity().getApplicationContext(), filesAndFolders));
            } else {
            // Handle the case where path is null
            // For example, display an error message or take appropriate action
            }
        return rootView;
    }
}