package edu.illinois.cs498.soccershotrecorder;

        import android.app.AlertDialog;
        import android.app.Dialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.HashSet;
        import java.util.Set;

public class PlayerListActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    String newPlayerNameString;
    private ArrayAdapter<String> adapter;
    private String newPlayerName;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Select Player");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_list_view);

        prefs = getSharedPreferences("edu.illinois.cs498.soccershotrecorder", MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.player_list);

        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter", "Saturn", "Uranus", "Neptune"};

        ArrayList<String> planetList = new ArrayList<String>();
        //planetList.addAll(Arrays.asList(planets));

        adapter = new ArrayAdapter<String>(this, R.layout.player_list_item, planetList);
        listView.setAdapter(adapter);
        Set<String> playerSet = new HashSet<String>(prefs.getStringSet("playerSet", null));
        if (playerSet != null ) {
            for (String player : playerSet) {
                adapter.add(player);
            }
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newPlayerName = ((TextView) view).getText().toString();
                prefs.edit().putString("selected_player_name", newPlayerName).apply();
                goToMain();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                View whole_view = (LayoutInflater.from(PlayerListActivity.this)).inflate(R.layout.del_confirmation_dialog, null);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PlayerListActivity.this);
                alertBuilder.setView(whole_view);

                alertBuilder.setCancelable(true)
                        .setTitle("Are you sure you want to delete player " + ((TextView) view).getText().toString() + "?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Set<String> playerSet = new HashSet<String>(prefs.getStringSet("playerSet", null));
                                playerSet.remove(((TextView) view).getText().toString());
                                prefs.edit().putStringSet("playerSet", playerSet).commit();
                                adapter.remove(((TextView) view).getText().toString());
                                adapter.notifyDataSetChanged();
                                if (((TextView) view).getText().toString() == prefs.getString("selected_player_name", "failed")) {
                                    prefs.edit().putString("selected_player_name", "").commit();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });;

                Dialog dialog = alertBuilder.create();
                dialog.show();


                //Log.v("long clicked", "pos: " + pos);
                return true;
            }
        });


    }

    public  void goToMain () {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("newPlayerName", newPlayerName);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //new MenuInflater(this).inflate(R.menu.play_list_menu, menu);
        //super.onCreateOptionsMenu(menu, inflater);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.player_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.player_list_add_player_button :
                View view = (LayoutInflater.from(PlayerListActivity.this)).inflate(R.layout.add_player_dialog, null);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PlayerListActivity.this);
                alertBuilder.setView(view);
                final EditText newPlayerName =  (EditText) view.findViewById(R.id.new_player_name);

                alertBuilder.setCancelable(true)
                        .setTitle("Enter new player name")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newPlayerNameString = newPlayerName.getText().toString();
                                //prefs.edit().putInt("player_count", prefs.getInt("player_count", 1) + 1).commit();
                                //prefs.edit().putStringSet()
                                Set<String> playerSet = new HashSet<String>(prefs.getStringSet("playerSet", null));
                                playerSet.add(newPlayerNameString);
                                prefs.edit().putStringSet("playerSet", playerSet).commit();
                                //prefs.edit().putString("selected_player_name", newPlayerNameString).commit();
                                adapter.add(newPlayerNameString);
                                adapter.notifyDataSetChanged();
                                //goToMain();
                            }
                        });
                Dialog dialog = alertBuilder.create();
                dialog.show();
                Log.d("DEBUG", "clicked");

                return true;

            default :
                return false;
        }
    }
}

