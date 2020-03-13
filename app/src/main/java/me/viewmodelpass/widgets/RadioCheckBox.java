package me.viewmodelpass.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class RadioCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {



    public RadioCheckBox(Context context) {
        this(context,null);
    }

    public RadioCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.radioButtonStyle);
    }

    public RadioCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
