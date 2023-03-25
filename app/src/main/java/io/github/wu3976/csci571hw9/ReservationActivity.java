package io.github.wu3976.csci571hw9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReservationActivity extends AppCompatActivity {
    ArrayList<ReservationRowModel> res_arr;
    RAdapter res_adpt;
    RecyclerView res_table_view;
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default: break;
        }
        return true;
    }

    private void updatemodelview() {
        res_arr.clear();
        SharedPreferences pref = getSharedPreferences("hw9sh", 0);
        String data = pref.getString("data", "[]");
        try {
            JSONArray prefarr_json = new JSONArray(data);
            for (int i = 0; i < prefarr_json.length(); i++) {
                JSONObject obj = prefarr_json.getJSONObject(i);
                ReservationRowModel rowModel = new ReservationRowModel((i + 1) + "",
                        obj.getString("name"), obj.getString("date"),
                        obj.getString("time"), obj.getString("email"));
                res_arr.add(rowModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        res_adpt.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation2);
        setTitle("Yelp");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        res_table_view = findViewById(R.id.reservation_table_view);
        res_arr = new ArrayList<>();

        // testing
        //res_arr.add(new ReservationRowModel("1", "A", "211", "231", "dads"));
        //res_arr.add(new ReservationRowModel("2", "B", "211", "233", "sdvsvds"));
        res_adpt = new RAdapter(this, res_arr, new OnItemTouchListener() {
            @Override
            public void OnItemTouch(int position, ReservationRowModel item, MotionEvent motionEvent) {
                Toast.makeText(ReservationActivity.this, "Removing Existing Reservation", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = getSharedPreferences("hw9sh", 0);
                String data = pref.getString("data", "[]");
                JSONArray data_arr = null;
                try {
                    data_arr = new JSONArray(data);
                    data_arr.remove(position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("data", data_arr.toString());
                editor.commit();
                updatemodelview();
            }
        });
        res_table_view.setAdapter(res_adpt);
        res_table_view.setLayoutManager(new LinearLayoutManager(this));
        updatemodelview();
    }

    @Override
    protected void onResume() {

        super.onResume();
        updatemodelview();
    }
}