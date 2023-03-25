package io.github.wu3976.csci571hw9;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detail_frag_3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detail_frag_3 extends Fragment {

    public static final String COMMENT_URL = "https://csci571p8.uc.r.appspot.com/review";
    private String business_id;
    private LinearLayout comment_sec;

    private ConstraintLayout comment1, comment2, comment3;
    private TextView cname1, crating1, ctext1, cdate1;
    private TextView cname2, crating2, ctext2, cdate2;
    private TextView cname3, crating3, ctext3, cdate3;

    private RequestQueue req_q;
    public detail_frag_3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment detail_frag_3.
     */
    // TODO: Rename and change types and number of parameters
    public static detail_frag_3 newInstance(Bundle b) {
        detail_frag_3 fragment = new detail_frag_3();
        Bundle args = new Bundle();
        args.putString("business_id", b.getString("business_id"));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        business_id = getArguments().getString("business_id");
        req_q = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_frag_3, container, false);
    }

    private void fill_comments(JSONObject comment_res_obj) throws JSONException {
        JSONArray comarr = comment_res_obj.getJSONArray("review");
        //Toast.makeText(getContext(), "" + comarr.length(), Toast.LENGTH_SHORT).show();
        Log.d("commlength", Integer.toString(comarr.length()));
        if (comarr.length() >= 1) {
            JSONObject comm = comarr.getJSONObject(0);
            comment1.setVisibility(View.VISIBLE);
            cname1.setText(comm.getString("username"));
            crating1.setText("Rating: " + Double.toString(comm.getDouble("rating")));
            ctext1.setText(comm.getString("text"));
            cdate1.setText(comm.getString("time_created"));
        }
        if (comarr.length() >= 2) {
            JSONObject comm = comarr.getJSONObject(1);
            comment2.setVisibility(View.VISIBLE);
            cname2.setText(comm.getString("username"));
            crating2.setText("Rating: " + Double.toString(comm.getDouble("rating")));
            ctext2.setText(comm.getString("text"));
            cdate2.setText(comm.getString("time_created"));
        }
        if (comarr.length() >= 3) {
            JSONObject comm = comarr.getJSONObject(2);
            comment3.setVisibility(View.VISIBLE);
            cname3.setText(comm.getString("username"));
            crating3.setText("Rating: " + Double.toString(comm.getDouble("rating")));
            ctext3.setText(comm.getString("text"));
            cdate3.setText(comm.getString("time_created"));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        comment1 = view.findViewById(R.id.comment1);
        comment2 = view.findViewById(R.id.comment2);
        comment3 = view.findViewById(R.id.comment3);

        cname1 = view.findViewById(R.id.cname1);
        crating1 = view.findViewById(R.id.crating1);
        ctext1 = view.findViewById(R.id.ctext1);
        cdate1 = view.findViewById(R.id.cdate1);

        cname2 = view.findViewById(R.id.cname2);
        crating2 = view.findViewById(R.id.crating2);
        ctext2 = view.findViewById(R.id.ctext2);
        cdate2 = view.findViewById(R.id.cdate2);

        cname3 = view.findViewById(R.id.cname3);
        crating3 = view.findViewById(R.id.crating3);
        ctext3 = view.findViewById(R.id.ctext3);
        cdate3 = view.findViewById(R.id.cdate3);

        StringRequest comment_req = new StringRequest(Request.Method.GET,
                COMMENT_URL + "?id=" + business_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject comment_res_obj = new JSONObject(response);
                            fill_comments(comment_res_obj);
                        } catch (JSONException e) {
                            Log.e("xxx", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("comm net err", error.toString());
                    }
                });
        req_q.add(comment_req);
    }
}