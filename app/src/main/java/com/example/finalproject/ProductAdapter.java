package com.example.finalproject;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Product> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        //LayoutInflater inflater = LayoutInflater.from(mCtx);
        //View view = inflater.inflate(R.layout.imageview, null);
        //return new ProductViewHolder(view);
        View view;
        if(viewType==Product.TEXT_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textonly, parent, false);
            return new TextTypeViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview, parent, false);
            return new ImageTypeViewHolder(view);
        }
    }
    public int getItemViewType(int position) {

        switch (productList.get(position).getType()) {
            case 0:
                return Product.TEXT_TYPE;
            case 1:
                return Product.IMAGE_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //getting the product of the specified position
        Product product = productList.get(position);

        if(product!=null) {
            switch (product.getType()) {
                case Product.TEXT_TYPE:
                    ((TextTypeViewHolder) holder).textViewTitle.setText(product.getTitle());
                    ((TextTypeViewHolder) holder).textViewShortDesc.setText(product.getShortdesc());

                    break;
                case Product.IMAGE_TYPE:
                    ((ImageTypeViewHolder) holder).textViewTitle.setText(product.getTitle());
                    ((ImageTypeViewHolder) holder).textViewShortDesc.setText(product.getShortdesc());


                    Picasso.get().load(product.getUrl()).into(((ImageTypeViewHolder) holder).imageView);
            }
        }

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            //textType = itemView.findViewById(R.id.textViewType);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
    class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;


        public TextTypeViewHolder(View itemView) {
            super(itemView);

            //textType = itemView.findViewById(R.id.textViewType);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);

        }
    }
}
