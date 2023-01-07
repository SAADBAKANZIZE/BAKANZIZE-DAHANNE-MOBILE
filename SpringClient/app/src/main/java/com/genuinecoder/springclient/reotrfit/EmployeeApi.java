package com.genuinecoder.springclient.reotrfit;

import com.genuinecoder.springclient.model.Employee;
import com.genuinecoder.springclient.model.Pharmacie;
import com.genuinecoder.springclient.model.Zone;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EmployeeApi {

  @GET("/employee/get-all")
  Call<List<Employee>> getAllEmployees();

  @GET("/pharmacies/all")
  Call<List<Pharmacie>> findAll();

  @GET("/pharmacies/byZoneString/{zone}")
  Call<List<Pharmacie>> findByZone(@Path("zone") String zone);

  @GET("/pharmacies/byZoneVilleString/{zone}/{ville}")
  Call<List<Pharmacie>> findByZoneVille(@Path("zone") String zone, @Path("ville") String ville);
  @GET("/zones/allZones/{ville}")
  Call<List<String>> findAllZones(@Path("ville") String ville);
  @GET("/ville/allVilles")
  Call<List<String>> findAllVilles();

  @POST("/employee/save")
  Call<Employee> save(@Body Employee employee);

  @GET("/pharmacieGarde/gardeNuitbyZoneVille/{zone}/{ville}")
  Call<List<Pharmacie>> findByZoneVilleNuit(@Path("zone") String zone, @Path("ville") String ville);

  @GET("/pharmacieGarde/gardeJourbyZoneVille/{zone}/{ville}")
  Call<List<Pharmacie>> findByZoneVilleJour(@Path("zone") String zone, @Path("ville") String ville);

  @GET("/pharmacies/byLatLong/{zone}/{ville}/{lat}/{lon}")
  Call <Pharmacie> findMinDistance(@Path("zone") String zone, @Path("ville") String ville, @Path("lat") double lat, @Path("lon") double lon);

  @GET("/pharmacieGarde/byLatLongJour/{zone}/{ville}/{lat}/{lon}")
  Call <Pharmacie> findMinDistanceJour(@Path("zone") String zone, @Path("ville") String ville, @Path("lat") double lat, @Path("lon") double lon);

  @GET("/pharmacieGarde/byLatLongNuit/{zone}/{ville}/{lat}/{lon}")
  Call <Pharmacie> findMinDistanceNuit(@Path("zone") String zone, @Path("ville") String ville, @Path("lat") double lat, @Path("lon") double lon);
}
