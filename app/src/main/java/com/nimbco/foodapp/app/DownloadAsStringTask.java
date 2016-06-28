package com.nimbco.foodapp.app;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

// Uses AsyncTask to create a task away from the main UI thread. This task takes a
// URL string and uses it to create an HttpUrlConnection. Once the connection
// has been established, the AsyncTask downloads the contents of the webpage as
// an InputStream. Finally, the InputStream is converted into a string, which is
// displayed in the UI by the AsyncTask's onPostExecute method.
public class DownloadAsStringTask extends AsyncTask<String, Void, Pair<String, Boolean>> {

    private static final String DEBUG_TAG = DownloadAsStringTask.class.getName();
    private final TaskCallbackHandler callback;

    public DownloadAsStringTask(final TaskCallbackHandler callback) {
        this.callback = callback;
    }


    @Override
    protected Pair<String, Boolean> doInBackground(String... urls) {

        // params comes from the execute() call: params[0] is the url.
        try {
            final String result = downloadUrl(urls[0]);
            return Pair.create(result, true);
        } catch (IOException e) {
            return Pair.create("Unable to retrieve web page. URL may be invalid.", false);
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Pair<String, Boolean> result) {
        callback.callback(result);
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, conn.getContentLength());
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, final int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public interface TaskCallbackHandler {
        void callback(Pair<String, Boolean> result);
    }
}
