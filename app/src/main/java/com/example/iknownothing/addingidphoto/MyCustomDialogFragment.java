package com.example.iknownothing.addingidphoto;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;


public class MyCustomDialogFragment extends DialogFragment {


    String file;
    private Uri FrontImageUri,mCropImageUri;
    private Uri RearImageUri;
    private Uri ImageUri;
    TextView front, rear;
    Button front_add, rear_add, upload;
    final int GalleryPic = 1;
    final int CameraPic = 2;
    int CHECK = 0;
    String size;
    int SOURCE;

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

    private void startCropImageActivity(Uri resultUri) {
        CropImage.activity()
                .start(getActivity());
    }

    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        front = view.findViewById(R.id.front);
        rear = view.findViewById(R.id.rear);

        front_add = view.findViewById(R.id.front_add);
        rear_add = view.findViewById(R.id.rear_add);

        upload = view.findViewById(R.id.upload);

        getDialog().setTitle("Verify yourself!!!");


        front_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHECK = 0;

                onSelectImageClick(view);

             /*   CropImage.activity(ImageUri)
                        .setAspectRatio(2, 3)
                        .setCropShape(CropImageView.CropShape.RECTANGLE).start(getActivity());*/

               /* Intent intent = CropImage.activity(ImageUri)
                        .getIntent(getActivity());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);*/
                //ShowDialogBox showDialogbox = new ShowDialogBox(getContext());
                //showDialogbox.show();
                //OpenGallery();


            }
        });


        rear_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHECK = 1;

                startCropImageActivity(ImageUri);
                /*CropImage.activity(ImageUri)
                        .setAspectRatio(2, 3)
                        .setCropShape(CropImageView.CropShape.RECTANGLE).start(getActivity());

                */
                /*Intent intent = CropImage.activity(ImageUri)
                        .getIntent(getActivity());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
               */



                //ShowDialogBox showDialogbox = new ShowDialogBox(getContext());
                //showDialogbox.show();


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


    //Intent to Gallery...
    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPic); //User will pick the Picture..
    }

    private void OpenCamera() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CameraPic);


    }
    //Getting the on Activity Result....

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if((requestCode == GalleryPic ||  requestCode == CameraPic) && resultCode == Activity.RESULT_OK && data!= null)
        {

            ImageUri = data.getData();


            Log.d("result",size+"");

            //GETTING THE ACTUAL NAME OF THE FILE....
            String filename = getFileName(ImageUri);
            //Log.d("result",ImageUri.toString());
            //Log.d("result",ImageUri.getPath());

            //CHECKING THE FILE SIZE, IF IT IS MORE THAN 5MB CHOOSE ANOTHER ONE...
            float size = Float.valueOf(this.size)/(1024*1024);

            Log.d("result",filename);
            Log.d("result",size+"");

            if(size > 5.0)
            {
                Toast.makeText(getActivity(),"Image Size is more then 5 MB",Toast.LENGTH_SHORT).show();
            }

            else {
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

                //When the user Select the image he will be redirected to the Image Cropping Activity...

                CropImage.activity(ImageUri)
                        .setAspectRatio(2, 3)
                        .setCropShape(CropImageView.CropShape.RECTANGLE).start(getActivity());

            }
        }
        else{

        }
        */

       Toast.makeText(getActivity(),"called",Toast.LENGTH_SHORT).show();

        Log.d("result","11111");


        //THIS CHECKS WHETHER WE SELECT THE CROP OPTION...
        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //HERE WE GETTING CROPPED IMAGE...
            //CropImage.ActivityResult result = CropImage.getActivityResult(data);


                Uri resultUri = CropImage.getPickImageResultUri(getContext(),data);
            if (CropImage.isReadExternalStoragePermissionsRequired(getContext(), resultUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = resultUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already granted, can start crop image activity
                startCropImageActivity(resultUri);
            }


               // Log.d("result",resultUri.toString());
                Log.d("result",resultUri.getPath());

                File f =new File(resultUri.getPath());

                file = f.getAbsolutePath();

                //GETTING THE ACTUAL NAME OF THE FILE....
                String filename = getFileName(resultUri);
                //Log.d("result",ImageUri.toString());
                Log.d("result",filename);


                Log.d("result",f.length()/(1024*1024)+"");
                //Log.d("result",f.getUsableSpace()/(1024*1024)+"");
                //CHECKING THE FILE SIZE, IF IT IS MORE THAN 5MB CHOOSE ANOTHER ONE...
                float sizes = Float.valueOf(size)/(1024*1024);

                Log.d("result",filename);
                Log.d("result",size+"");

                if(sizes > 5.0)
                {
                    Toast.makeText(getActivity(),"Image Size is more then 5 MB",Toast.LENGTH_SHORT).show();
                }

                else {
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


                }

            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(),"Select Image Again",Toast.LENGTH_SHORT).show();
            }





    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
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
                File f = new File(file+"/"+result);
                size = f.length() / (1024*1024)+"";
            }
        }
        return result;
    }



    //Showing Dialog Box......
    public class ShowDialogBox extends Dialog{

        Button camera,gallery;
        public ShowDialogBox(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.showdialog);

            camera = findViewById(R.id.camera);

            gallery = findViewById(R.id.gallery);


            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SOURCE=0;
                    OpenCamera();
                    dismiss();


                }
            });

            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SOURCE=1;
                    OpenGallery();
                    dismiss();

                }
            });

        }
    }
}