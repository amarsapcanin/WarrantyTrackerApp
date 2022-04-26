package com.warranty.tracker.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.warranty.tracker.R;
import com.warranty.tracker.activities.HomePageActivity;
import com.warranty.tracker.sqlite.DBHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddWarrantyFragment extends Fragment {

    private ImageView imvCalenderAddWarranty;
    private EditText edtDateAddWarranty, edtWarrantyName;
    private ImageButton imvUploadPicture;
    private Button btnAdd;
    private DBHandler dbHandler;
    Uri selectedImageUri;
    String imagePath = "";

    final Calendar myCalendar = Calendar.getInstance();

    int SELECT_PICTURE = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_warranty, container, false);

        edtWarrantyName = view.findViewById(R.id.edtWarrantyName);
        edtDateAddWarranty = view.findViewById(R.id.edtDateAddWarranty);
        imvCalenderAddWarranty = view.findViewById(R.id.imvCalenderAddWarranty);
        imvUploadPicture = view.findViewById(R.id.imvUploadPicture);
        btnAdd = view.findViewById(R.id.btnAdd);
        dbHandler = new DBHandler(getActivity());
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        edtDateAddWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        imvCalenderAddWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        imvUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String warrantyName = edtWarrantyName.getText().toString();
                String warrantyDate = edtDateAddWarranty.getText().toString();

                if (warrantyName.isEmpty() && warrantyDate.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbHandler.addNewWarranty(warrantyName, warrantyDate,imagePath);
                Toast.makeText(getActivity(), "Warranty has been added.", Toast.LENGTH_SHORT).show();
                ((HomePageActivity)getActivity()).onBackPressed();
            }
        });

        ImageView ivBack = view.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomePageActivity)getActivity()).onBackPressed();
            }
        });
        return view;
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat);
        edtDateAddWarranty.setText(dateFormat.format(myCalendar.getTime()));
    }

    void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);

        /*Intent i = new Intent(Intent.ACTION_PICK);
        i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_PICTURE);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);*/
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                imvUploadPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imvUploadPicture.setImageURI(data.getData());
                selectedImageUri = data.getData();
                //imagePath = getPathFromUri(getActivity(), data.getData());

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagePath = saveInCatch(bitmap);
            }
        }
    }

    private String saveInCatch(Bitmap bitmap) {
        File sd = getActivity().getCacheDir();
        File folder = new File(sd, "/myfolder/");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                folder.mkdirs();
            }
        }

        File fileName = new File(folder,System.currentTimeMillis()+".jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName.getAbsolutePath();
    }

    private String saveImage(Bitmap resizedbitmap) {
        ContextWrapper cw = new ContextWrapper(getActivity());
        File directory = cw.getDir("profile", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File mypath = new File(directory, System.currentTimeMillis()+".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            resizedbitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE", e.getMessage(), e);
        }
        return mypath.getAbsolutePath();
    }


    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
