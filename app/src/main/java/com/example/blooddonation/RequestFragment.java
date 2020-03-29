package com.example.blooddonation;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    LocationManager locationManager;
    LocationListener locationListener;
    String current_location = "";
    EditText txt_location,txt_bloodGroup,txt_description;
    Button getLocationbutton,postButton;
    DatabaseReference mDatabse;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        View view = inflater.inflate(R.layout.fragment_request, container, false);
        if(currentUser != null) {
            // Inflate the layout for this fragment
            getLocationbutton = view.findViewById(R.id.getLocation);

            //We use same view inside a fragment
//        View locationview = layoutInflater.inflate(R.layout.fragment_request,container,false);

            txt_location = view.findViewById(R.id.locationText);
            txt_bloodGroup = view.findViewById(R.id.bloodGroup);
            txt_description = view.findViewById(R.id.description);

            postButton = view.findViewById(R.id.postButton);
            mDatabse = FirebaseDatabase.getInstance().getReference();

            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                Log.i("TAG",location.toString());
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

//                            locationManager.removeUpdates(locationListener);
                        if (addressList == null) {
                            Toast.makeText(getContext(), "Cannot get Location", Toast.LENGTH_SHORT).show();
                        } else {
                            current_location = addressList.get(0).getLocality();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };


            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }


            getLocationbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    txt_location.setText(current_location);

                    // To pause locationManager from getting further updates
                    locationManager.removeUpdates(locationListener);

                }
            });


            postButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String bloodGroup = txt_bloodGroup.getText().toString();
                    String description = txt_description.getText().toString();
                    String location = txt_location.getText().toString();
//                if(TextUtils.isEmpty(location)){
//                    Toast.makeText(getContext(), "Enter location", Toast.LENGTH_SHORT).show();
//                }

                    if (TextUtils.isEmpty(bloodGroup) || TextUtils.isEmpty(location) || TextUtils.isEmpty(description)) {
                        Toast.makeText(getContext(), "Field(s) cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        String key = mDatabse.child("posts").push().getKey();
                        Map<String, String> timeStamp = ServerValue.TIMESTAMP;
                        String ts = timeStamp.get(".sv");
//                    Log.i(TAG, "onClick: "+ts);

                        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        Post post = new Post(bloodGroup, location, description, timeStamp, firebaseUser.getUid());

                        Map<String, Object> postMap = post.toMap();
                        mDatabse.child("posts").child(key).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());


                                    Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Post Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            });

        }

        return view;

    }

}
