package com.mylibraries.colorseekbarpreference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceViewHolder;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by marko on 4/9/2017.
 */

public class ColorSeekBarPreference extends DialogPreference{

    protected final SharedPreferences preferences;

    protected LayoutInflater li;

    private View colorsample;

    protected int currentcolor;

    protected int defaultcolor;

    public ColorSeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorSeekbarPreference, 0, 0);

        try {
            defaultcolor = a.getInteger(R.styleable.ColorSeekbarPreference_defaultcolor, ContextCompat.getColor(getContext(), R.color.colorDefault));
        }finally {
            a.recycle();
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        li = (LayoutInflater)context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        setDialogLayoutResource(R.layout.color_seekbar_preference_dialog);
        setWidgetLayoutResource(R.layout.color_seekbar_preference_widget);

        currentcolor = preferences.getInt(getKey(), defaultcolor);
    }

    private void storeValue()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(getKey(), currentcolor);
        editor.commit();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder)
    {
        super.onBindViewHolder(holder);

        colorsample = holder.findViewById(R.id.color_sample);

        if(isEnabled()){
            setsamplecolor(currentcolor);
        }else {
            setsamplecolor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
        }
    }

    private void setsamplecolor(int color)
    {
        Drawable background = colorsample.getBackground();
        background.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
    }

    @Override
    public void onDependencyChanged (Preference dependency, boolean disableDependent){
        super.onDependencyChanged(dependency, disableDependent);

        if(colorsample != null){
            if(disableDependent){
                setsamplecolor(ContextCompat.getColor(getContext(), R.color.colorDisabled));
            }else {
                setsamplecolor(currentcolor);
            }
            colorsample.invalidate();
        }
    }

    public void setCurrentcolor(int color){
        currentcolor = color;
        setsamplecolor(currentcolor);
        colorsample.invalidate();
        storeValue();
    }

    public int getCurrentcolor(){
        return currentcolor;
    }
}

