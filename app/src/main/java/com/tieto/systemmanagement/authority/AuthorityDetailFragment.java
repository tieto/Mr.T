package com.tieto.systemmanagement.authority;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tieto.systemmanagement.R;
import com.tieto.systemmanagement.authority.entity.AppWrapper;
import com.tieto.systemmanagement.authority.utilities.BitmapUtils;

/**
 * @author Jiang Ping
 */
public class AuthorityDetailFragment extends Fragment {

    private ImageView mImageIcon;
    private AppWrapper mAppInfo;

    public static AuthorityDetailFragment newInstance() {
        return new AuthorityDetailFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("app_info", mAppInfo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_authority_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mAppInfo = args.getParcelable("app_info");
        }
        if (savedInstanceState != null) {
            mAppInfo = savedInstanceState.getParcelable("app_info");
        }

        TextView name = (TextView) getView().findViewById(R.id.text_app_name);
        name.setText(mAppInfo.getName(getActivity()));

        mImageIcon = (ImageView) getView().findViewById(R.id.image_app_icon);
        Drawable icon = mAppInfo.loadIcon(getActivity());
        Bitmap original = BitmapUtils.convertDrawableToBitmap(icon);
        Bitmap bitmap = BitmapUtils.createReflectBitmap(original, original.getHeight() / 3);
        mImageIcon.setImageBitmap(bitmap);
    }
}
