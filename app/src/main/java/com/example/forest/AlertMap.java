package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;




import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgba;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

import android.os.Bundle;
import android.util.Log;

public class AlertMap extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_alert_map);

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_map);

        // Initialize the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                map = mapboxMap;

                mapboxMap.setStyle(Style.SATELLITE_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        initalertLayer(style);
                    }
                });
            }
        });
    }

    private void initalertLayer(@NonNull Style style) {
        // h<key,value> --> key = animal_type --> value = List[animal id]
        Intent intent = getIntent();
        Bundle b = getIntent().getExtras();
        double lat = b.getDouble("latitude");
        double lon = b.getDouble("longitude");

        int d_alert = 0;
        d_alert = getResources().getIdentifier("i_elephant", "drawable", getPackageName());
        style.addImage("icon0", BitmapFactory.decodeResource(this.getResources(), d_alert));
        style.addSource(new GeoJsonSource("sid_alert"));
        style.addLayer(new SymbolLayer("layer-id", "sid_alert").withProperties(
                iconImage("icon0"),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconSize(.02f)
        ));
        GeoJsonSource alertSource = map.getStyle().getSourceAs("sid_alert");
        if (alertSource != null) {
            alertSource.setGeoJson(FeatureCollection.fromFeature(
                    Feature.fromGeometry(Point.fromLngLat(lon,lat))
            ));
        }
    }
}