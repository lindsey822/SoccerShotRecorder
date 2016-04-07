package edu.illinois.cs498.soccershotrecorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Author: Lindsey Liu
 * Date: 16-04-02
 */
public class GameSummaryActivity extends Activity {
    List<String> dot_descriptions = GlobalConst.dot_descriptions;
    int num_types = GlobalConst.num_types;

    Boolean from_curr_game;
//    HashMap<Integer, HashMap<Integer, Integer>> shots;
    HashMap<Integer, HashMap<Integer, Integer>> counts;

    String my_team_name;
    String opp_team_name;
    String player_name;
    String my_team_score;
    String opp_team_score;

    String bmap_first_filename;
    String bmap_second_filename;
    Bitmap bmap_first;
    Bitmap bmap_second;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_summary_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        my_team_name = bundle.getString("my_team_name");
        opp_team_name = bundle.getString("opp_team_name");
        player_name = bundle.getString("player_name");
        my_team_score = bundle.getString("my_team_score");
        opp_team_score = bundle.getString("opp_team_score");

        counts = (HashMap<Integer, HashMap<Integer, Integer>>)bundle.getSerializable("counts");

        //will use this flag later after we have main activity
        from_curr_game = bundle.getBoolean("from_curr_game");

        bmap_first_filename = bundle.getString("field_bitmap_first_filename");
        bmap_second_filename = bundle.getString("field_bitmap_second_filename");
        Context context = getApplicationContext();
        bmap_first = GlobalHelper.readBitmapFile(context, bmap_first_filename);
        bmap_second = GlobalHelper.readBitmapFile(context, bmap_second_filename);
        Log.d("Game Summary", bmap_first_filename);
        Log.d("Game Summary", bmap_second_filename);

        ((TextView)findViewById(R.id.my_team_name)).setText(my_team_name);
        ((TextView)findViewById(R.id.opp_team_name)).setText(opp_team_name);
        ((TextView)findViewById(R.id.my_team_score)).setText(my_team_score);
        ((TextView)findViewById(R.id.opp_team_score)).setText(opp_team_score);

        ((TextView)findViewById(R.id.goals0)).setText("Goal " + Integer.toString(counts.get(0).get(2)));
        ((TextView)findViewById(R.id.misses0)).setText("Miss " + Integer.toString(counts.get(0).get(0)));
        ((TextView)findViewById(R.id.penalty_kicks0)).setText("Penalty Kick " + Integer.toString(counts.get(0).get(1)));
        ((TextView)findViewById(R.id.red_cards0)).setText("Red Card " + Integer.toString(counts.get(0).get(3)));
        ((TextView)findViewById(R.id.yellow_cards0)).setText("Yellow Card " + Integer.toString(counts.get(0).get(4)));

        ((TextView)findViewById(R.id.goals1)).setText("Goal " + Integer.toString(counts.get(1).get(2)));
        ((TextView)findViewById(R.id.misses1)).setText("Miss " + Integer.toString(counts.get(1).get(0)));
        ((TextView)findViewById(R.id.penalty_kicks1)).setText("Penalty Kick " + Integer.toString(counts.get(1).get(1)));
        ((TextView)findViewById(R.id.red_cards1)).setText("Red Card " + Integer.toString(counts.get(1).get(3)));
        ((TextView)findViewById(R.id.yellow_cards1)).setText("Yellow Card " + Integer.toString(counts.get(1).get(4)));

        ((TextView)findViewById(R.id.player_text)).setText(player_name.toUpperCase() + "'S PERFORMANCE");

        if (bmap_first != null) ((ImageButton)findViewById(R.id.field_thumbnail_first)).setImageDrawable(new BitmapDrawable(getResources(), bmap_first));
        (findViewById(R.id.field_thumbnail_first)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToFieldSummaryActivity(0);
            }
        });

        if (bmap_second != null) ((ImageButton)findViewById(R.id.field_thumbnail_second)).setImageDrawable(new BitmapDrawable(getResources(), bmap_second));
        (findViewById(R.id.field_thumbnail_second)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToFieldSummaryActivity(1);
            }
        });

        //add onclick listeners to the two buttons
        findViewById(R.id.main_menu_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToMainActivity();
            }
        });
        findViewById(R.id.share_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = "Check out my newest soccer game record!";
                StringBuffer bodyBuffer = new StringBuffer("");
                bodyBuffer.append(my_team_name + " " + my_team_score
                        + " : " + opp_team_score + " " + opp_team_name);
                bodyBuffer.append("\n");
                bodyBuffer.append("\n");

                bodyBuffer.append("-" + player_name + "'s performance for first half:");
                bodyBuffer.append("\n");
                for (int i=0;i<num_types;i++) {
                    bodyBuffer.append(dot_descriptions.get(i) + " " + counts.get(0).get(i));
                    bodyBuffer.append("\n");
                }
                bodyBuffer.append("\n");
                bodyBuffer.append("-" + player_name + "'s performance for second half:\n");
                for (int i=0;i<num_types;i++) {
                    bodyBuffer.append(dot_descriptions.get(i) + " " + counts.get(1).get(i));
                    bodyBuffer.append("\n");
                }
                shareViaEmail(subject, bodyBuffer.toString());
            }
        });
    }

    private void onGoToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onGoToFieldSummaryActivity(int half_flag) {
        Intent intent = new Intent(this, FieldSummaryActivity.class);
        switch (half_flag) {
            case 0:
                Log.d("Game Summary", "go to first, filename="+bmap_first_filename);
                intent.putExtra("image_filename", bmap_first_filename);
                break;
            case 1:
                Log.d("Game Summary", "go to second, filename="+bmap_second_filename);
                intent.putExtra("image_filename", bmap_second_filename);
                break;
        }
        startActivity(intent);
    }

    public void shareViaEmail(String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
