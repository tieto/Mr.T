package com.tieto.systemmanagement.processmanage.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import android.util.Log;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

import com.tieto.systemmanagement.R;

/**
 * TODO: document your custom view class.
 */
public class SlidingChartView extends View {

    protected static final String SlidingChartView_TAG="SlidingChartView";

    private class ChartValPoint {
        public float mX;
        public float mY;

        public ChartValPoint(float x, float y){
            mX=x;
            mY=y;
        }
    }

    private int mXRange=0;
    private float mXUnit=0;
    private int mYRange=0;
    private float mYUnit=0;
    private int mBorderColor=Color.BLACK;
    private int mCoordinateLineColor=0;
    private int mValueStrokeColor=Color.BLUE;
    private int mValueFillColor=Color.CYAN;
    private boolean mIsDataInitialized=false;

    private float mTextWidth;
    private float mTextHeight;

    private float mStartTime=0.0f;
    private float previousTime=0.0f;
    private float mGridLineOffset=0.0f;

    private ChartValPoint mEndPoint=new ChartValPoint(0,0);

    private List<ChartValPoint> mPoints=new ArrayList<ChartValPoint>();

    public SlidingChartView(Context context) {
        super(context);
        init(null, 0);
    }

    public SlidingChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SlidingChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SlidingChartView, defStyle, 0);

        mXRange = a.getInteger(R.styleable.SlidingChartView_xRange, 10);
        mXUnit = a.getFloat(R.styleable.SlidingChartView_xUnit, 1.0f);

        mYRange = a.getInteger(R.styleable.SlidingChartView_yRange, 10);
        mYUnit = a.getFloat(R.styleable.SlidingChartView_yRange, 1.0f);

        mBorderColor=a.getColor(R.styleable.SlidingChartView_borderColor, Color.BLACK);
        mCoordinateLineColor=a.getColor(R.styleable.SlidingChartView_coordinateLineColor, Color.BLACK);
        mValueStrokeColor=a.getColor(R.styleable.SlidingChartView_valueStrokeColor, Color.BLUE);
        mValueFillColor=a.getColor(R.styleable.SlidingChartView_valueFillColor, Color.CYAN);

        a.recycle();
    }

    private void invalidateAttr() {

    }

    public void append(float value) {
        if(!mIsDataInitialized) {
            mIsDataInitialized=true;
            mStartTime=(float)(System.currentTimeMillis()/1000.0f);
            previousTime=0;
            mEndPoint.mY=0;
            mEndPoint.mX=mXUnit;
            mGridLineOffset=0;
        }
        updatePoints(value);

        invalidate();
    }

    public void clear() {
        mIsDataInitialized=false;
        mPoints.clear();
    }

    private void updatePoints(float value) {
        float currentTime=(float)(System.currentTimeMillis()/1000.0f);
        float timespan=currentTime-previousTime;

        timespan=1.5f;
        List<ChartValPoint> toRemove=new ArrayList<ChartValPoint>();

        mGridLineOffset=(timespan+mGridLineOffset)%mXUnit;

        for (ChartValPoint pt: mPoints) {
            pt.mX+=timespan;
            if(pt.mX>=mXRange) {
                toRemove.add(pt);
            }
        }

        if(toRemove.size()>0) {
            for (ChartValPoint pt: toRemove) {
                mPoints.remove(pt);
            }

            mEndPoint=toRemove.get(toRemove.size()-1);
        }

        mEndPoint.mX+=timespan;
        mPoints.add(new ChartValPoint(0, value));
        previousTime=currentTime;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        drawChartLine(p,canvas,paddingLeft,paddingTop,contentWidth,contentHeight);
        drawValues(p,canvas,paddingLeft,paddingTop,contentWidth,contentHeight);
        drawOverlay(p,canvas,paddingLeft,paddingTop,contentWidth,contentHeight);
    }

    private void drawChartLine(Paint p, Canvas canvas,int left,int top, int width,int height) {

        p.setColor(mCoordinateLineColor);
        p.setStyle(Paint.Style.FILL);
        //l,t,r,b
        float x;
        float l;
        float r;
        float t;
        float b;
        float scaleX=width/mXRange;
        float lineWidth=2.0f;
        float lineWidthDiv2=lineWidth/2;
        int xCount=(int)Math.floor((double)mXRange/(double)mXUnit);
        int yCount=(int)Math.floor((double)mYRange/(double)mYUnit);
        float xSpace=width/xCount;
        float ySpace=height/yCount;

        float offset=mGridLineOffset*scaleX;

        for(int i=1;i<xCount;i++) {
            l=xSpace*i-lineWidthDiv2+left+offset;
            r=l+lineWidth;
            canvas.drawRect(l,top,r,height,p);
        }

        for(int i=1;i<yCount;i++) {
            t=ySpace*i-lineWidthDiv2+top;
            b=t+lineWidth;
            canvas.drawRect(left,t,width,b,p);
        }
    }

    private void drawValues(Paint p, Canvas canvas,int left,int top, int width,int height) {

        float scaleX=width/mXRange;
        float scaleY=height/mYRange;

        if(mPoints.size()>0) {

            Path path = new Path();
            ChartValPoint pt=mEndPoint;

            float x=width - pt.mX*scaleX + left;
            float y=height -pt.mY*scaleY + top;

            path.moveTo(x, y);
            for (int i=0;i<mPoints.size();i++) {
                pt=mPoints.get(i);

                x=width - pt.mX*scaleX + left;
                y=height -pt.mY*scaleY + top;

                path.lineTo(x, y);
            }

            //draw closeshape
            //1
            pt=mPoints.get(mPoints.size()-1);
            x=width - pt.mX*scaleX + left;
            y=height+top;
            path.lineTo(x, y);

            //2
            pt=mEndPoint;
            x=width - pt.mX*scaleX + left;
            y=height+top;
            path.lineTo(x, y);

            //3
            x=width - pt.mX*scaleX + left;
            y=height -pt.mY*scaleY + top;
            path.lineTo(x, y);

            path.close();
            p.setStrokeWidth(5);
            p.setPathEffect(null);

            p.setColor(mValueFillColor);
            p.setXfermode(new PorterDuffXfermode(Mode.ADD));
            p.setStyle(Paint.Style.FILL);
            p.setAlpha(96);
            canvas.drawPath(path, p);
            p.setAlpha(255);
            p.setXfermode(null);

            p.setColor(mValueStrokeColor);
            p.setStyle(Paint.Style.STROKE);

            canvas.drawPath(path, p);

            Log.d(SlidingChartView_TAG,"mEndPoint.x="+mEndPoint.mX+", mEndPoint.y="+mEndPoint.mY);
        }
    }

    private void drawOverlay(Paint p, Canvas canvas,int left,int top, int width,int height) {
        p.setStrokeWidth(5);
        p.setColor(mBorderColor);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, left+width, top+height, p);
    }

    public int getXRange() {
        return mXRange;
    }

    public void setXRange(int xRange) {
        mXRange = xRange;
        invalidateAttr();
    }

    public float getXUnit() {
        return mYUnit;
    }

    public void setXUnit(int xUnit) {
        mXUnit = xUnit;
        invalidateAttr();
    }


    public int getYRange() {
        return mYRange;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param yRange The YResolution attribute value to use.
     */
    public void setYCount(int yRange) {
        mYRange = yRange;
        invalidateAttr();
    }

    public float getYUnit() {
        return mYUnit;
    }

    public void setYUnit(int yUnit) {
        mYUnit = yUnit;
        invalidateAttr();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        invalidateAttr();
    }

    public int getCoordinateLineColor() {
        return mCoordinateLineColor;
    }

    public void setCoordinateLineColor(int coordinateLineColor) {
        mCoordinateLineColor = coordinateLineColor;
        invalidateAttr();
    }

    public int getValueStrokeColor() {
        return mValueStrokeColor;
    }

    public void setValueStrokeColor(int valueStrokeColor) {
        mValueStrokeColor = valueStrokeColor;
        invalidateAttr();
    }

    public int getValueFillColor() {
        return mValueFillColor;
    }

    public void setValueBackgroundColor(int valueFillColor) {
        mValueFillColor = valueFillColor;
        invalidateAttr();
    }
}
