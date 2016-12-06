package com.animelabs.asheeshsharma.dressupapp.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.animelabs.asheeshsharma.dressupapp.Adapter.BottomViewPagerAdapter;
import com.animelabs.asheeshsharma.dressupapp.Adapter.TopViewPagerAdapter;
import com.animelabs.asheeshsharma.dressupapp.Data.DatabaseHandler;
import com.animelabs.asheeshsharma.dressupapp.Model.Combination;
import com.animelabs.asheeshsharma.dressupapp.Model.Product;
import com.animelabs.asheeshsharma.dressupapp.R;
import com.animelabs.asheeshsharma.dressupapp.Utility.MarshPermission;
import com.animelabs.asheeshsharma.dressupapp.Utility.MyReceiver;
import com.animelabs.asheeshsharma.dressupapp.Utility.NotifyService;
import com.animelabs.asheeshsharma.dressupapp.Utility.PictureUtils;
import com.animelabs.asheeshsharma.dressupapp.Utility.SharedPref;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ViewPager topViewPager;
    ViewPager bottomViewPager;
    ImageButton refreshBtn;
    ImageButton addToColBtn;
    private ArrayList<Product> mTopProducts;
    private ArrayList<Product> mBottomProducts;
    public static final String TYPE_SHIRT = "s";
    public static final String TYPE_TROUSER = "t";
    DatabaseHandler db;
    ProgressDialog pd;
    int[] sampleTopImagesList = new int[]{R.drawable.shirt_1, R.drawable.shirt_2, R.drawable.shirt_3, R.drawable.shirt_4, R.drawable.shirt_5};
    int[] sampleBottomImagesList = new int[]{R.drawable.j_1, R.drawable.j_2, R.drawable.j_3, R.drawable.j_4,R.drawable.j_5};
    Bitmap bm;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MarshPermission marshPermission = new MarshPermission(MainActivity.this);
        if(!marshPermission.isStoragePermissionGranted()){
            Toast.makeText(getApplicationContext(),"Please accept the permissions",Toast.LENGTH_SHORT).show();
        }
        String startedFrom = getIntent().getStringExtra(NotifyService.STARTED_VIA);
        topViewPager = (ViewPager)findViewById(R.id.viewpager_top);
        bottomViewPager = (ViewPager)findViewById(R.id.viewpager_bottom);
        refreshBtn = (ImageButton)findViewById(R.id.refreshButton);
        addToColBtn = (ImageButton)findViewById(R.id.addToCollection);

         AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this,MyReceiver.class); // AlarmReceiver1 = broadcast receiver

        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 06);
        alarmStartTime.set(Calendar.MINUTE, 00);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        db = new DatabaseHandler(this);
        if(SharedPref.getAddedDrawables(getApplicationContext())!=1){
            saveImagesToDB(sampleTopImagesList,TYPE_SHIRT);
            saveImagesToDB(sampleBottomImagesList,TYPE_TROUSER);
        }
        mTopProducts = new ArrayList<Product>();
        mBottomProducts = new ArrayList<Product>();
        mTopProducts = getProductListFromDB(TYPE_SHIRT);
        mBottomProducts = getProductListFromDB(TYPE_TROUSER);
        int t = mBottomProducts.size() *  TopViewPagerAdapter.LOOPS_COUNT/2 ;
        int s = mTopProducts.size() *  TopViewPagerAdapter.LOOPS_COUNT/2;

        if(startedFrom!=null && startedFrom.equals("1")){
            t = generateRandomNumber(mTopProducts.size() - 1);
            s = generateRandomNumber(mBottomProducts.size() - 1);
        }

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tx = generateRandomNumber(mBottomProducts.size() -1 );
                int sx = generateRandomNumber(mTopProducts.size() - 1);
                refreshLayoutViewPagers(topViewPager, bottomViewPager, tx, sx);
            }
        });

        addToColBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = topViewPager.getCurrentItem();
                int positionb = bottomViewPager.getCurrentItem();
                Combination c = new Combination();
                c.setShirtPath(Integer.toString(position));
                c.setTrouserPath(Integer.toString(positionb));
                db.addCombination(c);
                Log.d("Hi", db.getCombinationCount() + "");
            }
        });

        if(topViewPager!=null && bottomViewPager!=null)
            setViewPagers(topViewPager, bottomViewPager, t, s);
        else
            Log.d("Error", "VP is NULL");
    }

    private void refreshLayoutViewPagers(ViewPager topViewPager, ViewPager bottomViewPager, int tx, int sx){
        topViewPager.setCurrentItem(sx,true);
        bottomViewPager.setCurrentItem(tx, true);
    }
    private void setViewPagers(ViewPager topViewPager, ViewPager bottomViewPager, int t, int s){
        TopViewPagerAdapter topViewPagerAdapter = new TopViewPagerAdapter(getSupportFragmentManager(), mTopProducts);
        BottomViewPagerAdapter bottomViewPagerAdapter = new BottomViewPagerAdapter(getSupportFragmentManager(), mBottomProducts);
        topViewPager.setAdapter(topViewPagerAdapter);
        bottomViewPager.setAdapter(bottomViewPagerAdapter);
        topViewPager.setCurrentItem(s, false);
        bottomViewPager.setCurrentItem(t, false);
    }

    private ArrayList<Product> getProductListFromDB(String type){
        return (ArrayList<Product>)db.getAllProducts(type);
    }
    private void saveImagesToDB(int arr[], String type){
        pd = new ProgressDialog(MainActivity.this,R.style.AppCompatAlertDialogStyle);
        pd.setMessage("Setting Up");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        for(int i = 0; i < arr.length;i++){
            if(bm!=null){
                bm.recycle();
                bm = null;
            }
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            bitmapOptions.inSampleSize = PictureUtils.calculateInSampleSize(bitmapOptions, 300,300);
            bitmapOptions.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeResource( getResources(),arr[i], bitmapOptions);
            /*bm=Bitmap.createBitmap(bm, 0,0,300, 300);*/
            int dimension = getSquareCropDimensionForBitmap(bm);
            bm = ThumbnailUtils.extractThumbnail(bm, dimension, dimension);
            bm = Bitmap.createScaledBitmap(bm, 300, 300, true);
            String name = "product_" + type + i + ".jpg";
            String filePath = getFilePathOfBitmap(bm, name);
            if(filePath!=null && !filePath.isEmpty()){
                Product p = new Product(filePath, "","","","",type);
                db.addProduct(p);
                SharedPref.setAddedDrawbles(getApplicationContext());
            }
        }
        try {
            if ((this.pd != null) && this.pd.isShowing()) {
                this.pd.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
            e.printStackTrace();
        } catch (final Exception e) {
            // Handle or log or ignore
            e.printStackTrace();
        } finally {
            this.pd = null;
        }
    }
    String getFilePathOfBitmap(Bitmap bm, String name){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Eckovationx");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Failed", "failed to create directory");
                return null;
            }
        }
        File file = new File(mediaStorageDir, name);
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getSquareCropDimensionForBitmap(Bitmap bitmap) {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.getWidth(), bitmap.getHeight());
    }

    private int generateRandomNumber(int size){
        int x;
        Random r = new Random();
        x = r.nextInt(size - 0) + size;
        return x;
    }
}
