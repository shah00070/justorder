package in.creativebucket.justorder.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import in.creativebucket.justorder.R;
import in.creativebucket.justorder.location.GPSTracker;

/**
 * Created by chandan on 5/12/15.
 */
public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GPSTracker gpsTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // In the onCreate method
        AutoCompleteTextView serviceNameEditText = (AutoCompleteTextView) rootView.findViewById(R.id.service_name_edittext);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, getActivity().getResources().getStringArray(R.array.service_list));
        serviceNameEditText.setAdapter(adapter);

        serviceNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(getActivity(), "current", Toast.LENGTH_LONG).show();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker in Sydney, Australia, and move the camera.

        LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        map.addMarker(new MarkerOptions().position(latLng).title(gpsTracker.getLocation().toString()));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
