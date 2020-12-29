package com.mylibraries.colorseekbarpreference;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceDialogFragmentCompat;

public class ColorSeekBarPreferenceDialog extends PreferenceDialogFragmentCompat implements SeekBar.OnSeekBarChangeListener{

    private final ColorSeekBarPreference preference;

    private View dialogcolor;
    private SeekBar rseekbar;
    private SeekBar gseekbar;
    private SeekBar bseekbar;
    private TextView rvalue;
    private TextView gvalue;
    private TextView bvalue;

    protected int currentcolor;

    protected int increment = 1;

    public ColorSeekBarPreferenceDialog(ColorSeekBarPreference preference){
        this.preference = preference;

        final Bundle b = new Bundle();
        b.putString(ARG_KEY, preference.getKey());
        setArguments(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View onCreateDialogView(Context context) {
        View view = super.onCreateDialogView(context);

        currentcolor = preference.getCurrentcolor();

        dialogcolor = view.findViewById(R.id.preference_color);

        rseekbar = (SeekBar)view.findViewById(R.id.rseekbar);
        gseekbar = (SeekBar)view.findViewById(R.id.gseekbar);
        bseekbar = (SeekBar)view.findViewById(R.id.bseekbar);

        rvalue = (TextView)view.findViewById(R.id.rvalue);
        gvalue = (TextView)view.findViewById(R.id.gvalue);
        bvalue = (TextView)view.findViewById(R.id.bvalue);

        rseekbar.setOnSeekBarChangeListener(this);
        gseekbar.setOnSeekBarChangeListener(this);
        bseekbar.setOnSeekBarChangeListener(this);

        rseekbar.setMax(255);
        gseekbar.setMax(255);
        bseekbar.setMax(255);

        rseekbar.setProgress(Math.min(Color.red(currentcolor), 255));
        gseekbar.setProgress(Math.min(Color.green(currentcolor), 255));
        bseekbar.setProgress(Math.min(Color.blue(currentcolor), 255));

        rseekbar.setKeyProgressIncrement(increment);
        gseekbar.setKeyProgressIncrement(increment);
        bseekbar.setKeyProgressIncrement(increment);

        rvalue.setText(String.valueOf(rseekbar.getProgress()));
        gvalue.setText(String.valueOf(gseekbar.getProgress()));
        bvalue.setText(String.valueOf(bseekbar.getProgress()));

        dialogcolor.setBackgroundColor(currentcolor);

        return view;
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            int newValue = Color.rgb(rseekbar.getProgress(), gseekbar.getProgress(), bseekbar.getProgress());
            if (currentcolor != newValue) {
                preference.setCurrentcolor(newValue);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        dialogcolor.setBackgroundColor(Color.rgb(rseekbar.getProgress(), gseekbar.getProgress(), bseekbar.getProgress()));
        rvalue.setText(String.valueOf(rseekbar.getProgress()));
        gvalue.setText(String.valueOf(gseekbar.getProgress()));
        bvalue.setText(String.valueOf(bseekbar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
