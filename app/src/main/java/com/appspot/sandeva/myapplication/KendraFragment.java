package com.appspot.sandeva.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class KendraFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ScrollView sv = new ScrollView(container.getContext());
        LinearLayout ll = new LinearLayout(container.getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        for (int i : VargaView.VARGAS.keySet()) {
            ll.addView(new VargaView(container.getContext(), MainActivity.activity.event.values, i));
            ll.addView(new TextView(container.getContext()));
        }
        return sv;
    }
}


