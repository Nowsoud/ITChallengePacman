package com.soft.cyberforest.itchallenge;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Levels {
    Context context;
    Levels(Context context){
        this.context = context;
    }

    Point[][] getLevel(int LevelResource){
        try {
            ArrayList<Point[]> level = new ArrayList<>();

            Resources res = context.getResources();
            InputStream inputStream = res.openRawResource(LevelResource);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                ArrayList<Point> tmp_ln = new ArrayList<>();
                for (char C: line.toCharArray()) {
                    tmp_ln.add(new Point(Integer.parseInt(C+"")));
                }
                level.add(tmp_ln.toArray(new Point[tmp_ln.size()]));
            }
            return level.toArray(new Point[level.size()][]);
        } catch (Exception e) {
            String s  = e.getMessage();
            return null;
        }
    }
}
