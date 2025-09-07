package com.cobeffectiveness.type;

import java.util.List;

public final class ChartJson {
    public String name;
    public double default_ = 1.0;
    public Tiles tiles;
    public List<String> order;
    public List<Rule> rules;

    public static final class Tiles {
        public int cols;
        public int rows;
        public int tile_w;
        public int tile_h;
    }

    public static final class Rule {
        public String attack;
        public String defend;
        public double mult;
    }
}
