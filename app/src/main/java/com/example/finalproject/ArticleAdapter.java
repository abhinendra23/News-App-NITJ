package com.example.finalproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter{
    private Context mCtx;

    //we are storing all the products in a list
    private List<Article> articleList;

    //getting the context and product list with constructor
    public ArticleAdapter(Context mCtx, List<Article> articleList) {
        this.mCtx = mCtx;
        this.articleList = articleList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.article_view, parent, false);
        return new ArticleViewHolder(view);

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
        Article article1 = articleList.get(position);

        if(article1!=null) {
            ((ArticleViewHolder) holder).article_headline.setText(article1.getHeadline());
            ((ArticleViewHolder) holder).writer_name.setText(article1.getWriter());
            ((ArticleViewHolder) holder).article_image.setImageDrawable(mCtx.getResources().getDrawable(article1.getImage()));
        }

    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }



    class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView article_headline, writer_name ;
        ImageView article_image;


        public ArticleViewHolder(View itemView) {
            super(itemView);

            //textType = itemView.findViewById(R.id.textViewType);
            article_headline = itemView.findViewById(R.id.article_headline);
            writer_name = itemView.findViewById(R.id.writer_name);
            article_image = itemView.findViewById(R.id.article_image);

        }
    }
}
