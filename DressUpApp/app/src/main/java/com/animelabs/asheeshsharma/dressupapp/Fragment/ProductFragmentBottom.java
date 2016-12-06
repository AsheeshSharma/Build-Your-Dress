package com.animelabs.asheeshsharma.dressupapp.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.animelabs.asheeshsharma.dressupapp.Activities.MainActivity;
import com.animelabs.asheeshsharma.dressupapp.Data.DatabaseHandler;
import com.animelabs.asheeshsharma.dressupapp.Model.Product;
import com.animelabs.asheeshsharma.dressupapp.R;
import com.animelabs.asheeshsharma.dressupapp.Utility.FileChooser;
import com.animelabs.asheeshsharma.dressupapp.Utility.PictureUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Asheesh.Sharma on 04-12-2016.
 */
public class ProductFragmentBottom extends Fragment {
    private Product product;
    private final static String object = "PRODUCT_ITEM";
    ImageButton addBottomBtn;
    public static int REQUEST_IMAGE_FILE = 1;
    public String mSavedProfileImage;
    public String tempLocation;
    public FileChooser fileChooser;
    ImageView imageView;
    DatabaseHandler db;
    String path;
    public ProductFragmentBottom(){}
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileChooser=new FileChooser(getActivity());
    }

    public static ProductFragmentBottom newInstance(Product product){
        ProductFragmentBottom productFragment = new  ProductFragmentBottom();
        Bundle args = new Bundle();
        args.putParcelable(object, product);
        productFragment.setArguments(args);
        return productFragment;
    }

    private void readBundleArgs(Bundle bundle){
        if(bundle!=null){
            Product p  =bundle.getParcelable(object);
            if(p!=null &&  p.getPathToImage()!=null && !p.getPathToImage().isEmpty())
                path = p.getPathToImage();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bottom, container, false);
        addBottomBtn = (ImageButton)rootView.findViewById(R.id.addBottomButton);
        imageView = (ImageView)rootView.findViewById(R.id.imageView);
        readBundleArgs(getArguments());
        if(path!=null){
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
            imageView.setBackgroundDrawable(ob);
        }
        addBottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemGenerousClick();
            }
        });
        return rootView;
    }

    private void addItemGenerousClick(){
        db = new DatabaseHandler(getActivity());
        final CharSequence[] items_without_grouppic = {"Choose from Library","Capture", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items_without_grouppic, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items_without_grouppic[item].equals("Choose from Library")) {
                    //instead of sending the result directly, we send the user to AskToSend activity
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    photoPickerIntent.setType("image/jpg");
                    photoPickerIntent.putExtra("crop", "true");

                    photoPickerIntent.putExtra("outputX", 300);
                    photoPickerIntent.putExtra("outputY", 300);
                    photoPickerIntent.putExtra("aspectX", 1);
                    photoPickerIntent.putExtra("aspectY", 1);

                    photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
                    photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                    startActivityForResult(photoPickerIntent, REQUEST_IMAGE_FILE);
                } else if (items_without_grouppic[item].equals("Capture")) {
                    Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFileUri());
                    photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                    startActivityForResult(photoPickerIntent, 20);
                } else if(items_without_grouppic[item].equals("Cancel")){
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }
    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Eckovation");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Failed", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        mSavedProfileImage = mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg";
        tempLocation = mSavedProfileImage;

        return mediaFile;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_FILE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            int currentapiVersion = fileChooser.getVersion();
            mSavedProfileImage = fileChooser.getPath(getActivity(), selectedImageUri,currentapiVersion);
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inJustDecodeBounds = true;
            //done to get the width and height of the source image
            BitmapFactory.decodeFile(mSavedProfileImage, bitmapOptions);

            bitmapOptions.inSampleSize = PictureUtils.calculateInSampleSize(bitmapOptions, 300,300);
            bitmapOptions.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(mSavedProfileImage, bitmapOptions);
            bitmap = bitmap.createScaledBitmap(bitmap, 300, 300, true);
            try {
                File file = new File(tempLocation);
                file.createNewFile();
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.flush();
                ostream.close();
                /*bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                imageView.setBackgroundDrawable(ob);*/
                if(db!=null){
                    Product p = new Product(file.getAbsolutePath(),"","","","","t");
                    db.addProduct(p);
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                }else{
                    Log.d("Log","NUll");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (resultCode != Activity.RESULT_CANCELED && requestCode == 20) {
            Log.d("Cameraasdasd","Request");
            try {
                File file = new File(tempLocation);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                bitmap = bitmap.createScaledBitmap(bitmap, 300, 300, true);
                FileOutputStream ostream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                ostream.flush();
                ostream.close();
                /*bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                imageView.setBackgroundDrawable(ob);*/
                if(db!=null){
                    Product p = new Product(file.getAbsolutePath(),"","","","","t");
                    db.addProduct(p);
                    Intent intent = new Intent(getActivity(),MainActivity.class);
                    startActivity(intent);
                }else{
                    Log.d("Log","NUll");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
