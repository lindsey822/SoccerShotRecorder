package edu.illinois.cs498.draganddroppractice;

import android.content.ClipData;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import java.util.Map;

/**
 * Created by Lindsey Liu on 16-03-29.
 */
public class RecordFieldActivity extends Activity implements View.OnDragListener {
    GridView gridview;

    List<Integer> dot_layouts = GlobalVar.dot_layouts;
    List<String> dot_descriptions = GlobalVar.dot_descriptions;

    int half_flag = GlobalVar.FIRST_HALF;
    //two maps of shots corresponding to first and second half, retrieved by index = half_flag
    List<Map<Integer, Integer>> shots;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        shots = new ArrayList<Map<Integer, Integer>>();
        shots.add(0, new HashMap<Integer, Integer>());
        Log.d("RecordFieldActivity", "shots for first half created");
        shots.add(1, new HashMap<Integer, Integer>());//shots for second half
        Log.d("RecordFieldActivity", "shots for second half created");

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
                int curr_color = Integer.parseInt(color_item.getText().toString());
                int prev_position = Integer.parseInt(position_item.getText().toString());

                // Displays a message containing the dragged data.
                //Toast.makeText(this, "Dragged data is " + from_pos, Toast.LENGTH_LONG);
                v.setColorFilter(Color.argb(0, 0, 0, 0));
                if (isTrashCan) {
                    v.setBackgroundColor(Color.TRANSPARENT);
                    removeShot(prev_position);
                    Log.d("onDragDrop", "shot removed; position=" + prev_position);
                } else {
                    v.setImageResource(dot_layouts.get(curr_color));
                    v.setTag(Integer.toString(curr_color));
                }
                return true;

            default:
                break;
        }

        return false;
    }

    //setters for shots
    public void addShot (int position, int color_index) {
        shots.get(half_flag).put(position, dot_layouts.get(color_index));
    }

    public void removeShot (int position) {
        shots.get(half_flag).remove(position);
    }

}
