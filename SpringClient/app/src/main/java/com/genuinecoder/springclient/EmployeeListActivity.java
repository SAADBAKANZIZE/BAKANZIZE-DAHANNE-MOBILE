package com.genuinecoder.springclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.genuinecoder.springclient.adapter.EmployeeAdapter;
import com.genuinecoder.springclient.adapter.PharmacieAdapter;
import com.genuinecoder.springclient.model.Employee;
import com.genuinecoder.springclient.model.Pharmacie;
import com.genuinecoder.springclient.model.Zone;
import com.genuinecoder.springclient.reotrfit.EmployeeApi;
import com.genuinecoder.springclient.reotrfit.RetrofitService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeListActivity extends AppCompatActivity {

  private RecyclerView recyclerView;

    private Spinner spinner_zones, spinner_villes;

    Button search, search_garde_nuit, search_garde_jour;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee_list);

      search = findViewById(R.id.search);
      spinner_zones = findViewById(R.id.spinner_zones);
      spinner_villes = findViewById(R.id.spinner_villes);
      search_garde_jour  = findViewById(R.id.search_garde_jour);
      search_garde_nuit = findViewById(R.id.search_garde_nuit);

      loadVilles();

      search.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
           loadPharmaciesByZoneVille(spinner_zones.getSelectedItem().toString(), spinner_villes.getSelectedItem().toString());
          }
      });

      search_garde_jour.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              loadPharmaciesByZoneVilleGardeJour(spinner_zones.getSelectedItem().toString(), spinner_villes.getSelectedItem().toString());

          }
      });

      search_garde_nuit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              loadPharmaciesByZoneVilleGardeNuit(spinner_zones.getSelectedItem().toString(), spinner_villes.getSelectedItem().toString());

          }
      });


    recyclerView = findViewById(R.id.employeeList_recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
      spinner_villes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              loadZone(spinner_villes.getSelectedItem().toString());
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
      });


  }

  private void loadEmployees() {
    RetrofitService retrofitService = new RetrofitService();
    EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
    employeeApi.getAllEmployees()
        .enqueue(new Callback<List<Employee>>() {
          @Override
          public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
            populateListView(response.body());
          }

          @Override
          public void onFailure(Call<List<Employee>> call, Throwable t) {
            Toast.makeText(EmployeeListActivity.this, "Failed to load employees", Toast.LENGTH_SHORT).show();
          }
        });
  }

  private void loadPharmacies() {
    RetrofitService retrofitService = new RetrofitService();
    EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
    employeeApi.findAll()
            .enqueue(new Callback<List<Pharmacie>>() {
              @Override
              public void onResponse(Call<List<Pharmacie>> call, Response<List<Pharmacie>> response) {
                Toast.makeText(EmployeeListActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                System.out.println("success");
                  populateListViewPharmacie(response.body());
              }

              @Override
              public void onFailure(Call<List<Pharmacie>> call, Throwable t) {
                Toast.makeText(EmployeeListActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                  System.out.println("error is" + t);
              }
            });
  }


    private void loadZone(String ville) {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findAllZones(ville)
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        Toast.makeText(EmployeeListActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateListViewZones(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Toast.makeText(EmployeeListActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }
    private void loadVilles() {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findAllVilles()
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        Toast.makeText(EmployeeListActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateListViewVilles(response.body());
                        loadZone(spinner_villes.getSelectedItem().toString());
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Toast.makeText(EmployeeListActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }

    private void loadPharmaciesByZoneVille(String zone, String ville) {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findByZoneVille(zone, ville)
                .enqueue(new Callback<List<Pharmacie>>() {
                    @Override
                    public void onResponse(Call<List<Pharmacie>> call, Response<List<Pharmacie>> response) {
                        Toast.makeText(EmployeeListActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateListViewPharmacie(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Pharmacie>> call, Throwable t) {
                        Toast.makeText(EmployeeListActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }
    private void loadPharmaciesByZoneVilleGardeJour(String zone, String ville) {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findByZoneVilleJour(zone, ville)
                .enqueue(new Callback<List<Pharmacie>>() {
                    @Override
                    public void onResponse(Call<List<Pharmacie>> call, Response<List<Pharmacie>> response) {
                        Toast.makeText(EmployeeListActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateListViewPharmacie(response.body());

                    }

                    @Override
                    public void onFailure(Call<List<Pharmacie>> call, Throwable t) {
                        Toast.makeText(EmployeeListActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }
    private void loadPharmaciesByZoneVilleGardeNuit(String zone, String ville) {
        RetrofitService retrofitService = new RetrofitService();
        EmployeeApi employeeApi = retrofitService.getRetrofit().create(EmployeeApi.class);
        employeeApi.findByZoneVilleNuit(zone, ville)
                .enqueue(new Callback<List<Pharmacie>>() {
                    @Override
                    public void onResponse(Call<List<Pharmacie>> call, Response<List<Pharmacie>> response) {
                        Toast.makeText(EmployeeListActivity.this, "successfully connected", Toast.LENGTH_SHORT).show();
                        System.out.println("success");
                        populateListViewPharmacie(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Pharmacie>> call, Throwable t) {
                        Toast.makeText(EmployeeListActivity.this, "Failed to load pharmacie", Toast.LENGTH_SHORT).show();
                        System.out.println("error is" + t);
                    }
                });
    }

  private void populateListView(List<Employee> employeeList) {
    EmployeeAdapter employeeAdapter = new EmployeeAdapter(employeeList);
    recyclerView.setAdapter(employeeAdapter);
  }

    private void populateListViewPharmacie(List<Pharmacie> pharmacieList) {
        PharmacieAdapter pharmacieAdapter = new PharmacieAdapter(pharmacieList);
        recyclerView.setAdapter(pharmacieAdapter);
    }

    private void populateListViewZones(List<String> zoneList) {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,zoneList);
        spinner_zones.setAdapter(stringArrayAdapter);
    }

    private void populateListViewVilles(List<String> zoneList) {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_item,zoneList);
        spinner_villes.setAdapter(stringArrayAdapter);
    }

  @Override
  protected void onResume() {
    super.onResume();
    loadPharmacies();
  }
}