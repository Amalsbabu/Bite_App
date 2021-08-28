package com.its.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ScannerActivity extends AppCompatActivity {


    private CameraView camera;
    private static final String TAG = "ScannerActivity";
    private ConstraintLayout bottomSheet;
    BottomSheetBehavior sheetBehavior;
    private ImageView playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        camera = findViewById(R.id.camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bottomSheet = findViewById(R.id.bottom_sheet);
        FloatingActionButton fab = findViewById(R.id.fab);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab.setOnClickListener(view -> {
            camera.takePicture();
        });
        camera.setLifecycleOwner(this);


        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                if (result != null) {
                    result.toBitmap(bitmap -> {
                        processImage(bitmap);
                    });
                }
            }
        });

    }

    private void processImage(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        recognizer.processImage(image)
                .addOnSuccessListener(
                        texts -> {
                            new Handler().postDelayed(() -> {
                                sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                if (texts.getText().length() == 0) {
                                    Toast.makeText(getApplicationContext(), "No text found", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Intent intent = new Intent(this, ResultActivity.class);
                                intent.putExtra("text", texts.getText());
                                startActivity(intent);
                            }, 400);

                        })
                .addOnFailureListener(
                        e -> {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            Toast.makeText(getApplicationContext(), "Error:  ".concat(e.getMessage()), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
