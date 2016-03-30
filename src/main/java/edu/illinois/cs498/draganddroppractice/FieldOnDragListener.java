package edu.illinois.cs498.draganddroppractice;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

    Boolean isTrashCan;

    List<Integer> dot_layouts = GlobalVar.getInstance().getDotLayouts();

    public FieldOnDragListener(Boolean isTrashCan) {
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
                ClipData.Item item = e.getClipData().getItemAt(0);

                // Gets the text data from the item.
                int curr_color = Integer.parseInt(item.getText().toString());

                // Displays a message containing the dragged data.
                //Toast.makeText(this, "Dragged data is " + from_pos, Toast.LENGTH_LONG);
                v.setColorFilter(Color.argb(0, 0, 0, 0));
                if (isTrashCan) v.setBackgroundColor(Color.TRANSPARENT);
                else {
                    v.setImageResource((int)dot_layouts.get(curr_color));
                    v.setTag(Integer.toString(curr_color));
                }
                return true;

            default:
                break;
        }

        return false;
    }
}
