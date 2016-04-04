package edu.illinois.cs498.draganddroppractice;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Lindsey Liu on 16-04-02.
 */
public class GameSummaryActivity extends Activity {
    List<String> dot_descriptions = GlobalConst.dot_descriptions;
    int num_types = GlobalConst.num_types;

    Boolean from_curr_game;
    HashMap<Integer, HashMap<Integer, Integer>> shots;
    HashMap<Integer, HashMap<Integer, Integer>> counts;

    String my_team_name;
    String opp_team_name;
    String player_name;
    String my_team_score;
    String opp_team_score;

    //bitmap of field view for first half
    Bitmap bmap_first;
    //bitmap of field view for second half
    Bitmap bmap_second;
    //thumbnail of field view for first half
    Bitmap thumbnail_first;
    //thumbnail of field view for second half
    Bitmap thumbnail_second;

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

        shots = (HashMap<Integer, HashMap<Integer, Integer>>)bundle.getSerializable("shots");
        counts = (HashMap<Integer, HashMap<Integer, Integer>>)bundle.getSerializable("counts");

        from_curr_game = bundle.getBoolean("from_curr_game");

        bmap_first = bundle.getParcelable("field_bitmap_first");
        bmap_second = bundle.getParcelable("field_bitmap_second");
        thumbnail_first = bundle.getParcelable("field_thumbnail_first");
        thumbnail_second = bundle.getParcelable("field_thumbnail_first");

        ((TextView)findViewById(R.id.my_team_name)).setText(my_team_name);
        ((TextView)findViewById(R.id.opp_team_name)).setText(opp_team_name);
        ((TextView)findViewById(R.id.my_team_score)).setText(my_team_score);
        ((TextView)findViewById(R.id.opp_team_score)).setText(opp_team_score);

        ((TextView)findViewById(R.id.goals1)).setText(counts.get(0).get(0));
        ((TextView)findViewById(R.id.misses1)).setText(counts.get(0).get(1));
        ((TextView)findViewById(R.id.penalty_kicks1)).setText(counts.get(0).get(2));
        ((TextView)findViewById(R.id.red_cards1)).setText(counts.get(0).get(3));
        ((TextView)findViewById(R.id.yellow_cardss1)).setText(counts.get(0).get(4));
        /*
        ((TextView)findViewById(R.id.goals1)).setText(counts.get(1).get(0));
        ((TextView)findViewById(R.id.goals1)).setText(counts.get(1).get(1));
        ((TextView)findViewById(R.id.goals1)).setText(counts.get(1).get(2));
        ((TextView)findViewById(R.id.goals1)).setText(counts.get(1).get(3));
        ((TextView)findViewById(R.id.goals1)).setText(counts.get(1).get(4));

        ((TextView)findViewById(R.id.player_text)).setText(player_name + "'S PERFORMANCE");
        * */


        findViewById(R.id.field_thumbnail_first).setBackground(new BitmapDrawable(getResources(), thumbnail_first));
        //findViewById(R.id.field_thumbnail_second).setBackground(new BitmapDrawable(getResources(), thumbnail_second));

        (findViewById(R.id.field_thumbnail_first)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToFieldSummaryActivity();
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
                        + " : " + opp_team_score + " " + opp_team_name + "\n");
                bodyBuffer.append(player_name + "'s performance for first half:\n");
                for (int i=0;i<num_types;i++) {
                    bodyBuffer.append(dot_descriptions.get(i) + " " + counts.get(0).get(i));
                }
                bodyBuffer.append(player_name + "'s performance for second half:\n");
                for (int i=0;i<num_types;i++) {
                    bodyBuffer.append(dot_descriptions.get(i) + " " + counts.get(1).get(i));
                }
                shareViaEmail(subject, bodyBuffer.toString());
            }
        });
    }

    private void onGoToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onGoToFieldSummaryActivity() {
        Intent intent = new Intent(this, FieldSummaryActivity.class);
        //intent.putExtra("image", bmap_first);
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
