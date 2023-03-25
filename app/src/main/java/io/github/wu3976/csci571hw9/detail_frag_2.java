package io.github.wu3976.csci571hw9;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detail_frag_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detail_frag_2 extends Fragment implements OnMapReadyCallback {


    private double lat, lon;
    private GoogleMap gmap;
    public detail_frag_2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment detail_frag_2.
     */
    // TODO: Rename and change types and number of parameters
    public static detail_frag_2 newInstance(Bundle b) {
        detail_frag_2 fragment = new detail_frag_2();
        Bundle args = new Bundle();
        args.putBundle("bundle", b);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lat = Double.parseDouble(getArguments().getBundle("bundle").getString("lat"));
        lon = Double.parseDouble(getArguments().getBundle("bundle").getString("lon"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_frag_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        LatLng markerloc = new LatLng(lat, lon);
        gmap.addMarker(new MarkerOptions().position(markerloc)
                .title("Marker"));
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerloc, 15));
    }
}