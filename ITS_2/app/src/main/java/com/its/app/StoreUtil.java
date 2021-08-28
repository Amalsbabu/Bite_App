package com.its.app;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StoreUtil {

    public static void saveArrayList(Context context, ArrayList<String> list) {
        File file = new File(context.getFilesDir(), "scans.data");
        Gson gson = new Gson();
        String json = gson.toJson(list);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<String> getArrayList(Context context) {
        File file = new File(context.getFilesDir(), "scans.data");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        Gson gson = new Gson();

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
            return new ArrayList<>();

        }
        if (text.length() == 0) {
            return new ArrayList<>();
        }
        return gson.fromJson(text.toString(), new TypeToken<ArrayList<String>>() {
        }.getType());
    }
}
