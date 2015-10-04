package com.appspot.sandeva.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PlanetsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.planets, container, false);
        MainActivity main = MainActivity.activity;
        for (Consts.Key x : Consts.getPlanets()) {
            TableRow tr = new TableRow(container.getContext());

            TextView tv = new TextView(container.getContext());
            tv.setText(x.getName());
            tr.addView(tv);

            tv = new TextView(container.getContext());
            tv.setText(Consts.Rashi[Calculator.getVarga(main.event.values, x, 1)]);
            tr.addView(tv);

            tv = new TextView(container.getContext());
            tv.setText(format(main.event.values.get(x)));
            tr.addView(tv);

            ((TableLayout) rootView.findViewById(R.id.table)).addView(tr);
        }

        return rootView;
    }

    String format(double d) {
        return String.format("%02d°%02d'%02d\"", (int) d / 3600 % 30, (int) d / 60 % 60, (int) d % 60);
    }
}