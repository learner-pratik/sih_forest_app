package com.example.forest;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.mapbox.mapboxsdk.Mapbox;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;


import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;




import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgba;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;




public class Prediction extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

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
                        try {
                            initpredLayer(style);
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private void initpredLayer(@NonNull Style style) throws IOException, URISyntaxException {

        GeoJsonSource hood = new GeoJsonSource("hood_layer",new URI("https://forestweb.herokuapp.com/geojson"));
        style.addSource(hood);

        FillLayer hoodArea = new FillLayer("hood-fill", "hood_layer");
        LineLayer boundary = new LineLayer("b-line","hood_layer");
        hoodArea.setProperties(
                fillColor(Expression.match(Expression.get("AREA_SHORT_CODE"),literal(1),rgba(255, 0, 0, 1.0f),literal(2),rgba(240, 255, 0, 1.0f),rgba(0.0f, 255.0f, 0.0f, 1.0f))),
                fillOpacity(0.4f)
        );
        boundary.setProperties(
                lineWidth(3.5f),
                lineColor("#00ffff")
        );
        style.addLayer(hoodArea);
        style.addLayer(boundary);

    }
}