package test.android.com.rappitexttask;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import test.android.com.rappitexttask.apicalls.ApiNames;
import test.android.com.rappitexttask.apicalls.Requests;
import test.android.com.rappitexttask.base.BaseActivityWithApi;
import test.android.com.rappitexttask.module.Data;
import test.android.com.rappitexttask.utils.CheckNetwork;

public class MainActivity extends BaseActivityWithApi {

    final String fileName = "Data.json";
    final String filePath = "file_path";
    ArrayList<Data> dataList = new ArrayList<Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (dataList != null && dataList.size() == 0) {
            if(CheckNetwork.getInstance().isNetworkAvailable(this)) {
                callApi(Requests.getInstance().data);
            }else if(getFile() != null && getFile().length()>0){
                 parseDataFromJson();
            }else {
                Toast.makeText(this,"No data available",Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }

    private void parseDataFromJson() {
        String jsonData = getFiletoJsonData();
        Log.e("Jsont is ",jsonData);

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONObject("feed").getJSONArray("entry");
            Data data;
            for(int i=0;i<jsonArray.length();i++){
                data = new Data(jsonArray.getJSONObject(i));
                dataList.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(dataList != null && dataList.size()>0){

        }
    }

    private String getFiletoJsonData() {
        try {
            File file = getFile();
            if(file != null && file.length()>0) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                fis.read(data);
                fis.close();
                String str = new String(data, "UTF-8");
                if(str != null & str.length()>0) {
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
        Log.e("Response is ",jsonObject.toString());
        saveFileInPrivateMemory(jsonObject.toString());
    }

    private void showDataInView() {

    }

    private void saveFileInPrivateMemory(String data) {
        File file = new File(getFilesDir(), fileName);

        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            //saveFilePath(file.getAbsolutePath());
            parseDataFromJson();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"Unavailable to fetch data from server",Toast.LENGTH_SHORT).show();
        }

    }

    private void testFile() {
        File file = null;
        try {
            file = getFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(file != null && file.length()>0){

        }
    }

  /*  private void saveFilePath(String absolutePath) {
        App.editor.putString(absolutePath,"").commit();
    }*/

    public File getFile() {
        File file;
        try {
            file = new File(getFilesDir(), fileName);
            if(file.exists()){
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
