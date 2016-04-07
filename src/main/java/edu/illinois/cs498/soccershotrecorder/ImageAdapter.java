package edu.illinois.cs498.soccershotrecorder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;

/**
 * Author: Lindsey Liu
 * Date: 16-03-29
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private View.OnDragListener onDraglistener;

    public void setExistingShots(HashMap<Integer, Integer> existing_shots) {
        this.existing_shots = existing_shots;
    }

    private HashMap<Integer, Integer> existing_shots;

    List<Integer> dot_layouts = GlobalConst.dot_layouts;

    public ImageAdapter(Context c, View.OnDragListener listener,
                        HashMap<Integer, Integer> existing_shots) {
        context = c;
        this.onDraglistener = listener;
        this.existing_shots = existing_shots;
    }
    public int getCount() {
        return 171;
    }
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //get the item corresponding to your position

        ImageView curr_grid = (ImageView)LayoutInflater.from(context).inflate(R.layout.field_grid_item, parent, false);
        curr_grid.setId(position);
        curr_grid.setOnDragListener(onDraglistener);
        if (!existing_shots.isEmpty()) {
            if (existing_shots.containsKey(position)) {
                Log.d("redraw a dot",
                        "position="+position+" layout index="+existing_shots.get(position));
                curr_grid.setImageResource(existing_shots.get(position));
            }
        }

        return curr_grid;
    }
}
