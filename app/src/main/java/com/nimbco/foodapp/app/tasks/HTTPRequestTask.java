package com.nimbco.foodapp.app.tasks;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class HTTPRequestTask extends AsyncTask<String, Void, Pair<String, Boolean>> {

    private static final String DEBUG_TAG = HTTPRequestTask.class.getName();
    private final TaskCallbackHandler callback;
    private final String requestUrl;

    public HTTPRequestTask(final TaskCallbackHandler callback, final String requestUrl) {
        this.callback = callback;
        this.requestUrl = requestUrl;
    }

    @Override
    protected Pair<String, Boolean> doInBackground(String... data) {

        try {
            final String result = getUrl(data);
            return Pair.create(result, true);
        } catch (IOException e) {
            e.printStackTrace();
            return Pair.create("Unable to retrieve web page. URL may be invalid.", false);
        }
    }

    @Override
    protected void onPostExecute(Pair<String, Boolean> result) {
        callback.callback(result);
    }

    private String getUrl(final String... data) throws IOException {
        AndroidHttpClient client = null;
        try {
            client = AndroidHttpClient.newInstance("curl/7.43.0");

            HttpUriRequest request;
            if (data != null && data.length > 0) {
                final HttpPost post = new HttpPost(requestUrl);
                post.setEntity(new StringEntity(data[0]));
                request = post;
            } else {
                request = new HttpGet(requestUrl);
            }

            final HttpResponse httpResponse = client.execute(request);

            int response = httpResponse.getStatusLine().getStatusCode();
            Log.d(DEBUG_TAG, "The response is: " + response);

            return streamToString(httpResponse.getEntity().getContent(),
                    (int) httpResponse.getEntity().getContentLength());
        } finally {
            if (client != null) {
                client.close();
            }
        }

    }

    // Reads an InputStream and converts it to a String.
    public String streamToString(InputStream stream, final int len) throws IOException {
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
