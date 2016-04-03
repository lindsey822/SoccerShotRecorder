package edu.illinois.cs498.draganddroppractice;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Lindsey Liu on 16-04-02.
 */
public class GameSummaryActivity extends Activity {

    Boolean from_curr_game;
    HashMap<Integer, HashMap<Integer, Integer>> shots;
    HashMap<Integer, HashMap<Integer, Integer>> counts;

    String my_team_name;
    String opp_team_name;
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
        * */


        findViewById(R.id.field_thumbnail_first).setBackground(new BitmapDrawable(getResources(), thumbnail_first));
        //findViewById(R.id.field_thumbnail_second).setBackground(new BitmapDrawable(getResources(), thumbnail_second));

        (findViewById(R.id.field_thumbnail_first)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToFieldSummary();
            }
        });

    }

    public void onGoToFieldSummary() {
        Intent intent = new Intent(this, FieldSummaryActivity.class);
        //intent.putExtra("image", bmap_first);
        startActivity(intent);
    }
}
