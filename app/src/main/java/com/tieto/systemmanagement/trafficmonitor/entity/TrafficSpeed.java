package com.tieto.systemmanagement.trafficmonitor.entity;

import android.net.TrafficStats;
import android.os.Handler;

import java.util.ArrayList;


/**
 * Created by jianhpin on 15-4-1. <br/>
 * Calculate the speed by average during a period <br/>
 * Be sure to use the class within main thread
 */
public final class TrafficSpeed {

    private int mUid;
    private long mLastRxBytes = 0;
    private long mLastTxBytes = 0;
    private long mUpdateTime = 1000;

    //for mobile netspeed info
    private long mSpeed;
    private int mTotalBytes;
    private int mHadReadBytes;

    private boolean mIsShutdown = false;
    private static final ArrayList<TrafficSpeed> INSTANCES = new ArrayList<TrafficSpeed>();

    private OnSpeedUpdatedListener mListener;
    private static Handler mInternalHandler = new Handler();

    public TrafficSpeed() {
        if (INSTANCES.size() > 50) {
            throw new IllegalStateException("Over 50 instance are created!");
        }
        INSTANCES.add(this);
    }

    public static void shutdownAll() {
        for (TrafficSpeed ts : INSTANCES) {
            ts.shutdown();
        }
        INSTANCES.clear();
    }

    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mListener != null) {
                mListener.onSpeedUpdated(calcSpeeds());
            }
            if (!mIsShutdown) {
                mInternalHandler.postDelayed(this, mUpdateTime);
            }
        }
    };

    public void setUid(int uid) {
        mUid = uid;
        mLastRxBytes = 0;
        mLastTxBytes = 0;
    }

    /**
     * @param timeMillis Update per time, suggest larger than 1000 millisecond
     * @param listener The listener to get the update event
     */
    public void registerUpdate(long timeMillis, OnSpeedUpdatedListener listener) {
        shutdown();
        mIsShutdown = false;
        mUpdateTime = timeMillis;
        mListener = listener;
        if (mListener != null && timeMillis > 0 && mUid != 0) {
            mInternalHandler.postDelayed(mUpdateRunnable, mUpdateTime);
        }
    }

    public void shutdown() {
        mIsShutdown = true;
        mInternalHandler.removeCallbacks(mUpdateRunnable);
    }

    private Speeds calcSpeeds() {
        long rxSpeed = 0;
        long txSpeed = 0;
        if (mLastRxBytes != 0) {
            rxSpeed = (TrafficStats.getUidRxBytes(mUid) - mLastRxBytes) / mUpdateTime;
            rxSpeed *= 1000;
        }
        if (mLastTxBytes != 0) {
            txSpeed = (TrafficStats.getUidRxBytes(mUid) - mLastTxBytes) / mUpdateTime;
            txSpeed *= 1000;
        }

        mLastRxBytes = TrafficStats.getUidRxBytes(mUid);
        mLastTxBytes = TrafficStats.getUidTxBytes(mUid);
        return new Speeds(rxSpeed, txSpeed);
    };

    public static interface OnSpeedUpdatedListener {
        public void onSpeedUpdated(Speeds speeds);
    }

    /** A class to hold the Rx and Tx speeds */
    public static final class Speeds {
        /** The Rx Speed */
        private long mRxSpeed;
        /** The Tx Speed */
        private long mTxSpeed;

        /** 0 speed for both Rx and Tx */
        public static final Speeds EMPTY = new Speeds(0, 0);

        public Speeds(long rx, long tx) {
            mRxSpeed = rx;
            mTxSpeed = tx;
        }

        public long getRxSpeed() {
            return  mRxSpeed;
        }

        public String getRxSpeedReadable() {
            long speed = getRxSpeed();
            return getReadableString(speed);
        }

        public long getTxSpeed() {
            return mTxSpeed;
        }

        public String getTxSpeedReadable() {
            long speed = getTxSpeed();
            return getReadableString(speed);
        }

        public String getReadableString(long byteSize) {
            if (byteSize < 1024) {
                return String.format("%dB/s", byteSize);
            } else if (byteSize < 1024 * 1024) {
                return String.format("%dKB/s", Math.round(byteSize / 1024.0f));
            } else {
                return String.format("%.1fMB/s", byteSize / (float)(1024 * 1024));
            }
        }
    }

    public int getmUid() {
        return mUid;
    }

    public void setmUid(int mUid) {
        this.mUid = mUid;
    }

    public int getmTotalBytes() {
        return mTotalBytes;
    }

    public void setmTotalBytes(int mTotalBytes) {
        this.mTotalBytes = mTotalBytes;
    }

    public long getmSpeed() {
        return mSpeed;
    }

    public void setmSpeed(long mSpeed) {
        this.mSpeed = mSpeed;
    }

    public int getmHadReadBytes() {
        return mHadReadBytes;
    }

    public void setmHadReadBytes(int mHadReadBytes) {
        this.mHadReadBytes = mHadReadBytes;
    }
}
