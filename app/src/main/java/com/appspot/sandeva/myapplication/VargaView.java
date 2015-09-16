package com.appspot.sandeva.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VargaView extends GridLayout {
    int W = 170;

    public static final Map<Integer, String> VARGAS = getVargas();
    private static Map<Integer, String> getVargas() {
        Map<Integer, String> VARGAS = new TreeMap<Integer, String>();
        VARGAS.put(1, "\n\nලග්නම්");
        VARGAS.put(2, "\nරාශ්\u200Dයාර්ධම්");
        VARGAS.put(3, "\n\nදෙර්කාණම්");

        VARGAS.put(4, "\nචතුර්ථාං-\nශකම්");
        VARGAS.put(5, "\nපඤ්චාං-\nශකම්");
        VARGAS.put(6, "\nෂෂ්ඨාං-\nශකම්");

        VARGAS.put(7, "\nසප්තාං-\nශකම්");
        VARGAS.put(8, "\nඅෂ්ඨාං-\nශකම්");
        VARGAS.put(9, "\n\nනවාංශකම්");

        VARGAS.put(10, "\n\nදශාංශකම්");
        VARGAS.put(12, "\nද්වාදශාං-\nශකම්");
        VARGAS.put(16, "\nෂෝඩශාං-\nශකම්");

        VARGAS.put(20, "\n\nවිංශාංශකම්");
        VARGAS.put(24, "\nචතුර්-\nවිංශාංශකම්");
        VARGAS.put(27, "\nසප්ත-\nවිංශාංශකම්");

        VARGAS.put(30, "\nත්\u200Dරිං-\nශාංශකම්");
        VARGAS.put(40, "\nචත්වාරිං-\nශාංශකම්");
        VARGAS.put(45, "\nපඤ්ච-\nචත්වාරිං-\nශාංශකම්");

        VARGAS.put(60, "\nෂෂ්ඨ්\u200Dයාං-\nශකම්");
        return VARGAS;
    }

    public VargaView(Context context, Map<Consts.Key, Double> kendra, int varga) {
        super(context);

        List<List<String>> houses = new ArrayList<List<String>>();
        for (int i = 0; i <= 12; i++) houses.add(new ArrayList<String>());
        int I = Calculator.getVarga(kendra, Consts.Key.I, varga);
        houses.get(0).add(Consts.Rashi[I] + VARGAS.get(varga));
        for (Consts.Key x : Consts.getPlanets()) {
            houses.get(1 + (Calculator.getVarga(kendra, x, varga) - I + 12) % 12).add(x.getShortName());
        }
        for (int i = 0; i <= 12; i++) if(houses.get(i).size()==0) houses.get(i).add(((Integer)i).toString());



        setBackgroundColor(Color.BLACK);
        int[][][] map = new int[][][]{{{3, 2}, {1}, {12, 11}}, {{4}, {0}, {10}}, {{5, 6}, {7}, {8, 9}}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(spec(i), spec(j));
                lp.height = lp.width = W;
                lp.setMargins(1, 1, 1, 1);
                boolean split = i != 1 && j != 1;
                boolean forward = i == j;
                for (int k = 0; k < map[i][j].length; k++)
                    addView(new SplitView(getContext(), split, forward, k == 0, houses.get(map[i][j][k])), lp);
            }
        }
    }

    public class SplitView extends LinearLayout {
        boolean forward, split, left;
        int r_height;

        public SplitView(Context context, boolean split, boolean forward, boolean left, List<String> lst) {
            super(context);
            setOrientation(VERTICAL);
            this.forward = forward;
            this.split = split;
            this.left = left;
            if (left) setBackgroundColor(Color.WHITE);
            int n = lst.size();
            int cols = (int) Math.ceil(split ? Math.sqrt(2 * n + 0.25) + 0.5 : Math.sqrt(n));
            int rows = split ? cols : 1 + (n - 1) / cols;
            r_height = 150 / rows;
            for (int r = rows - 1; r >= 0; r--) {
                TableRow tr = getTableRow(rows);
                int w = split ? r * W / cols : W;
                int cls = Math.min(split ? r : cols, n);
                if (!left) addText("", tr, W - w);
                for (int c = 0; c < cls; c++, n--) {
                    addText(lst.get(lst.size()-n), tr, w / cls);
                }
            }
        }

        private TableRow getTableRow(int rows) {
            TableLayout t = new TableLayout(getContext());
            TableRow tr = new TableRow(getContext());
            tr.setMinimumHeight(W / rows);
            t.addView(tr);
            if (forward == left) addView(t, 0);
            else addView(t);
            return tr;
        }

        void addText(String text, TableRow tr, int width) {
            TextView tv = new TextView(getContext());
            tv.setText(text);
            tv.setMinWidth(width);
            tv.setMinHeight(r_height);
            tv.setGravity(Gravity.CENTER);
            tr.addView(tv);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!split) return;
            int x = getWidth();
            int y = getHeight();
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            canvas.drawLine(0, forward ? 0 : y, x, forward ? y : 0, paint);
        }
    }

}
