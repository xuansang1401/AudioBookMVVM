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
import com.t3h.basemvvm.ui.category.CategoryFragmentDirections;
import com.t3h.basemvvm.ui.home.HomeFragmentDirections;
import com.t3h.basemvvm.ui.trending.TrendingFragmentDirections;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {
    private Context context;
    private List<Book> list;


    //tham số truyền vào 1 list Item bạn muốn hiển thị
    public TrendingAdapter(List<Book> list, Context context) {
        this.context = context;
        this.list = list;

    }


    // xác định layout Item của bạn item trending
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trending, parent, false);//layout item
        return new MyViewHolder(view);
    }

    //thực hiện các thao tác vs item
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Book book = list.get(position);//lấy 1 phần tử theo list  và thứ tự
        initView(book, holder, position);// thêm dữ liệu vapf layout

        // click item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections dir = TrendingFragmentDirections.Companion.actionTrendingFragmentToBookFragment(book.getId());

                Navigation.findNavController(v).navigate(dir);
            }
        });


    }

    private void initView(Book book, MyViewHolder holder, int position) {
        holder.textViewTitle.setText(book.getTitle());
        int pos=position+1;
        holder.tvNumber.setText(pos+".");
        holder.tvContent.setText(book.getAuthor());
        Glide.with(context)
                .load(book.getCoverImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
    }

    //số phần tử
    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else return 0;
    }

    // xác định các thành phần của item
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, tvContent, tvNumber;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvContent = itemView.findViewById(R.id.tv_auther);
            imageView = itemView.findViewById(R.id.image);
        }
    }


}
