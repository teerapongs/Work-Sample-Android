package com.bananacoding.weather1;

import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.widget.TextView;

        import com.bananacoding.weather1.helper.RSSParser;
import com.bananacoding.weather1.model.RSSWeather;

public class Summit extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    private static String weatherUrl = "http://weather.yahooapis.com/forecastrss?w=12756339&u=c";
    RSSParser parser = new RSSParser();
    RSSWeather weather;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summit);
        textView = (TextView) findViewById(R.id.textView);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if (message.trim().isEmpty()){
            textView.setText("No Information for this Latitude and Longitude");
        } else {
            weatherUrl = "http://weather.yahooapis.com/forecastrss?w="+ message +"&u=c";
            new loadRSSFeedItems().execute(weatherUrl);
        }
    }

    class loadRSSFeedItems extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Summit.this);
            pDialog.setMessage("Loading weather...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            String rss_url = args[0];
            weather = parser.getRSSFeedWeather(rss_url);
            runOnUiThread(new Runnable() {
                public void run() {
                    String description = String.format("title: %s \n pubdate: %s \n temp: %s \n link: %s " , weather.getTitle(),weather.getPubdate(),weather.getTemp(),weather.getLink());
                    textView.setText(description);
                }
            });
            return null;
        }

        protected void onPostExecute(String args) {
            pDialog.dismiss();
        }
    }
}