package com.example.a277hackathon.network;

public class DownloadRunnable implements Runnable {
    String downloadUrl;

    DownloadRunnable(String url) {
        this.downloadUrl = url;
    }

    @Override
    public void run() {
        // Download the desired file in the background to storage
    }
}