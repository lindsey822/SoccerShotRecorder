package edu.illinois.cs498.draganddroppractice;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Lindsey Liu
 * Date: 16-03-29
 */
public class RecordFieldActivity extends Activity implements View.OnDragListener {
    GridView gridview;
    ImageAdapter imageAdapter;

    List<Integer> dot_layouts = GlobalConst.dot_layouts;
    List<String> dot_descriptions = GlobalConst.dot_descriptions;
    int num_types = GlobalConst.num_types;

    //global info for current game, coming from the previous activity
    String my_team_name;
    String opp_team_name;
    String player_name;
    //information entered by the user while recording
    int my_team_score;
    int opp_team_score;

    //real-time updated information
    int half_flag = GlobalConst.FIRST_HALF;
    Boolean is_initial_recording = Boolean.TRUE;
    //two maps of shots corresponding to first and second half, retrieved by index = half_flag
    HashMap<Integer, HashMap<Integer, Integer>> shots;
    //counter for num shots for each type for first and second half
    HashMap<Integer, HashMap<Integer, Integer>> shot_type_counter;

    //bitmap of field view for first half
    Bitmap bmap_first;
    String bmap_first_filename = "";
    //bitmap of field view for second half
    Bitmap bmap_second;
    String bmap_second_filename = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.field_recording_view);

        //retrieve known information about this game
        /*
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        my_team_name = bundle.getString("my_team_name");
        opp_team_name = bundle.getString("opp_team_name");
        player_name = bundle.getString("player_name");
*/
        //temporarily give initialized values to above three
        my_team_name = "my team";
        opp_team_name = "opp team";
        player_name = "Eric";

        /* Initialize maps */
        shots = new HashMap<>();
        shots.put(0, new HashMap<Integer, Integer>());
        Log.d("RecordFieldActivity", "shots for first half created");
        shots.put(1, new HashMap<Integer, Integer>());//shots for second half
        Log.d("RecordFieldActivity", "shots for second half created");

        shot_type_counter = new HashMap<>();
        shot_type_counter.put(0, new HashMap<Integer, Integer>());
        shot_type_counter.put(1, new HashMap<Integer, Integer>());
        for(int i=0;i<num_types;i++) {
            shot_type_counter.get(0).put(i, 0);
            shot_type_counter.get(1).put(i, 0);
        }

        /* Set texts of information displayed on title bar */
        ((TextView)findViewById(R.id.my_team_name)).setText(my_team_name);
        ((TextView)findViewById(R.id.opp_team_name)).setText(opp_team_name);

        /* register listeners */
        findViewById(R.id.trash_view).setOnDragListener(this);

        findViewById(R.id.my_team_score_inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_team_score++;
                ((TextView) findViewById(R.id.my_team_score)).setText(Integer.toString(my_team_score));
            }
        });
        findViewById(R.id.opp_team_score_inc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opp_team_score++;
                ((TextView)findViewById(R.id.opp_team_score)).setText(Integer.toString(opp_team_score));
            }
        });
        findViewById(R.id.my_team_score_dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(my_team_score != 0) my_team_score --;
                ((TextView)findViewById(R.id.my_team_score)).setText(Integer.toString(my_team_score));
            }
        });
        findViewById(R.id.opp_team_score_dec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(opp_team_score != 0) opp_team_score --;
                ((TextView)findViewById(R.id.opp_team_score)).setText(Integer.toString(opp_team_score));
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToGameSummaryActivity();
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.half_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.half_spinner_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (is_initial_recording) {
                    is_initial_recording = Boolean.FALSE;
                } else {
                    onGoToOtherdHalf();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        View.OnDragListener onDraglistener = this;
        gridview=(GridView) findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(this, onDraglistener, shots.get(half_flag));
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView curr_grid = (ImageView) v;
                int color_index = Integer.parseInt((String) curr_grid.getTag());
                int next_color_index = color_index + 1;
                if (next_color_index == num_types) {
                    next_color_index = 0;
                }
                curr_grid.setImageResource(dot_layouts.get(next_color_index));
                curr_grid.setTag(Integer.toString(next_color_index));
                Toast.makeText(RecordFieldActivity.this, "" + dot_descriptions.get(next_color_index),
                        Toast.LENGTH_SHORT).show();

                addShot(position, next_color_index);
                if (color_index != -1) {
                    decrementCountForType(color_index);
                }
                incrementCountForType(next_color_index);

                Log.d("onClick", "shot added; color=" + dot_descriptions.get(next_color_index)
                    + " position=" + position
                    + " halfflag=" + half_flag);

            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int position, long id) {
                ImageView curr_grid = (ImageView) v;
                // if there's no dot on this grid, do nothing
                if (curr_grid.getDrawable() == null) {
                    return false;
                }
                // add color type to clipdata
                ClipData dragData = ClipData.newPlainText("", (String) curr_grid.getTag());
                // add original position to clipdata
                ClipData.Item position_item = new ClipData.Item(Integer.toString(position));
                dragData.addItem(position_item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(dragData, shadowBuilder, v, 0);

                Toast.makeText(RecordFieldActivity.this, "START DRAGGING",
                        Toast.LENGTH_SHORT).show();
                //set the image as wallpaper
                return true;
            }
        });

    }
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("shots", shots);
        outState.putSerializable("counts", shot_type_counter);
        outState.putInt("half_flag", half_flag);
        //the following could be null
        outState.putParcelable("field_bitmap_first", bmap_first);
        outState.putParcelable("field_thumbnail_first", thumbnail_first);
        //save drawables of image views of gridview as well
        int count = gridview.getCount();
        for (int i = 0; i < count; i++) {
            ImageView v = (ImageView) gridview.getChildAt(i);
            if (v != null) {
                if (v.getDrawable() != null) v.getDrawable().setCallback(null);
            }
        }
    }

    private void restore(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            places = (ArrayList<HashMap<String,String>>) savedInstanceState.getSerializable("places");
        }
    }
