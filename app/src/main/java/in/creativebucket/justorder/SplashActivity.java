package in.creativebucket.justorder;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import in.creativebucket.justorder.location.GPSTracker;
import in.creativebucket.justorder.location.LocationTrackListener;
import in.creativebucket.justorder.location.SearchAddressByLatLng;
import in.creativebucket.justorder.preferences.JustOrderStateMachine;

/**
 * Created by chandan on 6/12/15.
 */
public class SplashActivity extends AppCompatActivity implements LocationTrackListener {

    private GPSTracker gpsTracker;
    private double latitude;
    private double longitude;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.VISIBLE);

                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                gpsTracker = new GPSTracker(SplashActivity.this);

                if (!statusOfGPS) {
                    gpsTracker.showSettingsAlert();
                }
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            LatLng myLocation = new LatLng(latitude,
                    longitude);
            SearchAddressByLatLng addressByLatLng = new SearchAddressByLatLng(
                    SplashActivity.this);
            addressByLatLng.execute(myLocation);
        }

    }

    @Override
    public void respondCurrentLocationAddress(Address address, LatLng userLocation) {
        progressBar.setVisibility(View.GONE);
        saveAddressAndLaunchLandingScreen(address, userLocation);
    }

    public void saveAddressAndLaunchLandingScreen(Address address, LatLng location) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    public void GpsCancelled() {
        progressBar.setVisibility(View.GONE);
        showPopupForTakeAddress();
    }

    @Override
    public void searchLatLngByAddressTaskComplete(Address address, int responseCode) {

    }

    public void showPopupForTakeAddress() {
        LayoutInflater inflater = (LayoutInflater)
                this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View popupView = inflater.inflate(R.layout.address_popup_layout, null, false);

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true);

        final EditText inputText = (EditText) popupView.findViewById(R.id.user_address);

        TextView submit_btn = (TextView) popupView.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressStr = inputText.getText().toString();

                if (addressStr == null || addressStr.equals(""))
                    Toast.makeText(getApplicationContext(), "Please enter the address to proceed.", Toast.LENGTH_LONG).show();
                else {
                    Address userAddress = new Address(Locale.US);
                    JustOrderStateMachine.setUserAddress(getApplicationContext(), addressStr);
                    saveAddressAndLaunchLandingScreen(userAddress, null);
                }

            }
        });

        TextView cancel_btn = (TextView) popupView.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finish();
            }
        });


        // The code below assumes that the root container has an id called 'main'
        popupWindow.showAtLocation(this.findViewById(R.id.main), Gravity.CENTER, 0, 0);

    }

}
