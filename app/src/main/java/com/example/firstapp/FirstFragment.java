package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.chaquo.python.*;

import java.nio.ByteBuffer;
import java.util.List;

public class FirstFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    TextView showCountTextView;
    byte[] bytes;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        // Get the count text view
        showCountTextView = fragmentFirstLayout.findViewById(R.id.textview_first);

        return fragmentFirstLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.Photo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);            }
        });

        getView().findViewById(R.id.Results_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String MyText = "Take a picture first!";
                Toast myToast = Toast.makeText(getActivity(), MyText, Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            // My photo variable is in the Bitmap photo variable. It needs to be sent to the python code.
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            imageView = getView().findViewById(R.id.imageView);
            imageView.setImageBitmap(bmp);
            Bitmap profileImage = Bitmap.createScaledBitmap(bmp, 224, 224, false);
            imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 224, 224, false));
            imageView.findViewById(R.id.imageView);



            System.out.println(bmp.getRowBytes());
            System.out.println(bmp.getWidth());
            System.out.println(bmp.getHeight());

            byte[] bytes;
            int r = profileImage.getHeight();
            int c = profileImage.getWidth();
            bytes = getImagePixels(profileImage);
            //int [] array_int = toIntArray(bytes);

            // assume python.start() is elsewhere

            Python py = Python.getInstance();
            PyObject py_module = py.getModule("test_2");

            PyObject result  = py_module.callAttr("pic_func", bytes, r, c); //.toJava(byte[].class);
            // compare the values at some random location to see make sure result is as expected
            //System.out.println("Compare: "+Byte.toString(bytes[2]) + " and " + Byte.toString(result[2]));
            //showCountTextView.setText(result.toString());
            List<PyObject> name = result.asList();
            showCountTextView.setText(name.get(0).toString());

            getView().findViewById(R.id.Results_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(name.get(1).toString());
                    NavHostFragment.findNavController(FirstFragment.this).navigate(action);
                    //Intent intent = new Intent(FirstFragment.this,.class).putExtra("myCustomerObj",customerObj);
                }
            });

        }
    }

    public byte[] getImagePixels(Bitmap image) {
        // calculate how many bytes our image consists of
        int bytes = image.getByteCount();

        ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer
        image.copyPixelsToBuffer(buffer); // Move the byte data to the buffer

        byte[] temp = buffer.array(); // Get the underlying array containing the data.

        byte[] pixels = new byte[(temp.length / 4) * 3]; // Allocate for 3 byte BGR

        // Copy pixels into place
        for (int i = 0; i < (temp.length / 4); i++) {
            pixels[i * 3] = temp[i * 4 + 3];     // B
            pixels[i * 3 + 1] = temp[i * 4 + 2]; // G
            pixels[i * 3 + 2] = temp[i * 4 + 1]; // R

            // Alpha is discarded
        }

        return pixels;
    }
}