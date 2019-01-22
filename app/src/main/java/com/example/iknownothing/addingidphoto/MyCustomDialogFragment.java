package com.example.iknownothing.addingidphoto;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


public class MyCustomDialogFragment extends DialogFragment {

    Uri picUri, picUri2;
    private String imagepath = null;
    private String imagepath2 = null;
    int clicked;
    String file;
    private Uri FrontImageUri, mCropImageUri;
    private Uri RearImageUri;
    private Uri ImageUri;
    TextView front, rear;
    Button front_add, rear_add, upload;
    String size;
    long filesize;
    final int EXTERNAL_STORAGE_CODE = 100;

    public MyCustomDialogFragment() {
        //Empty Constructor is required for Dialog Fragment...

    }

    public static MyCustomDialogFragment newInstance(String title) {
        MyCustomDialogFragment fragment = new MyCustomDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_dialog, container, false);


        return v;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        front = view.findViewById(R.id.front);
        rear = view.findViewById(R.id.rear);

        front_add = view.findViewById(R.id.front_add);
        rear_add = view.findViewById(R.id.rear_add);

        front_add.setVisibility(View.GONE);
        rear_add.setVisibility(View.GONE);

        upload = view.findViewById(R.id.upload);
        getDialog().setTitle("Verify yourself!!!");


        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = 0;

