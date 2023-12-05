package com.example.mipt6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class DataLoader {

    private DataLoaderCallback callback;
    private ArrayList<String> cachedData;
    private Handler handler = new Handler(Looper.getMainLooper());

    public interface DataLoaderCallback {
        void onDataLoaded(ArrayList<String> data);
        void onError(String errorMessage);
    }

    public DataLoader(DataLoaderCallback callback) {
        this.callback = callback;
    }

    public void loadData(String currencyCode) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                ArrayList<String> data = Parser.parseXml(stringBuilder.toString());
                cachedData = data;
                handler.post(() -> callback.onDataLoaded(data));

            } catch (IOException e) {
                e.printStackTrace();
                handler.post(() -> callback.onError("Loading error"));
            }
        });
    }

    public ArrayList<String> getCachedData() {
        return cachedData;
    }
}