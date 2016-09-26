package com.lynlab.lucid.async;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author lyn
 * @since 2016/09/23
 */

public class DownloadFromUrlTask extends AsyncTask<Void, Void, Void> {

    private static final int BUFFER_SIZE = 8192;

    public interface OnDownloadListener {
        void onDownload(String filePath);
    }

    private static final String FILE_NAME = "temp.apk";

    private final String urlString;
    private Context context;
    private OnDownloadListener listener;

    private ProgressDialog dialog;
    private String outputFilePath;


    public DownloadFromUrlTask(Context context, String url) {
        this.context = context;
        this.urlString = url;
    }

    public void setOnDownload(OnDownloadListener listener) {
        this.listener = listener;
    }


    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.show();
    }

    @SuppressLint("SetWorldReadable")
    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int connectionLength = connection.getContentLength();

            File outputFile = new File(context.getApplicationInfo().dataDir, FILE_NAME);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFilePath = outputFile.getAbsolutePath();

            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = new BufferedInputStream(url.openStream(), BUFFER_SIZE);
            byte[] buffer = new byte[1024];
            int length;
            long total = 0;
            while ((length = is.read(buffer)) != -1) {
                fos.write(buffer, 0, length);

                total += length;
                dialog.setProgress((int) ((total * 100) / connectionLength));
            }

            fos.flush();
            outputFile.setReadable(true, false);

            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();

        if (listener != null) {
            listener.onDownload(outputFilePath);
        }
    }
}
