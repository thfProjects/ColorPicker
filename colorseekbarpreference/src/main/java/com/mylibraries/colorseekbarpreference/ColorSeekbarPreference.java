package com.mylibraries.colorseekbarpreference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by marko on 4/9/2017.
 */

public class ColorSeekbarPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener{

    protected final SharedPreferences preferences;

    protected LayoutInflater li;

    private View colorsample;
    private View dialogcolor;
    private SeekBar rseekbar;
    private SeekBar gseekbar;
    private SeekBar bseekbar;

    private TextView title;


    protected int currentcolor;

    protected int defaultcolor;

    protected int increment = 1;

    public ColorSeekbarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorSeekbarPreference, 0, 0);

        try {
            defaultcolor = a.getInteger(R.styleable.ColorSeekbarPreference_defaultcolor, Color.BLACK);
        }finally {
            a.recycle();
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        li = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        setDialogLayoutResource(R.layout.color_seekbar_preference_dialog);
        setWidgetLayoutResource(R.layout.color_seekbar_preference);
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            int newValue = Color.rgb(rseekbar.getProgress(), gseekbar.getProgress(), bseekbar.getProgress());
            if (currentcolor != newValue) {
                currentcolor = newValue;
                storeValue();
            }
        }
    }

    private void storeValue()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(getKey(), currentcolor);
        editor.commit();
        setsamplecolor(preferences.getInt(getKey(), defaultcolor));
        colorsample.invalidate();
    }

    @Override
    protected View onCreateView(ViewGroup parent )
    {
        View view = super.onCreateView(parent);

        colorsample = view.findViewById(R.id.color_sample);
        title = (TextView) view.findViewById(android.R.id.title);
        if(isEnabled()){
            setsamplecolor(preferences.getInt(getKey(), defaultcolor));
        }else {
            setsamplecolor(ContextCompat.getColor(getContext(), android.R.color.secondary_text_dark));
        }

        return view;
    }

    private void setsamplecolor(int color)
    {
        Drawable background = colorsample.getBackground();
        background.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    @Override
    public View onCreateDialogView(){
        View view = super.onCreateDialogView();

        currentcolor = preferences.getInt(getKey(), defaultcolor);

        dialogcolor = view.findViewById(R.id.preference_color);

        rseekbar = (SeekBar)view.findViewById(R.id.rseekbar);
        gseekbar = (SeekBar)view.findViewById(R.id.gseekbar);
        bseekbar = (SeekBar)view.findViewById(R.id.bseekbar);

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

        dialogcolor.setBackgroundColor(currentcolor);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        dialogcolor.setBackgroundColor(Color.rgb(rseekbar.getProgress(), gseekbar.getProgress(), bseekbar.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDependencyChanged (Preference dependency, boolean disableDependent){
        super.onDependencyChanged(dependency, disableDependent);
        if(colorsample != null && title != null){
            if(disableDependent){
                setsamplecolor(ContextCompat.getColor(getContext(), android.R.color.secondary_text_dark));
                colorsample.invalidate();
            }else {
                setsamplecolor(preferences.getInt(getKey(), defaultcolor));
                colorsample.invalidate();
            }
        }
    }
}