                if (isReadStorageAllowed()) {

                    onSelectImageClick(getView());

                } else {

                    requestStoragePermission();


                }
            }
        });

        rear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = 1;


                if (isReadStorageAllowed()) {

                    onSelectImageClick(getView());

                } else {

                    requestStoragePermission();


                }
            }
        });


        front_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = 0;

                if (isReadStorageAllowed()) {

                    onSelectImageClick(getView());

                } else {

                    requestStoragePermission();


                }


            }
        });


        rear_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked = 1;


                if (isReadStorageAllowed()) {

                    onSelectImageClick(getView());

                } else {

                    requestStoragePermission();


                }


            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //HttpClient httpclient = new DefaultHttpClient();
                //HttpPost httppost = new HttpPost("LINK TO SERVER");
            }
        });
    }


    //Getting the on Activity Result....

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getActivity(), "called", Toast.LENGTH_SHORT).show();

        Log.d("result", "11111");


        //handle resuult of pick image chooser...
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //HERE WE GETTING CROPPED IMAGE...
            Uri resultUri = CropImage.getPickImageResultUri(getContext(), data);

            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), resultUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = resultUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(resultUri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {

                if (clicked == 0) {
                    Log.d("image", "imagepath: " + imagepath);

                    Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_SHORT).show();

                    Bitmap photo = null;
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                        Log.d("Uri", "" + result.getUri());
                        //  saveImageToExternalStorage(photo);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap scaledBitmap = scaleDown(photo, 1024, true);
                    // ((ImageView) findViewById(R.id.image)).setImageBitmap(scaledBitmap);
                    saveImageToExternalStorage(scaledBitmap);


                    if (scaledBitmap != null) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {

                            Log.d("result", "size 1 : " + scaledBitmap.getRowBytes() * photo.getHeight());
                        } else {
                            Log.d("result", "size 2 in MB: " + scaledBitmap.getByteCount() / (1024 * 1024));
                        }

                        Log.d("result", "size 3 in MB: " + BitmapCompat.getAllocationByteCount(scaledBitmap) / (1024 * 1024));


                        Log.d("result", "Absolute path is : " + imagepath);


                        //checking size of the front image.....

                        File f = new File(imagepath);
                        filesize = f.length() / 1024;
                        Log.d("result", "...............File Size in KB............. " + filesize);


                        if (filesize > 5 * 1024) {
                            Toast.makeText(getActivity(), "Image Size is more then 5 MB", Toast.LENGTH_SHORT).show();
                        } else {
                            //TO ADD UNDERLINE IN THE TEXTVIEW......
                            //SpannableString content = new SpannableString("Front_Image.jpg");
                            //content.setSpan(new UnderlineSpan(), 0, content.length(), 0);


                            //Checking from which button the photo has been added ....
                            if (clicked == 0) {
                                front.setText("Front_Image.jpg");
                                front_add.setText("EDIT");
                                front_add.setVisibility(View.VISIBLE);
                                front.setClickable(false);//disabling the textview click listener..

                                FrontImageUri = ImageUri;
                            } else if (clicked == 1) {
                                rear.setText("Back_Image.jpg");
                                rear_add.setText("EDIT");
                                rear_add.setVisibility(View.VISIBLE);
                                rear.setClickable(false);//disabling the textview click listener..

                                RearImageUri = ImageUri;
                            }

                            Toast.makeText(getActivity(), "Uploaded Successfully !!", Toast.LENGTH_SHORT).show();

                        }


                    }
                }

                if (clicked == 1) {
                    Log.d("image", "imagepath: " + imagepath);
                    Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                    Bitmap photo = null;
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                        Log.d("Uri", "" + result.getUri());
                        //  saveImageToExternalStorage(photo);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap scaledBitmap = scaleDown(photo, 1024, true);
                    // ((ImageView) findViewById(R.id.image)).setImageBitmap(scaledBitmap);
                    saveImageToExternalStorage(scaledBitmap);

                    Toast.makeText(getActivity(), "Uploaded Successfully !!", Toast.LENGTH_SHORT).show();

                    if (scaledBitmap != null) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {

                            Log.d("result", "size 1 : " + scaledBitmap.getRowBytes() * photo.getHeight());
                        } else {
                            Log.d("result", "size 2 in MB: " + scaledBitmap.getByteCount() * (1024 * 1024));
                        }

                        Log.d("result", "size 3 in MB: " + BitmapCompat.getAllocationByteCount(scaledBitmap) / (1024 * 1024));


                        //checking size of the front image.....

                        File f = new File(imagepath2);
                        filesize = f.length() / 1024;
                        Log.d("result", "...............File Size in KB............. " + filesize);


                        if (filesize > 5 * 1024) {
                            Toast.makeText(getActivity(), "Image Size is more then 5 MB", Toast.LENGTH_SHORT).show();
                        } else {
                            //TO ADD UNDERLINE IN THE TEXTVIEW......
                            SpannableString content = new SpannableString("Back_Image.jpg");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);


                            //Checking from which button the photo has been added ....
                            if (clicked == 0) {
                                front.setText(content);
                                front.setTextColor(Color.BLUE);
                                front_add.setText("EDIT");


                                FrontImageUri = ImageUri;
                            } else if (clicked == 1) {
                                rear.setText(content);
                                rear.setTextColor(Color.BLUE);
                                rear_add.setText("EDIT");

                                RearImageUri = ImageUri;
                            }

                            Toast.makeText(getActivity(), "Uploaded Successfully !!", Toast.LENGTH_SHORT).show();
                        }

                    }

                }



                /*    Uri resultUri = result.getUri();
                    // Log.d("result",resultUri.toString());
                    Log.d("result", resultUri.getPath());

                    File f = new File(resultUri.getPath());

                    file = f.getAbsolutePath();

                    //GETTING THE ACTUAL NAME OF THE FILE....
                    String filename = getFileName(resultUri);

                    Log.d("result", file + " - absolute path");
                    Log.d("result", filename + " - filename");


                    filesize = Float.valueOf(f.length() / (1024 * 1024));
                    Log.d("result", filesize + "MB - SIZE");
                    //Log.d("result",f.getUsableSpace()/(1024*1024)+"");
                    //CHECKING THE FILE SIZE, IF IT IS MORE THAN 5MB CHOOSE ANOTHER ONE...
                    float sizes = Float.valueOf(size) / (1024 * 1024);


                    if (filesize > 5.0) {
                        Toast.makeText(getActivity(), "Image Size is more then 5 MB", Toast.LENGTH_SHORT).show();
                    } else {
                        //TO ADD UNDERLINE IN THE TEXTVIEW......
                        SpannableString content = new SpannableString(filename);
                        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);


                        //Checking from which button the photo has been added ....
                        if (CHECK == 0) {
                            front.setText(content);
                            front.setTextColor(Color.BLUE);
                            front_add.setText("EDIT");


                            FrontImageUri = ImageUri;
                        } else if (CHECK == 1) {
                            rear.setText(content);
                            rear.setTextColor(Color.BLUE);
                            rear_add.setText("EDIT");

                            RearImageUri = ImageUri;
                        }


                    }*/
            }


        } else if (resultCode == Activity.RESULT_CANCELED || resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Toast.makeText(getActivity(), "Select Image Again", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Uploaded Unsuccessfull !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {

        Log.d("result", "OnRequestPermission called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d("result", "OnRequestPermission called");

        switch (requestCode) {
            case EXTERNAL_STORAGE_CODE: {
                Log.d("result", "OnRequestPermission called");

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("result", "OnRequestPermission called");

                    onSelectImageClick(getView());

                } else {
                    Log.d("result", "OnRequestPermission called");

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }

            }

            case CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE: {
                if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // required permissions granted, start crop image activity
                    startCropImageActivity(mCropImageUri);
                } else {
                    Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Getting file name and its size using CURSOR class.....
    public String getFileName(Uri uri) {
        String result = null;

        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    size = cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {

                result = result.substring(cut + 1);
                File f = new File(file + "/" + result);
                size = f.length() / (1024 * 1024) + "";
            }
        }
        return result;
    }


    private void startCropImageActivity(Uri resultUri) {
        CropImage.activity(resultUri)
                .start(getActivity());
    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(getActivity());
    }

    public Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {

        float ratio = Math.min((float) maxImageSize / realImage.getWidth(), (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Toast.makeText(getActivity(), "width- height" + width + " " + height, Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), "ratio" + ratio, Toast.LENGTH_LONG).show();

        if (ratio < 1) {

            Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                    height, filter);
            return newBitmap;
        } else {
            return realImage;
        }
    }

    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/Khaalijeb");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        if (clicked == 0) {
            String fname = "Front_Image" + ".jpg";
            File file = new File(myDir, fname);
            imagepath = file.getAbsolutePath();
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(getActivity(), new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                            picUri = uri;
                            //imagepath = path;
                        }
                    });
        } else if (clicked == 1) {

            String fname = "Back_Image" + ".jpg";
            File file = new File(myDir, fname);
            imagepath2 = file.getAbsolutePath();
            if (file.exists())
                file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(getActivity(), new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                            picUri2 = uri;
                            //imagepath2 = path;
                        }
                    });
        }

    }


    private boolean isReadStorageAllowed() {
        //Getting the permission status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

            //If permission is granted returning true
            if (result == PackageManager.PERMISSION_GRANTED)

                return true;

            //If permission is not granted returning false
            return false;
        } else {
            return true;
        }
    }

    public void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
            Log.d("scancode", "denied permission before");
        }

        Log.d("perm", "fourth");
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_CODE);
    }


}
