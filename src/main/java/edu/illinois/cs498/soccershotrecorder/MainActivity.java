package edu.illinois.cs498.soccershotrecorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnClickListener{

    SharedPreferences prefs = null;
    private Button playerNameButton;
    private Button newGameButton;

    String my_team_name;
    String opp_team_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("edu.illinois.cs498.soccershotrecorder", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {

            prefs.edit().putBoolean("firstrun", false).commit();
            Intent intent = new Intent(this, FirstTimeDialogActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_main);
            playerNameButton = (Button) findViewById(R.id.playerButton);
            playerNameButton.setText(prefs.getString("selected_player_name", "failed"));
            playerNameButton.setPaintFlags(playerNameButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            playerNameButton.setOnClickListener(this);
            newGameButton = (Button) findViewById(R.id.newGameButton);
            newGameButton.setOnClickListener(this);
        }
    }

    public void onClick(View v) {

        if (v.getId() == R.id.playerButton) {
            Intent intent = new Intent(this, PlayerListActivity.class);
            startActivity(intent);
        }
        else if (v.getId() == R.id.newGameButton){

            View view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.enter_teamnames_dialog, null);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
            alertBuilder.setView(view);
            final EditText userInput = (EditText) view.findViewById(R.id.userinput);
            final EditText userInput2 = (EditText) view.findViewById(R.id.userinput2);


            alertBuilder.setCancelable(true)
                    .setTitle("Enter Team Names")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            my_team_name = userInput.getText().toString();
                            opp_team_name = userInput2.getText().toString();

                            if (my_team_name.isEmpty()) my_team_name = "My Team";
                            if (opp_team_name.isEmpty()) opp_team_name = "Opp Team";

                            onGoToRecordFieldActivity();
                        }
                    });

            Dialog dialog = alertBuilder.create();
            dialog.show();

        }
    }

    private void onGoToRecordFieldActivity() {
        Intent intent = new Intent(this, RecordFieldActivity.class);
        Bundle extras = new Bundle();

        extras.putString("my_team_name", my_team_name);
        extras.putString("opp_team_name", opp_team_name);
        extras.putString("player_name", prefs.getString("selected_player_name", "failed"));

        intent.putExtras(extras);
        startActivity(intent);
    }
}
