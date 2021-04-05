package com.example.firstapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.chaquo.python.*;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.BufferedReader;
import java.io.*;
import java.io.InputStreamReader;
import java.lang.reflect.Executable;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SecondFragment extends Fragment {

    TextView showRanTextView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentSecondLayout = inflater.inflate(R.layout.fragment_second, container, false);

        showRanTextView = fragmentSecondLayout.findViewById(R.id.textView_random);

        return fragmentSecondLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String count = SecondFragmentArgs.fromBundle(getArguments()).getMyArg();
        showRanTextView.setText(count);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}