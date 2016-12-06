package com.animelabs.asheeshsharma.dressupapp.Utility;

import android.graphics.BitmapFactory;

/**
 * Created by Asheesh.Sharma on 05-12-2016.
 */
public class PictureUtils {

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
            //there is some sort of confusion here, the documentation suggests
            //that it should really be, half-heights, but i think its wrong!
            final int halfHeight = height;
            final int halfWidth = width;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}

