package ness.com.etw.common.customView;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


public class CustomView {

    public static Toast showToast(Context context, String message) {

        return Toast.makeText(context, message, Toast.LENGTH_LONG);
    }

    public static ProgressBar showProgressBar(Context context) {
        return new ProgressBar(context);
    }

    public static void drawRoundedCorners(Context context, int statusColor, View view) {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(5);
        gradientDrawable.setColor(statusColor);
        gradientDrawable.setStroke(2, ContextCompat.getColor(context, android.R.color.transparent));

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //noinspection deprecation
            view.setBackgroundDrawable(gradientDrawable);
        } else {
            view.setBackground(gradientDrawable);
        }
    }
}
