package io.github.wu3976.csci571hw9;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detail_frag_1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detail_frag_1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String category_str, phone, is_open_now, displayed_addr,
            business_id, name, price, url;
    private Double lat, lon;
    private ArrayList<String> photo_urls;

    private TextView addr_v, phone_v, cat_v, price_v, status_v, buslink_v;
    private ImageView imgsv_1, imgsv_2, imgsv_3;

    private MaterialButton reserve_button;
    public detail_frag_1() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addr_v = view.findViewById(R.id.det_addr);
        phone_v = view.findViewById(R.id.det_phone);
        cat_v = view.findViewById(R.id.det_cat);
        price_v = view.findViewById(R.id.det_price);
        status_v = view.findViewById(R.id.det_status);
        buslink_v = view.findViewById(R.id.det_bus_link);
        imgsv_1 = view.findViewById(R.id.imgslidev_1);
        imgsv_2 = view.findViewById(R.id.imgslidev_2);
        imgsv_3 = view.findViewById(R.id.imgslidev_3);
        reserve_button = view.findViewById(R.id.reserve_button);

        StringBuilder addrbuild = new StringBuilder();
        try {
            JSONArray addr_arr = new JSONArray(displayed_addr);
            for (int i = 0; i < addr_arr.length(); i++) {
                if (i != 0) {
                    addrbuild.append(' ');
                }
                addrbuild.append(addr_arr.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addr_v.setText(addrbuild.toString());
        phone_v.setText(phone.isEmpty() ? "N/A" : phone);
        cat_v.setText(category_str.isEmpty() ? "N/A" : category_str);
        price_v.setText(price.isEmpty() ? "N/A" : price);
        if (is_open_now.equals("open")) {
            status_v.setText("Open Now");
            status_v.setTextColor(getResources().getColor(R.color.green));
        } else if (is_open_now.equals("close")) {
            status_v.setText("Closed");
            status_v.setTextColor(getResources().getColor(R.color.red));
        } else {
            status_v.setText("N/A");
        }
        buslink_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(url));
                startActivity(openURL);
            }
        });
        if(photo_urls.size() >= 1) {
            Picasso.get().load(photo_urls.get(0)).into(imgsv_1);
        }
        if(photo_urls.size() >= 2) {
            Picasso.get().load(photo_urls.get(1)).into(imgsv_2);
        }
        if(photo_urls.size() >= 3) {
            Picasso.get().load(photo_urls.get(2)).into(imgsv_3);
        }
        reserve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.reserve_dialog_layout);

                EditText d_email_input = dialog.findViewById(R.id.d_email_input),
                        d_date_input = dialog.findViewById(R.id.d_date_input),
                        d_start_time_input = dialog.findViewById(R.id.d_start_time_input);
                Button d_submit_button = dialog.findViewById(R.id.d_submit_button),
                        d_cancel_button = dialog.findViewById(R.id.d_cancel_button);

                d_start_time_input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerFragment newFragment = new TimePickerFragment(d_start_time_input);
                        newFragment.show(getChildFragmentManager(), "timePicker");
                    }
                });
                d_date_input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerFragment frag = new DatePickerFragment(d_date_input);
                        frag.show(getChildFragmentManager(), "datePicker");
                    }
                });

                d_cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                d_submit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String entered_email = d_email_input.getText().toString();
                        int at_occr = 0;
                        for (int i = 0; i < entered_email.length(); i++) {
                            if (entered_email.charAt(i) == '@') {
                                at_occr++;
                            }
                            if (at_occr >= 2) {
                                break;
                            }
                        }
                        String entered_time = d_start_time_input.getText().toString();
                        String hour_str, min_str;
                        if (entered_time.indexOf(':') != -1) {
                            hour_str = entered_time.substring(0, entered_time.indexOf(':'));
                            min_str = entered_time.substring(entered_time.indexOf(':') + 1);
                        } else {
                            hour_str = "1";
                            min_str = "0";
                        }
                        int hour = Integer.parseInt(hour_str), minute = Integer.parseInt(min_str);

                        if (entered_email.toString().indexOf('@') == -1
                        || at_occr != 1) {
                            Toast.makeText(getContext(),
                                    "Email address is invalid",
                                    Toast.LENGTH_SHORT).show();
                        } else if (hour < 10 || hour > 17) {
                            Toast.makeText(getContext(),
                                    "Time is not between 10AM to 5PM",
                                    Toast.LENGTH_SHORT).show();
                        } else if (hour == 17 && minute != 0) {
                            Toast.makeText(getContext(),
                                    "Time is not between 10AM to 5PM",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(),
                                    "Reservation Booked",
                                    Toast.LENGTH_SHORT).show();
                            SharedPreferences pref = getContext().getSharedPreferences("hw9sh", 0);
                            String data = pref.getString("data", "[]");
                            JSONArray data_arr = null;
                            try {
                                data_arr = new JSONArray(data);
                                JSONObject curr_obj = new JSONObject();
                                String currdate = d_date_input.getText().toString(),
                                        currtime = d_start_time_input.getText().toString(),
                                        curremail = d_email_input.getText().toString();
                                curr_obj.put("name", name);
                                curr_obj.put("date", currdate);
                                curr_obj.put("time", currtime);
                                curr_obj.put("email", curremail);
                                boolean flag = true;
                                for (int i = 0; i < data_arr.length(); i++) {
                                    if (data_arr.getJSONObject(i).getString("name").equals(name)) {
                                        data_arr.put(i, curr_obj);
                                        flag = false;
                                    }
                                }
                                if (flag) {
                                    data_arr.put(curr_obj);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("data", data_arr.toString());
                            editor.commit();
                        }


                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment detail_frag_1.
     */
    // TODO: Rename and change types and number of parameters
    public static detail_frag_1 newInstance(Bundle b) {
        detail_frag_1 fragment = new detail_frag_1();
        Bundle args = new Bundle();
        args.putBundle("bundle", b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments().getBundle("bundle");
        category_str = b.getString("category_str");
        phone = b.getString("phone");
        is_open_now = b.getString("is_open_now");
        displayed_addr = b.getString("displayed_addr");
        business_id = b.getString("business_id");
        name = b.getString("name");
        price = b.getString("price");
        url = b.getString("url");
        lat = Double.parseDouble(b.getString("lat"));
        lon = Double.parseDouble(b.getString("lon"));
        photo_urls = b.getStringArrayList("photos");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_frag_1, container, false);
    }


}