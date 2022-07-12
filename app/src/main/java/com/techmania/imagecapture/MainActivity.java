package com.techmania.imagecapture;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.techmania.imagecapture.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

   private StorageReference storageRef;
    ActivityMainBinding binding;
    ImageView imageView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        storageRef = FirebaseStorage.getInstance().getReference();


        binding =ActivityMainBinding.inflate(getLayoutInflater());
        View view =binding.getRoot();
        setContentView(view);

        imageView=view.findViewById(R.id.imgMain);
        button=view.findViewById(R.id.btn_camera);

    }

    public void upload(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 101);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==101)
            {
                onCaptureImageResult(data);
            }
        }






    }

    private void onCaptureImageResult(Intent data)
    {
     Bitmap thumbnail=(Bitmap)data.getExtras().get("data");

        ByteArrayOutputStream  bytes= new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG,90,bytes);
        byte bb[]= bytes.toByteArray();
        String file = Base64.encodeToString(bb,Base64.DEFAULT);
                imageView.setImageBitmap(thumbnail);
                UploadToFirebase(bb);
    }


    private void UploadToFirebase(byte[] bb)
    {
        StorageReference sr =    storageRef.child("myimages/a.jpg");

        sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MainActivity.this, "Successfully Upload", Toast.LENGTH_SHORT).show();
            }
        });




    }
}