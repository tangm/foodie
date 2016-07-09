package com.nimbco.foodapp.app;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.nimbco.foodapp.app.models.Question;
import com.nimbco.foodapp.app.tasks.HTTPRequestTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mtan on 28/06/2016.
 */
public class ProcessDownloadResult {
    private static final String DEBUG_TAG = ProcessDownloadResult.class.getName();
    public static final String POST_URL = "http://poll.doganyazar.com:8001/app/click";

    public static void processRetrievalResult(final Activity activity,
                                              final Pair<String, Boolean> result,
                                              final ViewGroup layout) {
        Log.d(DEBUG_TAG, "Response text - " + result.first);

        if (result.second) {
            try {
                JSONArray questions = new JSONArray(result.first);
                for (int i = 0; i < questions.length(); i++) {

                    final JSONObject questionJSON = questions.getJSONObject(i);
                    final Question question = convertJSONToQuestion(questionJSON);
                    TextView header = new TextView(activity);
                    header.setText(question.getHeader());
                    header.setTextSize(16f);
                    header.setTypeface(null, Typeface.BOLD);

                    layout.addView(header);

                    TextView tv = new TextView(activity);
                    tv.setText(question.getText());

                    layout.addView(tv);

                    final RadioGroup group = new RadioGroup(activity);
                    group.setTag(question.getId());
                    group.setOrientation(LinearLayout.HORIZONTAL);
                    for (i = 0; i < 5; i++) {
                        RadioButton b = new RadioButton(activity);
                        b.setText(String.valueOf(i + 1));
                        b.setId(View.generateViewId());
                        group.addView(b);
                    }
                    layout.addView(group);

                    Button button = new Button(activity);
                    button.setText("Send");
                    button.setOnClickListener(new SubmitResponseClickListener(group, activity));
                    layout.addView(button);

                }
            } catch (JSONException e) {
                Log.d(DEBUG_TAG, "Could not parse JSON", e);

            }
        }
    }

    private static Question convertJSONToQuestion(final JSONObject jsonObject) throws JSONException {
        final String id = jsonObject.getString("_id");
        final String header = jsonObject.getString("header");
        final String text = jsonObject.getString("text");
        return new Question(id, header, text);
    }

    private static class SubmitResponseClickListener implements View.OnClickListener {
        private final RadioGroup group;
        private final Activity activity;

        public SubmitResponseClickListener(final RadioGroup group, final Activity activity) {
            this.group = group;
            this.activity = activity;
        }

        @Override
        public void onClick(final View v) {
            final int checkedRadioButtonId = group.getCheckedRadioButtonId();
            if (checkedRadioButtonId != -1) {
                final RadioButton selectedView = (RadioButton) group.findViewById(checkedRadioButtonId);
                Log.d(DEBUG_TAG, "Selected item was " + selectedView.getText());
                try {
                    new HTTPRequestTask(new HTTPRequestTask.TaskCallbackHandler() {
                        @Override
                        public void callback(final Pair<String, Boolean> result) {
                            Log.d(DEBUG_TAG, "Response text - " + result.first);
                            if (result.second) {
                                Toast.makeText(activity, "Submitted!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(activity, "Could not submit :(", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, POST_URL).execute(
                            new JSONObject()
                                .put("value", Integer.parseInt(selectedView.getText().toString()))
                                .put("id", group.getTag())
                                .toString()
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
