package test.android.com.rappitexttask.dialog;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;


import java.util.ArrayList;

import test.android.com.rappitexttask.R;
import test.android.com.rappitexttask.adpater.CategoryListAdapter;
import test.android.com.rappitexttask.recycler.RecyclerItemClickListener;


public class ShowCategoriesDialog extends BasePopupWindowDialog {

    ArrayList<String> itemList;
    OnCategorySelectedListener listener;

    public interface OnCategorySelectedListener {
        public void onCategorySelect(int position);
    }

    public ShowCategoriesDialog(Activity activity, ArrayList<String> itemList, OnCategorySelectedListener listener) {
        super(activity);
        this.listener = listener;
        this.itemList = itemList;
    }

    private RecyclerView recyclerView;
    private CategoryListAdapter mAdapter;

    @Override
    protected void handleChildViews(View layoutView, final PopupWindow popup) {
        super.handleChildViews(layoutView,popup);

        recyclerView = (RecyclerView) layoutView.findViewById(R.id.rList);
        mAdapter = new CategoryListAdapter(itemList, activity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (activity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        listener.onCategorySelect(position);
                        closePopup();
                    }
                }));

    }

    @Override
    public int getLayout() {
        return R.layout.show_category_list_dialog;
    }


    @Override
    public void onClick(View view) {

    }
}
