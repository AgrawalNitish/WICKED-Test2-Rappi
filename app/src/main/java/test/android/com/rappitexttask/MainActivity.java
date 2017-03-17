package test.android.com.rappitexttask;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import test.android.com.rappitexttask.adpater.DataListAdapter;
import test.android.com.rappitexttask.apicalls.ApiNames;
import test.android.com.rappitexttask.apicalls.Requests;
import test.android.com.rappitexttask.base.BaseActivityWithApi;
import test.android.com.rappitexttask.dialog.ShowCategoriesDialog;
import test.android.com.rappitexttask.module.Data;
import test.android.com.rappitexttask.recycler.RecyclerItemClickListener;
import test.android.com.rappitexttask.utils.CheckNetwork;

public class MainActivity extends BaseActivityWithApi {

    public static final String EXTRA_DATA_PARCELABLE = "sc_extra_Parcelable";
    final String fileName = "Data.json";
    ArrayList<Data> dataListAll = new ArrayList<Data>();
    ArrayList<Data> dataList = new ArrayList<Data>();
    protected RecyclerView recyclerView;
    protected TextView noDataMessageTextView;
    protected DataListAdapter mAdapter;
    ArrayList<String> categories = new ArrayList<>();
    ShowCategoriesDialog showCategoriesDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noDataMessageTextView = (TextView) findViewById(R.id.message);
        recyclerView = (RecyclerView) findViewById(R.id.rList);
        mAdapter = new DataListAdapter(dataList, this);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //mActivity.callFragment(ArtifactDetails.create(lst.get(position)),true,R.id.fragment_containers);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EXTRA_DATA_PARCELABLE, (Parcelable) dataList.get(position));
                        callIntent(DetailsActivity.class,bundle);
                    }
                }));
        if (dataList != null && dataList.size() == 0) {
            if (CheckNetwork.getInstance().isNetworkAvailable(this)) {
                callApi(Requests.getInstance().data);
            } else if (getFile() != null && getFile().length() > 0) {
                parseDataFromJsonAndCategories();
            } else {
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
            }
        }
        updateRecyclerViewVisiblity();
    }

    private void updateRecyclerViewVisiblity() {
        if (dataList != null && dataList.size() > 0) {
            noDataMessageTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            return;
        }
        noDataMessageTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void parseDataFromJsonAndCategories() {
        String jsonData = getFiletoJsonData();
        Log.e("Jsont is ", jsonData);
        HashMap<String, String> categoryMap = new HashMap<>();
        categoryMap.put("All", "All");
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONObject("feed").getJSONArray("entry");
            Data data;

            for (int i = 0; i < jsonArray.length(); i++) {
                data = new Data(jsonArray.getJSONObject(i));
                categoryMap.put(data.getCategory(), data.getCategory());
                dataListAll.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (String key : categoryMap.keySet()) {
            categories.add(key);
        }

        if (categories != null && categories.size() > 0) {
            Collections.sort(categories, new Comparator<String>() {
                public int compare(String v1, String v2) {
                    return v1.compareTo(v2);
                }
            });
            Collections.swap(categories, categories.indexOf("All"), 0);
            initCategoryItems();
        }

        if (dataListAll != null && dataListAll.size() > 0) {
            changeListData("All");
            if (dataListAll != null && dataListAll.size() > 0) {
                intiItemList();
            }
        }
    }

    private void initCategoryItems() {
        showCategoriesDialog = new ShowCategoriesDialog(this, categories, new ShowCategoriesDialog.OnCategorySelectedListener() {
            @Override
            public void onCategorySelect(int position) {
                changeListData(categories.get(position));
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void changeListData(String category) {
        dataList.clear();
        for (Data item : dataListAll) {
            if (category.equals("All")) {
                dataList.add(item);
            } else if (item.getCategory().equals(category)) {
                dataList.add(item);
            }
        }
        updateRecyclerViewVisiblity();
    }

    private void intiItemList() {
        mAdapter.notifyDataSetChanged();
        updateRecyclerViewVisiblity();
    }

    private String getFiletoJsonData() {
        try {
            File file = getFile();
            if (file != null && file.length() > 0) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String str = new String(data, "UTF-8");
                if (str != null & str.length() > 0) {
                    return str;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void errorResponseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {

    }

    @Override
    public void responseHandler(JSONObject jsonObject, ApiNames serviceTaskType) {
        Log.e("Response is ", jsonObject.toString());
        saveFileInPrivateMemory(jsonObject.toString());
    }

    private void saveFileInPrivateMemory(String data) {
        File file = new File(getFilesDir(), fileName);

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            //saveFilePath(file.getAbsolutePath());
            parseDataFromJsonAndCategories();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unavailable to fetch data from server", Toast.LENGTH_SHORT).show();
        }

    }

    public File getFile() {
        File file;
        try {
            file = new File(getFilesDir(), fileName);
            if (file.exists()) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_soft_grid) {
            initGridDisplay();
            return true;
        } else if (id == R.id.action_soft_list) {
            initListDisplay();
            return true;
        } else if (id == R.id.select_category) {
            showCategoriesDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Display a list
    private void initListDisplay() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.updateLayoutOnNotifyChange(DataListAdapter.LIST);
    }

    // Display the Grid
    private void initGridDisplay() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter.updateLayoutOnNotifyChange(DataListAdapter.GRID);
    }

}
