package com.bananacoding.weather1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bananacoding.weather1.helper.RSSParser;
import com.bananacoding.weather1.model.RSSWeather;

public class MainActivity extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "RECEIVE DATA";
    private ProgressDialog pDialog;
    RSSParser parser = new RSSParser();
    RSSWeather woeid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnClick = (Button) findViewById(R.id.btnClick);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Summit Parameter
                double toLatitude = 0.0;
                double toLongitude = 0.0;

                EditText txtLatitude = (EditText) findViewById(R.id.txtLatitude);
                String latitude = txtLatitude.getText().toString();
                EditText txtLongitude = (EditText) findViewById(R.id.txtLongitude);
                String longitude = txtLongitude.getText().toString();
                if (latitude.trim().isEmpty() || longitude.trim().isEmpty()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Warning!!!");
                    alertDialog.setMessage("Information isEmpty");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    try {
                        toLatitude = Double.parseDouble(latitude);
                        toLongitude = Double.parseDouble(longitude);
                        if (toLatitude > 90 || toLatitude < -90 || toLongitude > 180 || toLongitude < -180) {
                            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                            alertDialog.setTitle("Warning!!!");
                            alertDialog.setMessage("Please input Latitude number -90 to 90 and Longitude number -180 to 180 only");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        } else {
                            String url = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.placefinder%20where%20text=%22" + toLatitude + "," + toLongitude + "%22%20and%20gflags=%22R%22";
                            new loadRSSFeedItems().execute(url);
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Number only!!");
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Warning!!!");
                        alertDialog.setMessage("Information is not number");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            }

            class loadRSSFeedItems extends AsyncTask<String, String, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pDialog = new ProgressDialog(MainActivity.this);
                    pDialog.setMessage("Loading weather...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                /**
                 * getting all recent data and showing them in text view.
                 */
                @Override
                protected String doInBackground(String... args) {
                    // rss link url
                    String query_url = args[0];

                    // weather object of rss.
                    woeid = parser.getRSSFeedWeather2(query_url);

                    // updating UI from Background Thread
                    runOnUiThread(new Runnable() {
                        public void run() {
                            /**
                             * Updating parsed data into text view.
                             * */
                            Intent intent = new Intent(MainActivity.this, Summit.class);
                            String description = woeid.getWoeid();
                            intent.putExtra(EXTRA_MESSAGE, description);
                            // Log.d("Long", description);
                            startActivity(intent);
                        }
                    });
                    return null;
                }

                /**
                 * After completing background task Dismiss the progress dialog
                 **/
                protected void onPostExecute(String args) {
                    // dismiss the dialog after getting all products
                    pDialog.dismiss();
                }
            }
        });
    }
}
