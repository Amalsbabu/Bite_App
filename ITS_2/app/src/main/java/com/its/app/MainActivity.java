package com.its.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 12;
    private RecyclerView list;
    private RecyclerViewAdapter mAdapter;
    private List<String> dataList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    BottomSheetBehavior sheetBehavior;
    private ConstraintLayout bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = findViewById(R.id.rv);
        bottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        getData();
        setAdapter();

        findViewById(R.id.fab_choose).setOnClickListener(v -> chooseImage());
        findViewById(R.id.fab_capture).setOnClickListener(v -> startActivity(new Intent(this, ScannerActivity.class)));
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void getData() {
        ArrayList list = StoreUtil.getArrayList(getApplicationContext());
        if (list != null) {
            dataList = new ArrayList<String>();
            dataList.addAll(list);
        }
    }

    private void setAdapter() {

        mAdapter = new RecyclerViewAdapter(getApplicationContext(), dataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        list.setLayoutManager(layoutManager);
        list.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener((view, position, model) -> {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("text", model);
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        if (mAdapter != null) {
            mAdapter.updateList(dataList);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                processImage(bitmap);
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            StoreUtil.saveArrayList(getApplicationContext(), new ArrayList<>());
            getData();
            mAdapter.updateList(dataList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
