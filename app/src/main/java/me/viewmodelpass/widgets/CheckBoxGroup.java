package me.viewmodelpass.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.core.widget.CompoundButtonCompat;


import java.util.ArrayList;
import java.util.HashSet;

import me.viewmodelpass.R;


public class CheckBoxGroup extends LinearLayout {
    private final String TAG = "CheckBoxGroup";
    private ArrayList<Integer> mCheckedIds = new ArrayList<>();
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private CheckBoxGroup.PassThroughHierarchyChangeListener mPassThroughListener;
    private CheckBoxGroup.OnCheckedChangeListener mOnCheckedChangeListener;


    public ArrayList<Integer> getCheckedCheckboxButtonIds() {
        return new ArrayList<>((new HashSet<>(mCheckedIds)));
    }

    public CheckBoxGroup(Context context) {
        super(context);
        if (!isInEditMode()){
            init(null);
        }
    }

    public CheckBoxGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()){
            init(attrs);
        }
    }

    public CheckBoxGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (getImportantForAutofill() == View.IMPORTANT_FOR_AUTOFILL_AUTO) {
                    setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_YES);
                }
            }
            init(attrs);
        }
    }

    public CheckBoxGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init(AttributeSet attrs) {

        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);

    }


    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        OnHierarchyChangeListener mOnHierarchyChangeListener;

        @Override
        public void onChildViewAdded(View parent, View child) {
            if ((parent instanceof CheckBoxGroup) && (child instanceof CheckBox)) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
                ((CheckBox) child).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener!=null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if ((parent instanceof CheckBoxGroup) && (child instanceof CheckBox)) {
                ((CheckBox) child).setOnCheckedChangeListener(null);
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }


    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();
            setCheckedId(id, isChecked);
        }
    }

    private void setCheckedId(int id, boolean isChecked) {

        if (mCheckedIds.contains(id) && !isChecked) {
            mCheckedIds.remove(Integer.valueOf(id));
        } else {
            if (id!=-1) {
                mCheckedIds.add(id);
            }
        }

        if (mOnCheckedChangeListener!=null) {
            mOnCheckedChangeListener.onCheckedChanged(this, id, isChecked);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AutofillManager  afm = getContext().getSystemService(AutofillManager.class);
            if (afm != null) {
                afm.notifyValueChanged(this);
            }
        }

    }
    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof CheckBox) {
            ((CheckBox) checkedView).setChecked(checked);
        }
    }


    public void check(int id) {
        boolean isChecked = mCheckedIds.contains(id);
        if (id != -1) {
            setCheckedStateForView(id, isChecked);
        }
        setCheckedId(id, isChecked);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (Integer mCheckedId : mCheckedIds) {
            // checks the appropriate checkbox button as requested in the XML file
            if (mCheckedId <= 0) continue;
            setCheckedStateForView(mCheckedId,true);
            setCheckedId(mCheckedId, true);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof CheckBox){
            CompoundButtonCompat.setButtonTintList((CompoundButton) child, ColorStateList.valueOf(findAccentColor()));
            if (((CheckBox) child).isChecked()) {
                setCheckedId(child.getId(), true);
            }
        }
        super.addView(child, index, params);

    }

    private int getInnerChildMargin(int dpValue){
        float d = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * d); // margin in pixels
    }
    public void clearCheck(){
        ArrayList<Integer> ids = getCheckedCheckboxButtonIds();
        for (int i = 0; i <ids.size() ; i++) {
            setCheckedStateForView(ids.get(i),false);
        }
        mCheckedIds.clear();
    }

    public int getCheckedCount(){
        Log.d(TAG," mCheckedIds:"+mCheckedIds.toString());
        return mCheckedIds.size();
    }


    @ColorInt
    private int findAccentColor() {
        TypedArray a = null;
        try {
            TypedValue typedValue = new TypedValue();
            a = getContext().obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
            return a.getColor(0, Color.BLACK); // Default BLACK
        } finally {
            if (a!=null) {
                a.recycle();
            }
        }
    }


    @Override
    protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new CheckBoxGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
//        super.setOnHierarchyChangeListener(listener);
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * <p>This set of layout parameters defaults the width and the height of
     * the children to {@link #WRAP_CONTENT} when they are not specified in the
     * XML file. Otherwise, this class ussed the value read from the XML file.</p>
     * <p/>
     * <p>See
     * <p/>
     * for a list of all child view attributes that this class supports.</p>
     */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        /**
         * <p>Fixes the child's width to
         * {@link ViewGroup.LayoutParams#WRAP_CONTENT} and the child's
         * height to  {@link ViewGroup.LayoutParams#WRAP_CONTENT}
         * when not specified in the XML file.</p>
         *
         * @param a          the styled attributes set
         * @param widthAttr  the width attribute to fetch
         * @param heightAttr the height attribute to fetch
         */
        @Override
        protected void setBaseAttributes(TypedArray a,
                                         int widthAttr, int heightAttr) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener checkedChangeListener) {
        this.mOnCheckedChangeListener = checkedChangeListener;
    }

    public interface OnCheckedChangeListener {
        /**
         * Called when the checked checkbox button has changed. When the
         * selection is cleared, checkedId is -1.
         *
         * @param group     the group in which the checked checkbox button has changed
         * @param checkedId the unique identifier of the newly checked checkbox button
         */
        void onCheckedChanged(CheckBoxGroup group, @IdRes int checkedId, boolean isChecked);
    }
}
