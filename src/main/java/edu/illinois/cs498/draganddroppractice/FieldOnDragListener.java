package edu.illinois.cs498.draganddroppractice;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lindsey Liu on 16-03-29.
 */
public class FieldOnDragListener implements View.OnDragListener {

    private static Activity mActivity;

    Boolean isTrashCan;

    List<Integer> dot_layouts = GlobalVar.dot_layouts;

    public FieldOnDragListener(Boolean isTrashCan) {
        this.isTrashCan = isTrashCan;
    }

    public FieldOnDragListener(Activity activity, Boolean isTrashCan) {
        mActivity = activity;
        this.isTrashCan = isTrashCan;
    }

    @Override
    public boolean onDrag(View view, DragEvent e) {
        final int action = e.getAction();
        ImageView v = (ImageView) view;
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
                    Log.d("onDragDrop", "shot for position " + prev_position + " removed");
                }
                if (!isTrashCan) {
                    v.setImageResource(dot_layouts.get(curr_color));
                    v.setTag(Integer.toString(curr_color));
                }
                return true;

            default:
                break;
        }

        return false;
    }
}
