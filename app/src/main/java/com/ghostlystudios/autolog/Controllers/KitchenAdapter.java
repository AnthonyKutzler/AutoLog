package com.ghostlystudios.autolog.Controllers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghostlystudios.autolog.Models.Kitchen;
import com.ghostlystudios.autolog.R;

import java.util.List;

/**
 * List<Kitchen> To RecyclerView
 */

public class KitchenAdapter extends RecyclerView.Adapter<KitchenAdapter.KitchensViewHolder> {
    private List<Kitchen> kitchens;

    class KitchensViewHolder extends RecyclerView.ViewHolder {
        TextView kitchenName, kitchenAddress;

        KitchensViewHolder(View view) {
            super(view);
            kitchenName = (TextView) view.findViewById(R.id.kitchen_name);
            kitchenAddress = (TextView) view.findViewById(R.id.kitchen_address);
        }
    }
    public KitchenAdapter(List<Kitchen> kitchens){
        this.kitchens = kitchens;
    }
    @Override
    public KitchensViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kitchen_list_row, viewGroup, false);
        return new KitchensViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KitchensViewHolder viewHolder, int position) {
        Kitchen kitchen = kitchens.get(position);
        viewHolder.kitchenName.setText(kitchen.getName());
        viewHolder.kitchenAddress.setText(kitchen.getAddress());
    }

    @Override
    public int getItemCount() {
        return kitchens.size();
    }

}
