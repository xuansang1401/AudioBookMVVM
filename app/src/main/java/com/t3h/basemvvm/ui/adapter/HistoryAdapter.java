package com.t3h.basemvvm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.room.History;
import com.t3h.basemvvm.ui.category.CategoryDetailFragmentDirections;
import com.t3h.basemvvm.ui.category.CategoryFragmentDirections;
import com.t3h.basemvvm.ui.home.HomeFragmentDirections;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private Context context;
    private List<History> list;
    public HistoryAdapter(List<History> list, Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Book book = list.get(position).getBook();
        initView(book, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections dir= HomeFragmentDirections.Companion.actionHomeFragmentToBookFragment(book.getId());

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
                .error(R.drawable.logo)
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
