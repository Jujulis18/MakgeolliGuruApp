package com.example.makgeolliguru;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
public class CustomModalBottomSheet  {

    private final View mWindow;
    private Context mContext;

    public CustomModalBottomSheet(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.modal_bottom_sheet, null);
    }

    private void randomWindowText(String info, View view){
        /*String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        if(!title.equals("")){
            tvTitle.setText(title);
        }*/

        /*TextView informationArea = (TextView) view.findViewById(R.id.makgeolliInformation);
        if(!info.equals("")){
            informationArea.setText(info);
        }*/
    }



}

