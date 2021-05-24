package com.t3h.basemvvm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.internal.$Gson$Preconditions;
import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.databinding.ItemAudiobookBinding;
import com.t3h.basemvvm.ui.category.CategoryDetailFragment;
import com.t3h.basemvvm.ui.category.CategoryDetailFragmentDirections;
import com.t3h.basemvvm.ui.category.CategoryFragmentDirections;
import com.t3h.basemvvm.ui.home.HomeFragmentDirections;
import com.t3h.basemvvm.ui.trending.TrendingFragment;
import com.t3h.basemvvm.ui.trending.TrendingFragmentDirections;


import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    private Context context;
    private List<Book> list;
private int status;
    public BookAdapter(List<Book> list, Context context,int status) {
        this.context = context;
        this.list = list;
        this.status=status;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_audiobook, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Book book = list.get(position);
        initView(book, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections dir;
                if (status==1){
                    dir = CategoryDetailFragmentDirections.Companion.actionCategoryDetailFragmentToBookFragment(book.getId());
                }else {
                    dir = CategoryFragmentDirections.Companion.actionCategoryFragmentToBookFragment(book.getId());
                }
                Navigation.findNavController(v).navigate(dir);

            }
        });


    }

    private void initView(Book book, MyViewHolder holder) {
        holder.textViewTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        Glide.with(holder.imageView.getContext())
                .load(book.getImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, tvAuthor;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_auther);
            imageView = itemView.findViewById(R.id.image);
        }
    }


}
