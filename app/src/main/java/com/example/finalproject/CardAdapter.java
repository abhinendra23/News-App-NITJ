package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter {
    private Context mCtx;

    //we are storing all the products in a list
    private List<Cards> cardsList;

    //getting the context and product list with constructor
    public CardAdapter(Context mCtx, List<Cards> cardsList) {
        this.mCtx = mCtx;
        this.cardsList = cardsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.place_card, null);
        return new TextTypeViewHolder(view);

    }
    /*public int getItemViewType(int position) {

        switch (productList.get(position).getType()) {
            case 0:
                return Product.TEXT_TYPE;
            case 1:
                return Product.IMAGE_TYPE;
            default:
                return -1;
        }
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //getting the product of the specified position
        Cards card = cardsList.get(position);

        if(card!=null) {
                    ((TextTypeViewHolder) holder).textViewCompany.setText(card.getCompany());
                    ((TextTypeViewHolder) holder).textViewPackage.setText(card.getSalary());
                    ((TextTypeViewHolder) holder).textViewDescription.setText(card.getDesc());
        }

    }


    @Override
    public int getItemCount() {
        return cardsList.size();
    }



    class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCompany, textViewPackage, textViewDescription;


        public TextTypeViewHolder(View itemView) {
            super(itemView);

            //textType = itemView.findViewById(R.id.textViewType);
            textViewCompany = itemView.findViewById(R.id.textViewCompany);
            textViewPackage = itemView.findViewById(R.id.textViewPackage);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
