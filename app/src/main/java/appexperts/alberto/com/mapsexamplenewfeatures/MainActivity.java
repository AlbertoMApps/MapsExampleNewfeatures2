package appexperts.alberto.com.mapsexamplenewfeatures;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends android.support.v4.app.FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMaps;
    private SupportMapFragment mapFragment;
    private final LatLng me = new LatLng(51.52, 0.020);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMaps = googleMap;
        googleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMaps.addMarker(new MarkerOptions()
                .position(me)
                .title("me")
                .snippet("HOLAA")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.common_full_open_on_phone)));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 12));

        //opciones avanzadas de camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .zoom(17)
                .target(me) //centramos el mapa en las coordenadas
                .bearing(90) //Establecemos la orientacion al norte
                .tilt(90) //bajamos el punto de vita de la camara a 90 grados
                .build();
//        CameraUpdate cameraUpdate = new CameraUpdateFactory.newCameraPosition(cameraPosition);
//        googleMaps.animateCamera(cameraUpdate);

        //marker que interactua con el usuario...
        googleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "El usuario ha pulsado sobre: " + marker.getTitle(), Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        //ClickListeners..
        googleMaps.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Projection proj = googleMaps.getProjection();
                Point coord = proj.toScreenLocation(point);
                Toast.makeText(getApplicationContext(), "El usuario ha pulsado en: \n" + " latitud " + point.latitude + " longitud " + point.longitude, Toast.LENGTH_SHORT).show();
            }
        });

        googleMaps.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                Projection proj = googleMaps.getProjection(); //Objeto que nos permite convertir las coordenadas fisicas en coordenadas de la pantalla
                Point coord = proj.toScreenLocation(point);
                Toast.makeText(getApplicationContext(), "El usuario ha realizado una pulsacion LARGA en: \n" + " latitud " + point.latitude + " longitud " + point.longitude + " coordinadas: " + coord.x + " : " + coord.y, Toast.LENGTH_SHORT).show();
            }
        });

        //onCameraChangeListener
        googleMaps.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                //Toast.makeText(getApplicationContext(), "Desplazamiento de la camara por el usuario: \n" + " latitud " + cameraPosition.target.latitude + "\n longitud " + cameraPosition.target.longitude + "\n nivel de zoom: " + cameraPosition.zoom + "\n Orientacion : " + cameraPosition.bearing + "\n Angulo de vision : " + cameraPosition.tilt, Toast.LENGTH_SHORT).show();
            }
        });

        //Anadir rutas al mapa
        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(me.latitude + 0.05, me.longitude - 0.05))
                .add(new LatLng(me.latitude + 0.05, me.longitude + 0.05));
        //Grosor y color de linea
        lineas.width(10);
        lineas.color(Color.RED);

        googleMaps.addPolyline(lineas);

        //Mas polylines//...., yo hago un cuadrado externo....

        PolylineOptions lineas2 = new PolylineOptions()
                .add(new LatLng(me.latitude + 0.05, me.longitude - 0.05))
                .add(new LatLng(me.latitude + 0.05, me.longitude + 0.05))
                .add(new LatLng(me.latitude - 0.05, me.longitude + 0.05))
                .add(new LatLng(me.latitude - 0.05, me.longitude - 0.05))
                .add(new LatLng(me.latitude + 0.05, me.longitude - 0.05));
        //Grosor y color de linea
        lineas.width(10);
        lineas.color(Color.RED);

        googleMaps.addPolyline(lineas2);

        //Activamos la geolocalizacion, es muy facil con V2.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMaps.setMyLocationEnabled(true);

        //listener to listen the changes of my position....
        googleMaps.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location pos) {
                //latitude and longitud of what its listening on my position..
                double lat = pos.getLatitude();
                double lon = pos.getLongitude();
                //Move the camera there...
                //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng( lat,lon) );
                //Place the camera between the 2 locations
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng( lat,(lon + me.longitude)/2) );
                googleMaps.moveCamera(cameraUpdate);
                //I draw the route
                PolylineOptions lines = new PolylineOptions()
                        .add(new LatLng(me.latitude, me.longitude))
                        .add(new LatLng(lat,lon));
                //Grosor y color de linea
                lines.width(10);
                lines.color(Color.RED);
                googleMaps.addPolyline(lines);
                //calculate the distances between lines
                float[] results = new float[1];
                Location.distanceBetween(lat,lon, me.latitude,me.longitude,results);
                Toast.makeText(getApplicationContext(), "Distance between me and the point near me from before: \n" + results[0]/1000 + "Km", Toast.LENGTH_SHORT).show();
            }
        });

        //GPS FEATURES...
        //just speciify from URL, from where to where...
        String uriNavigation = "http://maps.google.com/maps?saddr="+51.492277+","+(-0.161457)+"&daddr="+me.latitude+","+me.longitude+"";
        //we send the information from my activity to googlemaps within an intent....
        Intent navigation = new Intent (Intent.ACTION_VIEW, Uri.parse(uriNavigation));
        //launch google navigation..
        startActivity(navigation);
    /**
     * This shows how to create a simple activity with streetview
     */
        SupportStreetViewPanoramaFragment streetViewPanoramaFragment = (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                        // Only set the panorama to ME on startup (when no panoramas have been
                        // loaded which is when the savedInstanceState is null).  You can comment any part to disable the streetView...
                        //if (savedInstanceState == null) {
                            panorama.setPosition(me);
                        //}
                    }
                });
    }
}
