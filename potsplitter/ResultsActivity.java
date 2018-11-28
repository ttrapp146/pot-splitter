package com.ttrapp14622.potsplitter;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ResultsActivity extends AppCompatActivity {

    //displays calculations
    TextView resultsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //MobileAds.initialize(this, "ca-app-pub-5126780395628695~5196737233");

        Button resultsBackButton = (Button) findViewById(R.id.resultsBackButton);

        final Resources resources = getResources();

        AdView adview = (AdView) findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adview.loadAd(adRequest);


        resultsText = findViewById(R.id.resultsText);
        resultsText.setText(resources.getString(R.string.results_results_text, DisplayResults.displayResults()));

        //goes back to main activity
        View.OnClickListener backToMain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultsActivity.this, MainActivity.class));

            }
        };

        resultsBackButton.setOnClickListener(backToMain);

    }


}
