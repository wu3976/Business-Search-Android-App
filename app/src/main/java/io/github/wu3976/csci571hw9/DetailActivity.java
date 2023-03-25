package io.github.wu3976.csci571hw9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private TabLayout detail_tab;
    private ViewPager2 detail_vp2;


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            {
                finish();
                break;
            }
            default: break;
        }
        return true;
    }

    /*private String category_str, phone, is_open_now, displayed_addr,
            business_id, name, price, url;
    private Double lat, lon;
    private ArrayList<String> photo_urls;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle b = getIntent().getExtras();
        setTitle(b.getString("name"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*category_str = b.getString("category_str");
        phone = b.getString("phone");
        is_open_now = b.getString("is_open_now");
        displayed_addr = b.getString("displayed_addr");
        business_id = b.getString("business_id");
        name = b.getString("name");
        price = b.getString("price");
        url = b.getString("url");
        lat = Double.parseDouble(b.getString("lat"));
        lon = Double.parseDouble(b.getString("lon"));
        photo_urls = b.getStringArrayList("photos");*/

        detail_tab = findViewById(R.id.detail_tabs);

        detail_vp2 = findViewById(R.id.detail_viewpager);

        DetailViewPagerAdapter detail_vpadpt = new DetailViewPagerAdapter(this, b);
        detail_vp2.setAdapter(detail_vpadpt);

        new TabLayoutMediator(detail_tab, detail_vp2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0: { tab.setText("BUSINESS DETAILS"); break; }
                            case 1: { tab.setText("MAP LOCATION"); break; }
                            case 2: { tab.setText("REVIEWS"); break; }
                            default: break;
                        }
                    }
                }).attach();
    }
}