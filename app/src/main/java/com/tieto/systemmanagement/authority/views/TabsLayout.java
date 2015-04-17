package com.tieto.systemmanagement.authority.views;

import com.tieto.systemmanagement.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author Jiang Ping
 */
public class TabsLayout extends LinearLayout {
	
	private RadioGroup mRadioGroup = null;
	private OnTabChangedListener mListener = null;

	// For tabs strip
	private float mStripPosition = 0;
	private int mStripHeight = 10;
    private int mStripMargin = 10;
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public TabsLayout(Context context) {
		super(context);
		initialize(context);
	}

	public TabsLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context, attrs);
	}

    @TargetApi(11)
	public TabsLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context, attrs);
	}
	
	private void initialize(Context con, AttributeSet attrs) {
		initialize(con);
	}

	private void initialize(Context con) {
		setWillNotDraw(false);
		mStripHeight = getPixelForDip(3);
        mStripMargin = getPixelForDip(10);

		mPaint.setColor(0xff2f8f58);
		setOrientation(VERTICAL);
		mRadioGroup = new RadioGroup(con);
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (mListener != null) {
					mListener.onSegmentChanged(TabsLayout.this, checkedId);
				}
			}
		});
		mRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mRadioGroup, lp);
	}

    private int getPixelForDip(float dip) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    protected final int indexOfChild(int id) {
		int count = mRadioGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			RadioButton button = (RadioButton) mRadioGroup.getChildAt(i);
			if (button.getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	public void setSegments(String[] segments) {
        mRadioGroup.removeAllViews();
		if (segments != null && segments.length > 0) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			for (int i = 0; i < segments.length; i++) {
				String seg = segments[i];
				RadioButton button = (RadioButton) inflater.inflate(R.layout.authority_tab_button, null);
				if (i == 0) {
					button.setChecked(true);
				}
				button.setId(i);
				button.setText(seg);
				RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(0,
                        LayoutParams.MATCH_PARENT);
				lp.weight = 1.0f;
				mRadioGroup.addView(button, lp);
			}
		}
	}
	
	public void setSelectedIndex(int index) {
		RadioButton btn = (RadioButton) mRadioGroup.getChildAt(index);
		if (btn != null) {
			btn.setChecked(true);
		}
	}

    public int getSelectedIndex() {
        int count = mRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton btn = (RadioButton) mRadioGroup.getChildAt(i);
            if (btn.isChecked()) {
                return i;
            }
        }
        return 0;
    }
	
	public ViewPager.OnPageChangeListener getOnPageChangeListener() {
        return mPagerListener;
    }
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		int stripWidth = 0;
		int stripsCount = mRadioGroup.getChildCount();
		if (stripsCount > 0) {
			stripWidth = Math.round((float)viewWidth / stripsCount) - 2 * mStripMargin;
		}
		int left = Math.round(viewWidth * mStripPosition) + mStripMargin;
		canvas.drawRect(left, viewHeight - mStripHeight, left + stripWidth, viewHeight, mPaint);
	}
	
	protected void updateStripPosition(float position) {
		mStripPosition = position;
		invalidate();
	}

	private ViewPager.OnPageChangeListener mPagerListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			setSelectedIndex(position);
		}
		
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (mRadioGroup.getChildCount() > 0)
				updateStripPosition((position + positionOffset) / mRadioGroup.getChildCount());
			else
				updateStripPosition(0);
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) { /* Nothing */ }
	};
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}
	
	public void setOnTabChangedListener(OnTabChangedListener l) {
		mListener = l;
	}
	
	public interface OnTabChangedListener {
		/**
		 * @param view The segment view instance.
		 * @param selectedIndex The current selected index, -1 for error.
		 */
		public void onSegmentChanged(TabsLayout view, int selectedIndex);
	}
}
