package edu.illinois.cs498.draganddroppractice;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lindsey Liu
 * Date: 16-03-30
 */
public class GlobalConst {

    public static final List dot_layouts = new ArrayList<Integer>()
    {{
            add(R.drawable.dot_goal);//(default)goal - blue
            add(R.drawable.dot_miss);//near miss - deep purple
            add(R.drawable.dot_yellow_card);//yellow card - yellow
            add(R.drawable.dot_red_card);//red card - red
            add(R.drawable.dot_penalty_kick);//penalty kick - pink
        }};

    public static final List dot_descriptions = new ArrayList<String>()
    {{
            add("GOAL");
            add("MISS");
            add("YELLOW CARD");
            add("RED CARD");
            add("PENALTY KICK");
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
