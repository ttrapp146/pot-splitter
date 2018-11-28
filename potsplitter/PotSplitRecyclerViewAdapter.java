package com.ttrapp14622.potsplitter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

class PotSplitRecyclerViewAdapter extends RecyclerView.Adapter<PotSplitRecyclerViewAdapter.PotSplitRecyclerViewHolder> {

    //creates a list of saved splits
    private Context mCtx;
    private List<Product> productList;

    public PotSplitRecyclerViewAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public PotSplitRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_list_item, null);
        return new PotSplitRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PotSplitRecyclerViewHolder potSplitRecyclerViewHolder, int i) {

        final Product product = productList.get(i);
        final int index = i;
        Resources resources = mCtx.getResources();
        potSplitRecyclerViewHolder.text.setText(product.getSplit());
        potSplitRecyclerViewHolder.button.setText(resources.getString(R.string.delete_button));
        potSplitRecyclerViewHolder.button.setOnClickListener(new View.OnClickListener() {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     removeItem(index);
                                                                 }
                                                             }

        );

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class PotSplitRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        Button button;


        public PotSplitRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.potSplitListItem);
            button = itemView.findViewById(R.id.potSplitDeleteButton);
        }
    }

    private void removeItem(int position) {

        //remove split if split is not last entry.  will ot save changes if save button not clicked!  save button setup in ListEditActivity
        Resources resources = mCtx.getResources();
        if (productList.size() == 1)
            Toast.makeText(mCtx, resources.getString(R.string.cannot_delete_last_split), Toast.LENGTH_SHORT).show();
        else {
            productList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, 1);
        }

    }
}
