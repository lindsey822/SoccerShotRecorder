package edu.illinois.cs498.soccershotrecorder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kevin on 3/29/16.
 */
public class FirstTimeDialogActivity extends Activity implements View.OnClickListener {

    SharedPreferences prefs;
    private Button nameButton;
    private Button continueButton;
    private EditText nameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_time_dialog);

        prefs = getSharedPreferences("edu.illinois.cs498.soccershotrecorder", MODE_PRIVATE);

        continueButton = (Button) findViewById(R.id.continue_Button);
        continueButton.setOnClickListener(this);

    }

    public void onClick(View v) {

        if (v.getId() == R.id.continue_Button) {
            nameText = (EditText)findViewById(R.id.firstname);
            prefs.edit().putString("selected_player_name", nameText.getText().toString()).commit();
            //prefs.edit().putInt("player_count", 1).commit();
            Set<String> playerSet = new HashSet<String>();
            playerSet.add(nameText.getText().toString());
            prefs.edit().putStringSet("playerSet", playerSet).commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}

