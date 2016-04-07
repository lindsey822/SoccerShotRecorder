package edu.illinois.cs498.soccershotrecorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Author: Lindsey Liu
 * Date: 16-04-02
 */
public class FieldSummaryActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_summary_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        String filename = intent.getStringExtra("image_filename");
        Log.d("Field Summary", filename);

        if (!filename.isEmpty()) {
            Context context = getApplicationContext();
            Bitmap bmap = GlobalHelper.readBitmapFile(context, filename);

            ((ImageView)findViewById(R.id.image)).setImageDrawable(new BitmapDrawable(getResources(), bmap));

        }
    }
}
