package test.android.com.rappitexttask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import test.android.com.rappitexttask.loadimages.ImageLoader;
import test.android.com.rappitexttask.module.Data;

public class DetailsActivity extends AppCompatActivity {

    Data data;
    ImageLoader imageLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.containsKey(MainActivity.EXTRA_DATA_PARCELABLE)){
            data = (Data) bundle.getParcelable(MainActivity.EXTRA_DATA_PARCELABLE);
            if(data != null){
                imageLoader = new ImageLoader(this);
                initView();
                return;
            }
        }
        Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show();
        finish();
    }

    TextView name_list;
    ImageView icon_list;
    TextView desc_list;

    private void initView() {
        name_list = (TextView) findViewById(R.id.name_text_view_list);
        icon_list = (ImageView) findViewById(R.id.icon_image_view_list);
        desc_list = (TextView) findViewById(R.id.desc_text_view_list);

        name_list.setText(data.getName());
        desc_list.setText(data.getSummary());
        imageLoader.DisplayImage(data.getImage().getImage100(),icon_list);
    }
}
