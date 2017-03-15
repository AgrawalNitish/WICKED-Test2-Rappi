package test.android.com.rappitexttask.adpater;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import test.android.com.rappitexttask.R;
import test.android.com.rappitexttask.loadimages.ImageLoader;
import test.android.com.rappitexttask.module.Data;


public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataHolder> {

    public static final int GRID = 0;
    public static final int LIST = 1;
    int mLayoutType = GRID;
    protected List<Data> itemList;
    protected Context context;
    ImageLoader imageLoader;
    int viewType ;
    public DataListAdapter(List<Data> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        imageLoader = new ImageLoader(context);
    }

    public void updateLayoutOnNotifyChange(int mLayoutType){
        this.mLayoutType = mLayoutType;
        notifyDataSetChanged();
    }

    public int getCurrentLayout(){
        if(mLayoutType == LIST){
            return R.layout.row_item;
        }
        return R.layout.grid_item;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return new DataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DataHolder holder, int position) {
        Data item = itemList.get(position);
        if(mLayoutType == LIST){
            holder.list_card_view.setVisibility(View.VISIBLE);
            holder.grid_card_view.setVisibility(View.GONE);
            holder.name_list.setText(item.getName());
            holder.desc_list.setText(item.getSummary());
            imageLoader.DisplayImage(item.getImage().getImage100(), holder.icon_list);
        }else {
            holder.list_card_view.setVisibility(View.GONE);
            holder.grid_card_view.setVisibility(View.VISIBLE);
            holder.name.setText(item.getName());
            imageLoader.DisplayImage(item.getImage().getImage100(), holder.icon);
        }
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder{
        CardView grid_card_view, list_card_view;
        TextView name, name_list;
        ImageView icon, icon_list;
        TextView desc_list;
        public DataHolder(View view) {
            super(view);
        grid_card_view = (CardView) view.findViewById(R.id.grid_card_view);
        list_card_view = (CardView) view.findViewById(R.id.list_card_view);
        name = (TextView) view.findViewById(R.id.name_text_view);
        icon = (ImageView) view.findViewById(R.id.icon_image_view);
        name_list = (TextView) view.findViewById(R.id.name_text_view_list);
        icon_list = (ImageView) view.findViewById(R.id.icon_image_view_list);
        desc_list = (TextView) view.findViewById(R.id.desc_text_view_list);
        }
    }
}
