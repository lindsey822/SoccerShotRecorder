package edu.illinois.cs498.draganddroppractice;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Lindsey Liu on 16-03-29.
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;

    public ImageAdapter(Context c) {
        context = c;
    }
    public int getCount() {
        return 209;
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

        ImageView curr_grid = (ImageView)(convertView == null
                ? LayoutInflater.from(context).inflate(R.layout.grid, parent, false)
                : convertView);
        curr_grid.setId(position);
        curr_grid.setOnDragListener(new FieldOnDragListener(Boolean.FALSE));
        return curr_grid;
    }
}
