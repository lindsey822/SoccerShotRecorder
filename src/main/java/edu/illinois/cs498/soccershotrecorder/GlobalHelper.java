package edu.illinois.cs498.soccershotrecorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;

/**
 * Author: Lindsey Liu
 * Date: 16-04-04
 */
public class GlobalHelper {
    public static Bitmap readBitmapFile(Context context,String filename){
        if (filename.isEmpty()) return null;
        try{
            FileInputStream fis = context.openFileInput(filename);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }
}
