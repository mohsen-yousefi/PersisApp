package ir.rahcode.persisapp.layouts.FragmentScreens;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import ir.rahcode.persisapp.R;
import ir.rahcode.persisapp.utils.PrefranceHelper;

import static android.os.Looper.getMainLooper;

public class MapFaragment extends Fragment {

    MapView mapView;
    public MapboxMap map;
    Style mapStyle;
LatLng current_location;


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    private void enableLocationComponent() {
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
// Create and customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(getActivity())
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.RED)
                    .build();
// Get an instance of the component
            LocationComponent locationComponent = map.getLocationComponent();
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(getContext(), mapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();
// Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
// Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
// Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
// Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
// Add the location icon click listener
            locationComponent.addOnLocationClickListener(new OnLocationClickListener() {
                @Override
                public void onLocationComponentClick() {
                }
            });
        } else {
            PermissionsManager permissionsManager = new PermissionsManager(new PermissionsListener() {
                @Override
                public void onExplanationNeeded(List<String> permissionsToExplain) {
                }

                @Override
                public void onPermissionResult(boolean granted) {
                    if (granted)
                        enableLocationComponent();

                 }
            });
            permissionsManager.requestLocationPermissions(getActivity());
        }

        if(current_location!=null) {
            zoomTo(current_location);
        }
        getCurentLocation();
    }
    LocationEngine locationEngine;







    public void zoomTo(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 3000);

    }


    public LocationEngineCallback<LocationEngineResult> locationEngineCallback = new LocationEngineCallback<LocationEngineResult>() {
        @Override
        public void onSuccess(LocationEngineResult result) {
            Location lastKnowLocation = result.getLastLocation();
            PrefranceHelper.SetLatLng(lastKnowLocation.getLatitude() + "," + lastKnowLocation.getLongitude());

            if(current_location==null) {
                zoomTo(new LatLng(lastKnowLocation.getLatitude(),lastKnowLocation.getLongitude()));
            }








            map.setMaxZoomPreference(16);


            locationEngine.removeLocationUpdates(locationEngineCallback);
        }

        @Override
        public void onFailure(@NonNull Exception exception) {

        }
    };
        private void getCurentLocation() {
        android.util.Log.i("zzzzz222zzzzz", "onSuccess: ");

        map.getLocationComponent().getLocationEngineRequest();
        LocationComponentOptions locationComponent = LocationComponentOptions.builder(getContext()).build();
        LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions.builder(getContext(), mapStyle).locationComponentOptions(locationComponent).build();
        map.getLocationComponent().activateLocationComponent(locationComponentActivationOptions);
        locationEngine = LocationEngineProvider.getBestLocationEngine(getContext());

        LocationEngineRequest request = new LocationEngineRequest.Builder(1000).setPriority(LocationEngineRequest.PRIORITY_BALANCED_POWER_ACCURACY).build();


        locationEngine.requestLocationUpdates(request, locationEngineCallback, getMainLooper());


        Intent intent = new Intent(getContext(), this.getClass());
        PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        locationEngine.requestLocationUpdates(request, pi);


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] current_location_prefrance = PrefranceHelper.getLatLng().split(",");
        if(PrefranceHelper.getLatLng().isEmpty()){
            current_location = null;
        }else{
            current_location =  new LatLng(Double.parseDouble(current_location_prefrance[0]),Double.parseDouble(current_location_prefrance[1]));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_map_box,container,false);
        mapView =  view.findViewById(R.id.view_map_box_map_view);
mapView.getMapAsync(new OnMapReadyCallback() {
    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        map.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapStyle = style;

                enableLocationComponent();






            }
        });
    }
});

        return  view;
    }
}
