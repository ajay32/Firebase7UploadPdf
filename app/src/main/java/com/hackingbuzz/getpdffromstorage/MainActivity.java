package com.hackingbuzz.getpdffromstorage;


import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    // Pdf is uploading from storage..not from google drive or any other place.. coz its taking pdf from storage path .... other could be content://

    private StorageReference mStorageRef;
    Button uploadFile;
    ProgressDialog dialog;
    public static int Request_Number = 1;



    Button pdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdf = (Button) findViewById(R.id.getPdf);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading Pdf. Please wait...");

        PDF();
    }


    public void PDF() {

        pdf.setOnClickListener(//Listens for a button click.
                new View.OnClickListener() {//Creates a new click listener.
                    @Override
                    public void onClick(View v) {//does what ever code is in here when the button is clicked

                        Intent intent = new Intent();
                        intent.setType("application/pdf");   // its gonna make all the pdf visisble n rest hide..so that gonna make it easy for you to select pdf...
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select a PDF "), 1);

                    }
                }
        );
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //PDF
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Uri uri = data.getData();
              //  String SelectedPDF = getPDFPath(selectedUri_PDF);

                StorageReference filePath = mStorageRef.child("Pdfs").child(uri.getLastPathSegment());

                dialog.show(); // showing dialoge at the time of putting image


                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {  // you can add more than one interface to each other ...where you put semicolon to close means method1(listener).methodw2(listener).method3(listener);
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();

                        Toast.makeText(getApplicationContext(), "Pdf Uploaded Sucessfully",Toast.LENGTH_SHORT ).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Pdf Uploading Failed",Toast.LENGTH_SHORT ).show();

                    }
                });

            }
        }
    }

/*    public String getPDFPath(Uri uri) {

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/


}
