package edu.illinois.cs498.draganddroppractice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lindsey Liu on 16-03-30.
 */
public class GlobalVar {

    public static final List dot_layouts = new ArrayList<Integer>()
    {{
            add(R.drawable.dot_goal);//(default)goal - blue
            add(R.drawable.dot_near_miss);//near miss - deep purple
            add(R.drawable.dot_yellow_card);//yellow card - yellow
            add(R.drawable.dot_red_card);//red card - red
            add(R.drawable.dot_penalty_kick);//penalty kick - pink
        }};

    public static final List dot_descriptions = new ArrayList<String>()
    {{
            add("GOAL");
            add("NEAR MISS");
            add("YELLOW CARD");
            add("RED CARD");
            add("PENALTY KICK");
        }};

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

    private static GlobalVar instance;

    static {
        instance = new GlobalVar();
    }

    private GlobalVar() {
    }

    public static GlobalVar getInstance() {
        return GlobalVar.instance;
    }
*/
}
