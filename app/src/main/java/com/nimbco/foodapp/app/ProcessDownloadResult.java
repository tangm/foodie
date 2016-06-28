package com.nimbco.foodapp.app;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.nimbco.foodapp.app.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by mtan on 28/06/2016.
 */
public class ProcessDownloadResult {
    private static final String DEBUG_TAG = ProcessDownloadResult.class.getName();

    public static void processRetrievalResult(final Activity activity, final Pair<String, Boolean> result, final ViewGroup layout, final TextView response) {
        response.setText(result.first);
        JSONObject jsonObject = null;
        if (result.second) {
            try {
                jsonObject = new JSONObject(result.first);
                final JSONObject survey = jsonObject.getJSONObject("survey");
                final Iterator<String> keys = survey.keys();
                while (keys.hasNext()) {
                    final String next = keys.next();
                    TextView tv = new TextView(activity);
                    tv.setText(survey.getString(next));

                    layout.addView(tv);

                    RadioGroup group = new RadioGroup(activity);
                    group.setOrientation(LinearLayout.HORIZONTAL);
                    for (int i = 0; i < 5; i++) {
                        RadioButton b = new RadioButton(activity);
                        b.setText(String.valueOf(i + 1));
                        group.addView(b);
                    }
                    layout.addView(group);

                }
            } catch (JSONException e) {
                Log.d(DEBUG_TAG, "Could not parse JSON", e);

            }
        }
    }
}
