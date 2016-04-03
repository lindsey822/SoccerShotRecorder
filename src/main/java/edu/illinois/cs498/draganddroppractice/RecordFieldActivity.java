package edu.illinois.cs498.draganddroppractice;

import android.content.ClipData;
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
import android.widget.GridView;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lindsey Liu on 16-03-29.
 */
public class RecordFieldActivity extends Activity implements View.OnDragListener {
    GridView gridview;

    List<Integer> dot_layouts = GlobalConst.dot_layouts;
    List<String> dot_descriptions = GlobalConst.dot_descriptions;

    //global info for current game, coming from the previous activity
    String my_team_name;
    String opp_team_name;
    //information entered by the user when recording is over
    String my_team_score;
    String opp_team_score;

    //real-time updated information
    int half_flag = GlobalConst.FIRST_HALF;
    //two maps of shots corresponding to first and second half, retrieved by index = half_flag
    HashMap<Integer, HashMap<Integer, Integer>> shots;
    //counter for num shots for each type for first and second half
    HashMap<Integer, HashMap<Integer, Integer>> shot_type_counter;

    //bitmap of field view for first half
    Bitmap bmap_first;
    //bitmap of field view for second half
    Bitmap bmap_second;
    //thumbnail of field view for first half
    Bitmap thumbnail_first;
    //thumbnail of field view for second half
    Bitmap thumbnail_second;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //retrieve known information about this game
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        my_team_name = bundle.getString("my_team_name");
        opp_team_name = bundle.getString("opp_team_name");

        shots = new HashMap<>();
        shots.put(0, new HashMap<Integer, Integer>());
        Log.d("RecordFieldActivity", "shots for first half created");
        shots.put(1, new HashMap<Integer, Integer>());//shots for second half
        Log.d("RecordFieldActivity", "shots for second half created");

        shot_type_counter = new HashMap<>();
        shot_type_counter.put(0, new HashMap<Integer, Integer>());
        shot_type_counter.put(1, new HashMap<Integer, Integer>());

        findViewById(R.id.trash_view).setOnDragListener(this);

        View.OnDragListener onDraglistener = this;
        gridview=(GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, onDraglistener));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView curr_grid = (ImageView) v;
                int color_index = Integer.parseInt((String) curr_grid.getTag());
                int next_color_index = color_index + 1;
                if (next_color_index == 5) {
                    next_color_index = 0;
                }
                curr_grid.setImageResource(dot_layouts.get(next_color_index));
                curr_grid.setTag(Integer.toString(next_color_index));
                Toast.makeText(RecordFieldActivity.this, "" + dot_descriptions.get(next_color_index),
                        Toast.LENGTH_SHORT).show();

                addShot(position, next_color_index);
                if (color_index != -1) {
                    decrementCountForType(color_index);
                    incrementCountForType(next_color_index);
                }

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

    public void onGoToSecondHalf() {
        //capture a bitmap and thumbnail of current field view for first half
        bmap_first = captureBitmap();
        thumbnail_first = getThumbnailFromBitmap(bmap_first);

        //set flag to second half
        half_flag = GlobalConst.SECOND_HALF;

        //clear all shots
        gridview.invalidateViews();
    }


    //when recording ends, trigger a pop-up dialog to let the user enter final scores
    //call this method after that dialog is completed
    public void onGoToGameSummary() {
        //capture a bitmap and thumbnail of current field view for second half
        bmap_second = captureBitmap();
        thumbnail_second = getThumbnailFromBitmap(bmap_first);

        Intent intent = new Intent(this, GameSummaryActivity.class);
        Bundle extras = new Bundle();

        extras.putString("my_team_name", my_team_name);
        extras.putString("opp_team_name", opp_team_name);
        extras.putString("my_team_score", my_team_score);
        extras.putString("opp_team_score", opp_team_score);

        extras.putSerializable("shots", shots);
        extras.putSerializable("counts", shot_type_counter);

        extras.putBoolean("from_curr_game",Boolean.TRUE);

        extras.putParcelable("field_bitmap_first", bmap_first);
        extras.putParcelable("field_bitmap_second", bmap_second);
        extras.putParcelable("field_thumbnail_first", thumbnail_first);
        extras.putParcelable("field_thumbnail_first", thumbnail_second);

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
    //capture bitmap of current view
    public Bitmap captureBitmap() {
        View curr_view = this.findViewById(android.R.id.content);
        Bitmap b = Bitmap.createBitmap( curr_view.getLayoutParams().width, curr_view.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        curr_view.layout(curr_view.getLeft(), curr_view.getTop(), curr_view.getRight(), curr_view.getBottom());
        curr_view.draw(c);
        return b;
    }

    public Bitmap getThumbnailFromBitmap(Bitmap b) {
        return ThumbnailUtils.extractThumbnail(b, 100, 100);
    }

    //setters for shots and their type counters
    public void addShot(int position, int color_index) {
        shots.get(half_flag).put(position, dot_layouts.get(color_index));
    }

    public void removeShot(int position) {
        shots.get(half_flag).remove(position);
    }

    public void incrementCountForType(int color_index) {
        int curr_count = shot_type_counter.get(half_flag).get(color_index);
        shot_type_counter.get(half_flag).put(color_index, curr_count + 1);
    }

    public void decrementCountForType(int color_index) {
        int curr_count = shot_type_counter.get(half_flag).get(color_index);
        shot_type_counter.get(half_flag).put(color_index, curr_count - 1);
    }

}
