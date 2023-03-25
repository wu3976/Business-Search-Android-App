package io.github.wu3976.csci571hw9;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RealMainActivity extends AppCompatActivity {
    public final static String AUTOCOMP_URL = "https://csci571p8.uc.r.appspot.com/autocomplete",
                        IPINFO_URL = "https://ipinfo.io/json",
                        IPINFO_TOKEN = "f60d3636012d38",
                        BUSINESS_SEARCH_URL = "https://csci571p8.uc.r.appspot.com/business_search",
                        BUSINESS_DETAIL_URL = "https://csci571p8.uc.r.appspot.com/business_detail",
                        GEOCODING_URL = "https://maps.googleapis.com/maps/api/geocode/json",
                        GEOCODING_KEY = "AIzaSyAGHUW4k0e1PTMNj1xnsXskjT1tOTWxU-M";

    public final static double MILE_TO_METER = 1609.34;

    private EditText distinput, locinput;
    private AutoCompleteTextView kwinput;
    private ArrayAdapter<String> kwinput_adpt;
    private TextView category_label, test;
    private Spinner category_dropdown;
    private CheckBox autodet_checkbox;
    private MaterialButton submit_button, clear_button;

    private RequestQueue req_q;

    private ArrayList<BusinessRowModel> bmodel_arr;
    private BAdapter row_adpt;
    private RecyclerView result_table;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.real_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calender_icon: {
                Intent i = new Intent(this, ReservationActivity.class);
                startActivity(i);
                break;
            }
            default: break;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Csci571hw9);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);

        distinput = findViewById(R.id.distinput);
        req_q = Volley.newRequestQueue(this);
        kwinput = findViewById(R.id.kwinput);
        kwinput.setHint(constructReqHint("KeyWord"));

        kwinput_adpt = new AutocompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        kwinput.setAdapter(kwinput_adpt);
        kwinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 3) {
                    StringRequest autocom_req = new StringRequest(
                            Request.Method.GET, AUTOCOMP_URL + "?text="
                            + URLEncoder.encode(kwinput.getText().toString()),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject autocom_res_obj = null;
                                    try {
                                        autocom_res_obj = new JSONObject(response);
                                        JSONArray cat_list_raw = autocom_res_obj.getJSONArray("categories"),
                                        term_list_raw = autocom_res_obj.getJSONArray("terms");

                                        ArrayList<String> autocomp_arr = new ArrayList<>();
                                        for (int i = 0; i < cat_list_raw.length(); i++) {
                                            autocomp_arr.add(cat_list_raw.getJSONObject(i).getString("title"));
                                        }
                                        for (int i = 0; i < term_list_raw.length(); i++) {
                                            autocomp_arr.add(term_list_raw.getJSONObject(i).getString("text"));
                                        }
                                        //Toast.makeText(RealMainActivity.this, autocomp_arr.toString(), Toast.LENGTH_SHORT).show();
                                        kwinput_adpt.clear();
                                        for (String str : autocomp_arr) {
                                            kwinput_adpt.add(str);
                                        }
                                        kwinput_adpt.getFilter().filter(kwinput.getText(), null);

                                    } catch (JSONException e) {
                                        Toast.makeText(RealMainActivity.this, "autocomplete error",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RealMainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    req_q.add(autocom_req);
                } else {
                    kwinput_adpt.clear();
                    kwinput_adpt.getFilter().filter(kwinput.getText(), null);
                }
            }
        });
        category_label = findViewById(R.id.category_label);
        category_label.setText(constructReqPurpleLabel("Category"));

        category_dropdown = findViewById(R.id.category_spinner);

        String cat_items[] = new String[]{
                "Default", "Arts and Entertainment", "Health and Medical",
                "Hotels and Travel", "Food", "Professional Services"
        };

        ArrayAdapter<String> adpt_cat_items = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, cat_items
        );

        category_dropdown.setAdapter(adpt_cat_items);

        locinput = findViewById(R.id.location_input);
        locinput.setHint(constructReqHint("Location"));

        autodet_checkbox = findViewById(R.id.autodetect_checkbox);
        autodet_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (autodet_checkbox.isChecked()) {
                    locinput.setVisibility(View.INVISIBLE);
                }  else {
                    locinput.setVisibility(View.VISIBLE);
                }
            }
        });
        submit_button = findViewById(R.id.submit_button);
        clear_button = findViewById(R.id.clear_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (autodet_checkbox.isChecked()) {
                    //Toast.makeText(RealMainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                    StringRequest autodet_req = new StringRequest(
                            Request.Method.GET, IPINFO_URL + "?token=" + IPINFO_TOKEN,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject ipinfo_res_obj = new JSONObject(response);
                                        String loc_str = ipinfo_res_obj.getString("loc");
                                        int comma_idx = loc_str.indexOf(',');
                                        double lat = Double.parseDouble(loc_str.substring(0,comma_idx)),
                                                lon = Double.parseDouble(loc_str.substring(comma_idx + 1));
                                        //Toast.makeText(RealMainActivity.this, lat+","+lon, Toast.LENGTH_SHORT).show();
                                        do_business_request(lat, lon);
                                    } catch (JSONException e) {
                                        Toast.makeText(RealMainActivity.this, "ipinfo error",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RealMainActivity.this, error.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    req_q.add(autodet_req);
                } else {
                    String geocoding_req_url = GEOCODING_URL
                            + "?address=" + URLEncoder.encode(locinput.getText().toString())
                            + "&key=" + GEOCODING_KEY;
                    StringRequest geocoding_req = new StringRequest(Request.Method.GET, geocoding_req_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject geocoding_res_obj = new JSONObject(response),
                                                latlonobj = geocoding_res_obj.getJSONArray("results")
                                                .getJSONObject(0).getJSONObject("geometry")
                                                .getJSONObject("location");
                                        double lat = Double.parseDouble(latlonobj.getString("lat")),
                                                lon = Double.parseDouble(latlonobj.getString("lng"));
                                        do_business_request(lat, lon);
                                    } catch (JSONException e) {
                                        Toast.makeText(RealMainActivity.this, e.toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(RealMainActivity.this, error.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    req_q.add(geocoding_req);
                }
            }
        });
        clear_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                kwinput.setText("");
                distinput.setText("");
                locinput.setText("");
                category_dropdown.setSelection(0);
                autodet_checkbox.setChecked(false);
                bmodel_arr.clear();
                row_adpt.notifyDataSetChanged();
            }
        });
        bmodel_arr = new ArrayList<>();
        /*
        bmodel_arr.add(new BusinessRowModel(
                "1", R.drawable.yelp_logo, "first", "5", "10"));
        bmodel_arr.add(new BusinessRowModel(
                "2", R.drawable.yelp_logo, "second", "3.5", "4"));
        bmodel_arr.add(new BusinessRowModel(
                "3", R.drawable.yelp_logo, "third", "4", "8"));*/
        result_table = findViewById(R.id.result_table_view);
        row_adpt = new BAdapter(this, bmodel_arr, new OnItemClickListener() {
            @Override
            public void OnItemClick(BusinessRowModel item) {
                StringRequest detail_req = new StringRequest(Request.Method.GET,
                        BUSINESS_DETAIL_URL + "?id=" + item.business_id,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject det_res_obj = null;
                                try {
                                    det_res_obj = new JSONObject(response);
                                    JSONArray cat_arr = det_res_obj.getJSONArray("categories");
                                    StringBuilder cat_str_builder = new StringBuilder();
                                    for (int i = 0; i < cat_arr.length(); i++) {
                                        if (i != 0) {
                                            cat_str_builder.append(" | ");
                                        }
                                        cat_str_builder.append(cat_arr.getString(i));
                                    }
                                    String catstr = cat_str_builder.toString(),
                                            lat = det_res_obj.getJSONObject("coordinates")
                                            .getString("latitude"),
                                            lon = det_res_obj.getJSONObject("coordinates")
                                            .getString("longitude"),
                                            phone = det_res_obj.isNull("phone") ?
                                                    "" :  det_res_obj.getString("phone");
                                    String is_open_now = det_res_obj.isNull("is_open_now") ?
                                            "null" : (det_res_obj.getBoolean("is_open_now") ?
                                            "open" : "close");
                                    JSONObject loc_obj = det_res_obj.getJSONObject("location");
                                    String displayed_addr = loc_obj.getString("display_address");
                                    String business_id = det_res_obj.getString("id");
                                    String name = det_res_obj.getString("name");
                                    ArrayList<String> photos = new ArrayList<>();
                                    JSONArray photo_jsonarr = det_res_obj.getJSONArray("photos");
                                    for (int i = 0; i < photo_jsonarr.length(); i++) {
                                        photos.add(photo_jsonarr.getString(i));
                                    }
                                    String price = !det_res_obj.isNull("price") ?
                                            det_res_obj.getString("price") : "";
                                    String url = det_res_obj.getString("url");

                                    Bundle b = new Bundle();
                                    b.putString("category_str", catstr);
                                    b.putString("lat", lat);
                                    b.putString("lon", lon);
                                    b.putString("phone", phone);
                                    b.putString("is_open_now", is_open_now);
                                    b.putString("displayed_addr", displayed_addr);
                                    b.putString("business_id", business_id);
                                    b.putString("name", name);
                                    b.putStringArrayList("photos", photos);
                                    b.putString("price", price);
                                    b.putString("url", url);

                                    Intent i = new Intent(RealMainActivity.this, DetailActivity.class);
                                    i.putExtras(b);
                                    startActivity(i);
                                } catch (JSONException e) {
                                    Toast.makeText(RealMainActivity.this, e.toString(),
                                            Toast.LENGTH_SHORT).show();;
                                }
                            }
                        },
                        new Response.ErrorListener(){

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(RealMainActivity.this, error.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                req_q.add(detail_req);
            }
        });
        result_table.setAdapter(row_adpt);
        result_table.setLayoutManager(new LinearLayoutManager(this));
    }

    public SpannableStringBuilder constructReqPurpleLabel(String str) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(str);
        int ppl_s = 0, ppl_e = builder.length(), r_l = builder.length();
        builder.append(" *");
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.purple_500)),
                ppl_s, ppl_e, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)),
                r_l, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public SpannableStringBuilder constructReqHint(String str) {
        String colored = " *";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(str);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    private void do_business_request(double lat, double lon){
        String bus_req_url = BUSINESS_SEARCH_URL
                + "?latitude=" + lat
                + "&longitude=" + lon
                + "&term=" + kwinput.getText().toString();
        if (!distinput.getText().toString().equals("")) {
            double distance_in_mile = Double.parseDouble(distinput.getText().toString());
            bus_req_url += "&radius=" + (int)(distance_in_mile * MILE_TO_METER);
        }
        int cat_idx = category_dropdown.getSelectedItemPosition();
        switch (cat_idx) {
            case 0: {
                break;
            }
            case 1: {
                bus_req_url += "&categories=arts";
                break;
            }
            case 2: {
                bus_req_url += "&categories=health";
                break;
            }
            case 3: {
                bus_req_url += "&categories=hotelstravel";
                break;
            }
            case 4: {
                bus_req_url += "&categories=food";
                break;
            }
            case 5: {
                bus_req_url += "&categories=professional";
                break;
            }
            default:
                break;
        }
        StringRequest business_req = new StringRequest(Request.Method.GET, bus_req_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        bmodel_arr.clear();
                        try {
                            JSONObject bus_res_obj = new JSONObject(response);
                            JSONArray businesses = bus_res_obj.getJSONArray("business_list");
                            for (int i = 0; i < businesses.length(); i++) {
                                JSONObject b = businesses.getJSONObject(i);
                                String id = b.getString("id"),
                                        name = b.getString("name"),
                                        img_src = b.getString("image_url"),
                                        rating = b.getString("rating"),
                                        distance = Integer.toString(
                                                (int)(Double.parseDouble(b.getString("distance")) / MILE_TO_METER)
                                        );
                                BusinessRowModel bmodel = new BusinessRowModel(Integer.toString(i + 1),
                                        img_src, name, rating, distance, id);
                                bmodel_arr.add(bmodel);
                            }
                            row_adpt.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(RealMainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RealMainActivity.this, error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        req_q.add(business_req);
            /*
            switch (cat_idx) {
                case 0: { break; }
                case 1: { bus_req_obj.put("categories", "arts"); break; }
                case 2: { bus_req_obj.put("categories", "health"); break; }
                case 3: { bus_req_obj.put("categories", "hotelstravel"); break; }
                case 4: { bus_req_obj.put("categories", "food"); break; }
                case 5: { bus_req_obj.put("categories", "professional"); break; }
                default: break;
            }*/

    }
}