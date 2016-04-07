package edu.illinois.cs498.soccershotrecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lindsey Liu
 * Date: 16-03-30
 */
public class GlobalConst {

    public static final List dot_layouts = new ArrayList<Integer>()
    {{
            //ordered roughly by frequency
            add(R.drawable.miss);
            add(R.drawable.whistle);//penalty kick
            add(R.drawable.goal);//
            add(R.drawable.yellow_card);
            add(R.drawable.red_card);
        }};

    public static final List dot_descriptions = new ArrayList<String>()
    {{
            add("MISS");
            add("PENALTY KICK");
            add("GOAL");
            add("YELLOW CARD");
            add("RED CARD");
        }};

    public static final int num_types = 5;

    public static final int FIRST_HALF = 0;
    public static final int SECOND_HALF = 1;
    /*
    public List getDotLayouts() {
        return dot_layouts;
    }

    public List getDotDescriptions() {
        return dot_descriptions;
    }

    public String getDotDescription(int index) {
        return (String) dot_descriptions.get(index);
    }

    public int getDotLayout(int index) {
        return (int) dot_layouts.get(index);
    }

    private static GlobalConst instance;

    static {
        instance = new GlobalConst();
    }

    private GlobalConst() {
    }

    public static GlobalConst getInstance() {
        return GlobalConst.instance;
    }
*/
}
