package edu.illinois.cs498.draganddroppractice;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by Lindsey Liu on 16-04-02.
 */
public class FieldSummaryActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_summary_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        Bitmap image = intent.getParcelableExtra("image");

        ((ImageView)findViewById(R.id.image)).setImageDrawable(new BitmapDrawable(getResources(), image));

    }
}
