package com.cit.exp_manager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cit.exp_manager.R;
import com.cit.exp_manager.databinding.SampleCategoryItemBinding;
import com.cit.exp_manager.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList <Category> categoryList;

    public interface CategoryClickListener{

        void onCategoryClicked(Category category);
    }

    CategoryClickListener categoryClickListener;

    public CategoryAdapter(Context context, ArrayList<Category> categoryList, CategoryClickListener categoryClickListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        Category category=categoryList.get(position);
        holder.binding.categoryText.setText(category.getCategoryName());
        holder.binding.categoryIcon.setImageResource(category.getCategoryImage());
    holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));  //icon piche color set

    holder.itemView.setOnClickListener(c->{

        categoryClickListener.onCategoryClicked(category);


    });



    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }



    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        SampleCategoryItemBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {

            super(itemView);
            binding=SampleCategoryItemBinding.bind(itemView);


        }
    }

}
