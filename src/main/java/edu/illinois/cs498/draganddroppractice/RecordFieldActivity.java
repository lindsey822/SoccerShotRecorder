package edu.illinois.cs498.draganddroppractice;

import android.content.ClipData;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
public class RecordFieldActivity extends Activity{
    GridView gridview;

    List<Integer> dot_layouts = GlobalVar.getInstance().getDotLayouts();
    List<String> dot_descriptions = GlobalVar.getInstance().getDotDescriptions();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        findViewById(R.id.trash_view).setOnDragListener(new FieldOnDragListener(Boolean.TRUE));

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                ImageView curr_grid = (ImageView) v;
                int color_count = Integer.parseInt((String) curr_grid.getTag());
                int next_color_count = color_count + 1;
                if (next_color_count == 5) {
                    next_color_count = 0;
                }
                curr_grid.setImageResource(dot_layouts.get(next_color_count));
                curr_grid.setTag(Integer.toString(next_color_count));
                Toast.makeText(RecordFieldActivity.this, "" + dot_descriptions.get(next_color_count),
                        Toast.LENGTH_SHORT).show();


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
                ClipData dragData = ClipData.newPlainText("", (String) curr_grid.getTag());
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(dragData, shadowBuilder, v, 0);

                Toast.makeText(RecordFieldActivity.this, "START DRAGGING",
                        Toast.LENGTH_SHORT).show();
                //set the image as wallpaper
                return true;
            }
        });
    }

}
