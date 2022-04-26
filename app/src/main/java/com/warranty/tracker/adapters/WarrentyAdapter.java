package com.warranty.tracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.warranty.tracker.R;
import com.warranty.tracker.activities.HomePageActivity;
import com.warranty.tracker.fragments.MyWarrantiesFragment;
import com.warranty.tracker.fragments.WarrantyItemInfoFragment;
import com.warranty.tracker.modal.OnWarrantyClickListener;
import com.warranty.tracker.modal.WarrantyItem;

import java.io.File;
import java.util.ArrayList;

public class WarrentyAdapter extends RecyclerView.Adapter<WarrentyAdapter.ViewHolder> {
    private Context mContext;
    MyWarrantiesFragment listener;
    ArrayList<WarrantyItem> warrantyItems;
    OnWarrantyClickListener onWarrantyClickListener;

    public WarrentyAdapter(Context mContext, ArrayList<WarrantyItem> warrantyItems) {
        this.mContext = mContext;
        this.warrantyItems = warrantyItems;
    }

    public void setListener(OnWarrantyClickListener onWarrantyClickListener) {
        this.onWarrantyClickListener = onWarrantyClickListener;
    }

    @NonNull
    @Override
    public WarrentyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.warranty_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WarrentyAdapter.ViewHolder holder, int position) {
            WarrantyItem warrantyItem = warrantyItems.get(position);
            holder.tvWarrantyItem.setText(warrantyItem.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onWarrantyClickListener.onWarrantClick(warrantyItem);
                }
            });
            holder.ivWarranty.setImageURI(getImageUri(warrantyItem.getImagePath()));
    }

    @Override
    public int getItemCount() {
        return warrantyItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvWarrantyItem;
        private ImageView ivWarranty;
        MyWarrantiesFragment myWarrantiesFragment = new MyWarrantiesFragment();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivWarranty = itemView.findViewById(R.id.ivWarranty);
            tvWarrantyItem = itemView.findViewById(R.id.tvWarrantyItem);
        }
    }


    private Uri getImageUri(String  fileDir) {
        File newFile = new File(fileDir);
        return FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", newFile);
    }
}
