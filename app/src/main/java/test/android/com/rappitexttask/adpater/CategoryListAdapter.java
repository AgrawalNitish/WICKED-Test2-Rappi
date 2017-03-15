package test.android.com.rappitexttask.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import test.android.com.rappitexttask.R;


public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.StatusHolder> {

    List<String> itemList;
    Context context;

    public CategoryListAdapter(List<String> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public StatusHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_adapter_row, parent, false);

        return new StatusHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StatusHolder holder, int position) {
        String item = itemList.get(position);
        holder.itemTextView.setText(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class StatusHolder extends RecyclerView.ViewHolder{
        TextView itemTextView;
        public StatusHolder(View view) {
            super(view);
            itemTextView = (TextView) view.findViewById(R.id.item_text_view);

        }
    }
}
