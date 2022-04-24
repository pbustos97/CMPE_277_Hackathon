package com.example.a277hackathon.network;

import static androidx.constraintlayout.widget.StateSet.TAG;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class DownloadThread extends Thread {
    String downloadUrl;
    Context ctx;
    int finalName;
    String category;

    DownloadThread(String url, Context ctx, int fileNum, String category) {
        this.downloadUrl = url;
        this.ctx = ctx;
        this.finalName = fileNum;
        this.category = category;
    }

    @Override
    public void run() {
        super.run();
        Log.d("DownloadThread", "run: after super()");
        InputStream input = null;
        OutputStream output = null;
        URLConnection connection = null;

        File dir = new File(ctx.getFilesDir(), "downloads");
        if (!dir.exists()) {
            dir.mkdir();
        }

        Log.d("DownloadThread", "run: after checking if dir exists\n" + dir.toString());

        int count;
        try {
            URL url = new URL(this.downloadUrl);
            Log.d("DownloadThread", "URL: " + url.toString());
            connection = url.openConnection();
            Log.d("DownloadThread", "connection: " + connection.toString());
            input = new BufferedInputStream(url.openStream(), 4096);
            Log.d("Downloadthread", "input: " + input.toString());
            output = new FileOutputStream(dir.toString() +
                    "/csv" + category + Integer.toString(finalName) + ".csv");
            Log.d("DownloadThread", "output: " + output.toString());

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

        } catch (Exception e) {
            Log.e("DownloadThread", e.getMessage());
            return;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException ignored) {
            }
            return;
        }
    }
}
