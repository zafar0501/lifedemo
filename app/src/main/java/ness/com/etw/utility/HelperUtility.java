package ness.com.etw.utility;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ness.com.etw.R;
import ness.com.etw.common.constant.StringConstant;
import ness.com.etw.network.NetworkClient;
import ness.com.etw.notes.views.fragments.NotesListFragment;


public class HelperUtility {


    private static final String TAG = HelperUtility.class.getSimpleName();
    private static final String IMAGE_DIR = "ETW_Images";
    private static File directory;
    private static String profileImageFilePath;


    public static Boolean InternetCheck(Context _context) {
        ConnectivityManager cn = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        return nf != null && nf.isConnected();
    }


    public static void ReplaceFragmentWithoutBackStack(FragmentManager fragmentManager, int containerId, Fragment fragment, String Tag) {
        fragmentManager.beginTransaction().replace(containerId, fragment, Tag).commit();


    }

    public static Date convertStringToDate(String strDate) {

        Date date = null;
        String modDate;
        try {

            if (!strDate.contains("T")) {
                modDate = strDate.split(" ")[0];
            } else {

                modDate = strDate.substring(0, strDate.indexOf("T"));
            }
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            date = format.parse(modDate);

        } catch (Exception ex) {
            Log.d(TAG, "convertStringToDate: " + ex);
        }
        return date;
    }


    public static String formattedCreatedDate(String strDate) {

        int day, month;

        Date date = convertStringToDate(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);


        return String.format(Locale.US, "%1$s %2$s", getMonthString(month), day + getDayNumberSuffix(day));
    }


    public static String formattedFullCreatedDate(String strDate) {

        int day, month;

        Date date = convertStringToDate(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);

        return String.format(Locale.US, "%1$s %2$s", getFullMonthString(month), day + getDayNumberSuffix(day));
    }


    private static String formattedCreatedTime(String strDate) {

        String createdTime = null;
        DateFormat parseDateTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss a", Locale.getDefault());

        try {

            Date createdTimeFormat = parseDateTime.parse(strDate);
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            createdTime = timeFormat.format(createdTimeFormat);
            Log.d(TAG, "formattedCreatedTime: " + createdTime);

        } catch (Exception ex) {
            Log.d(TAG, "formattedCreatedTime: ");
        }

        return createdTime;

    }

    private static String formattedCreatedDateTime(String strDate) {

        String displayCreatedDateTime;

        displayCreatedDateTime = String.format(Locale.US, "%1$s at %2$s",
                formattedCreatedDate(strDate), formattedCreatedTime(strDate));
        return displayCreatedDateTime;
    }

    public static String formattedCreatedUTCDateTime(String strDate) {

        String displayCreatedDateTime = null;
        DateFormat parseDateTime = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
        parseDateTime.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {

            Date createdUTCFormat = parseDateTime.parse(strDate);
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());

            String utcDateTime = String.valueOf(utcFormat.format(createdUTCFormat));
            displayCreatedDateTime = formattedCreatedDateTime(utcDateTime);


        } catch (Exception ex) {
            Log.d(TAG, "formattedCreatedTime: ");
        }

        return displayCreatedDateTime;
    }

    public static String formatHyphenToBackSlashDate(String strDate) {

        String strBackslashDate = null;
        try {

            DateFormat parseHypenDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date hypenDateFormat = parseHypenDate.parse(strDate);

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            strBackslashDate = formatter.format(hypenDateFormat);

        } catch (Exception e) {

            Log.d(TAG, "formatHyphenToBackSlashDate: " + e);
        }


        return strBackslashDate;
    }

    private static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private static String getFullMonthString(int month) {

        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return null;
    }

    private static String getMonthString(int month) {

        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
        }
        return null;
    }


    public static void loadProfileImage(final Context context, final String authorId, final ImageView imageProfileView) {

        /*
        1. Check the image on local storage
        2. If Image found, display the image
        3. If image not found, hit the API, load the image, save the image
        */

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        directory = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE);

        final String fileName = authorId + "_profile.png";
        if (isImageOnDevice(fileName)) {

            Glide.with(context)
                    .load(profileImageFilePath)
                    .apply(RequestOptions.placeholderOf(R.drawable.default_profile_pic))
                    .into(imageProfileView);

        } else {

            if (HelperUtility.InternetCheck(context)) {
                String url = NetworkClient.BASE_URL + "avatar/v1/avatars/users/" + authorId + "/SM3X";
                Log.d(TAG, " " + url);
                String cookieAuth = StringConstant.ICommon.AUTHORIZATION + PreferenceUtility.getAuthToken();
                LazyHeaders.Builder builder = new LazyHeaders.Builder().addHeader("Cookie", cookieAuth);

                GlideUrl glideUrl = new GlideUrl(url, builder.build());
                Glide.with(context)
                        .load(glideUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.default_profile_pic))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {

                                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                if (bitmap != null) {
                                    saveToInternalStorage(bitmap, fileName);
                                }
                                imageProfileView.setImageBitmap(bitmap);
                            }
                        });
            } else {

                Glide.with(context)
                        .load(null)
                        .apply(RequestOptions.placeholderOf(R.drawable.default_profile_pic))
                        .into(imageProfileView);
            }
        }

    }


    private static File saveToInternalStorage(Bitmap bitmapImage, String fileName) {


        File mypath = new File(directory, fileName);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return directory.getAbsolutePath();
        return mypath;
    }

    private static boolean isImageOnDevice(String filename) {

        File imageFilePath[] = directory.listFiles();
        for (File profileImage : imageFilePath) {

            if (profileImage.getName().equals(filename)) {
                profileImageFilePath = profileImage.getPath();
                return true;
            }

        }
        return false;
    }

    public static void setToolbarTitle(Activity activity, String title) {
        Toolbar toolbar = activity.findViewById(R.id.tbl_home_toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(title);
    }

    public static void hideKeyBoard(Context context, EditText editText) {

        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static String displayCamelCaseWords(String word) {

        String[] wordSplit;
        StringBuilder wordCapital = new StringBuilder();
        wordSplit = word.split(" ");
        for (int i = 0; i < wordSplit.length; i++) {
            String splitWord = wordSplit[i];
            wordCapital.append(splitWord.substring(0, 1).toUpperCase()).append(splitWord.substring(1).toLowerCase()).append(" ");
        }
        return wordCapital.toString();
    }

    /*  public static void ReplaceFragment(FragmentManager fragmentManager, int containerId, Fragment fragment, String Tag) {

          fragmentManager.beginTransaction().replace(containerId, fragment, Tag).addToBackStack(Tag).commit();
      }

      public static void AddFragment(FragmentManager fragmentManager, int containerId, Fragment fragment, String Tag) {

          fragmentManager.beginTransaction().add(containerId, fragment, Tag).addToBackStack(Tag).commit();
      }

      public static void AddFragmentWithoutBackStack(FragmentManager fragmentManager, int containerId, Fragment fragment, String Tag) {

          fragmentManager.beginTransaction().add(containerId, fragment, Tag).commit();
      }

      public static void popFragmentFromBackStackByName(FragmentManager fragmentManager, String fragmentTagName) {
        fragmentManager.popBackStack(fragmentTagName, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
  */
}
