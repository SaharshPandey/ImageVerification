package com.example.iknownothing.addingidphoto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button addPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPhotos = findViewById(R.id.add_photos);
        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });


    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MyCustomDialogFragment myCustomDialogFragment = MyCustomDialogFragment.newInstance("Verifying....");
        myCustomDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog);
        myCustomDialogFragment.show(fm, "fragment_dialog");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment_dialog");
        fragment.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(), "lkwmlasjd", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment_dialog");
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