*/

    public void onGoToOtherdHalf() {
        //capture a bitmap and thumbnail of current field view for current(first) half
        captureBitmap(this.half_flag);
        //save shots map and screenshot to file
        saveToFile(this.half_flag);
        gridview.setDrawingCacheEnabled(false);

        //set flag to the other half
        this.half_flag = 1 - this.half_flag;

        //redraw all shots for the other half
        imageAdapter.setExistingShots(shots.get(half_flag));
        imageAdapter.notifyDataSetChanged();
    }

    //when recording ends, trigger a pop-up dialog to let the user enter final scores
    //call this method after that dialog is completed
    public void onGoToGameSummaryActivity() {
        //capture and save latest version of second half screenshot
        captureBitmap(this.half_flag);
        saveToFile(this.half_flag);
        gridview.setDrawingCacheEnabled(false);

        Intent intent = new Intent(this, GameSummaryActivity.class);
        Bundle extras = new Bundle();

        extras.putString("my_team_name", my_team_name);
        extras.putString("opp_team_name", opp_team_name);
        extras.putString("player_name", player_name);
        extras.putString("my_team_score", Integer.toString(my_team_score));
        extras.putString("opp_team_score", Integer.toString(opp_team_score));

        extras.putSerializable("counts", shot_type_counter);

        extras.putBoolean("from_curr_game", Boolean.TRUE);

        extras.putString("field_bitmap_first_filename", bmap_first_filename);
        extras.putString("field_bitmap_second_filename", bmap_second_filename);

        intent.putExtras(extras);
        startActivity(intent);
    }

    //activity as an onDragListener

    @Override
    public boolean onDrag(View view, DragEvent e) {
        final int action = e.getAction();
        ImageView v = (ImageView) view;
        Boolean isTrashCan = (v.getId() == R.id.trash_view);
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                ImageView from = (ImageView) e.getLocalState();
                from.setImageDrawable(null);
                return true;

            case DragEvent.ACTION_DRAG_ENTERED:
                if (isTrashCan) v.setBackgroundColor(Color.RED);
                else v.setColorFilter(Color.argb(50, 0, 0, 0));
                v.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                if (isTrashCan) v.setBackgroundColor(Color.TRANSPARENT);
                v.setColorFilter(Color.argb(0, 0, 0, 0));
                v.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                // Gets the item containing the dragged data
                ClipData.Item color_item = e.getClipData().getItemAt(0);
                ClipData.Item position_item = e.getClipData().getItemAt(1);

                // Gets the text data from the items.
                int curr_color_index = Integer.parseInt(color_item.getText().toString());
                int prev_position = Integer.parseInt(position_item.getText().toString());

                // Displays a message containing the dragged data.
                //Toast.makeText(this, "Dragged data is " + from_pos, Toast.LENGTH_LONG);
                v.setColorFilter(Color.argb(0, 0, 0, 0));
                if (isTrashCan) {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    removeShot(prev_position);
                    decrementCountForType(curr_color_index);
                    Log.d("onDragDrop", "shot removed; position=" + prev_position);
                } else {
                    v.setImageResource(dot_layouts.get(curr_color_index));
                    v.setTag(Integer.toString(curr_color_index));
                }
                return true;

            default:
                break;
        }

        return false;
    }

    //helper functions

    public void saveToFile(int half_flag) {
        Calendar cal = Calendar.getInstance();
        String date = Integer.toString(cal.get(Calendar.YEAR))
                + Integer.toString(cal.get(Calendar.MONTH))
                + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        FileOutputStream fos = null;
        FileOutputStream fos2 = null;
        try
        {
            Context context = getApplicationContext();
            fos = context.openFileOutput(
                    "shots_half_" + half_flag + date + ".map", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(shots.get(half_flag));
            oos.close();

            String imageFilename = "screenshot_half_" + half_flag + date + ".png";
            fos2 = context.openFileOutput(imageFilename, Context.MODE_PRIVATE);
            Bitmap image = half_flag == 0? bmap_first : bmap_second;
            image.compress(Bitmap.CompressFormat.PNG, 100, fos2);

            if (half_flag == 0) bmap_first_filename = imageFilename;
            else bmap_second_filename = imageFilename;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                fos2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //capture bitmap of current view
    public void captureBitmap(int half_flag) {
        gridview.setDrawingCacheEnabled(true);
        gridview.buildDrawingCache(true);
        if (half_flag == 0) bmap_first = gridview.getDrawingCache(true);
        else bmap_second = gridview.getDrawingCache(true);
    }

    //setters for shots and their type counters
    public void addShot(int position, int color_index) {
        shots.get(half_flag).put(position, dot_layouts.get(color_index));
        Log.d("addShot", "shot added; position="+position+" color="+color_index);
    }

    public void removeShot(int position) {
        shots.get(half_flag).remove(position);
        Log.d("removeShot", "shot removed; position=" + position);
    }

    public void incrementCountForType(int color_index) {
        int curr_count = shot_type_counter.get(half_flag).get(color_index);
        shot_type_counter.get(half_flag).put(color_index, curr_count + 1);
        Log.d("incCount", "inc count; type=" + color_index + " new count=" + (curr_count+1));
    }

    public void decrementCountForType(int color_index) {
        int curr_count = shot_type_counter.get(half_flag).get(color_index);
        shot_type_counter.get(half_flag).put(color_index, curr_count - 1);
        Log.d("decCount", "dec count; type=" + color_index + " new count=" + (curr_count - 1));
    }

}
