package com.example.lloyd.practice;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.lloyd.practice.MainActivity.imageDirUri;

/**
 * Created by lloyd on 2017/12/22.
 */

public class cursorAdaptor extends CursorAdapter {

    Cursor cursor;
    Context context_;
    Uri imgDir = imageDirUri;
    ImageView imageView;
    TextView textView;


    public cursorAdaptor(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        cursor = c;
        context_ = context;


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context_.getSystemService(LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.list_layout,null,false);
        return layoutInflater.inflate(R.layout.list_layout,null,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Uri fileUri;
        int colPath;
        int colImageId;
        String pathOnDisk;

        //get columns
        colPath = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
        colImageId = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);

        //appended direcrory uri with image id
        fileUri = Uri.withAppendedPath(imageDirUri, cursor.getString(colImageId));

        //get actual file path
        pathOnDisk = cursor.getString(colPath);

        //get view references
        textView = (TextView) view.findViewById(R.id.txtV3);
        imageView = (ImageView) view.findViewById(R.id.imgV3);

        //set textview info
        textView.setText("Image URI: " + fileUri.toString() + "\n \n Actual Path On Disk: " + pathOnDisk);

        imageView.setImageBitmap(BitmapFactory.decodeFile(pathOnDisk));

    }
}
