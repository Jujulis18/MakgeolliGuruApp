package com.myapp.makgeolliguru.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.myapp.makgeolliguru.tools.DataManager;
import com.myapp.makgeolliguru.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {

    private DataManager dataManager;
    private MapManager mapManager;

    public MapFragment() {
        // require a empty public constructor
    }

    public static String getCurrentLanguage() {
        Locale currentLocale = Locale.getDefault();
        return currentLocale.getLanguage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        View bottomSheetView = inflater.inflate(R.layout.modal_bottom_sheet, container, false);

        dataManager = new DataManager(getActivity().getApplicationContext());
        mapManager = new MapManager();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                List<String[]> makgeolliListTab = dataManager.loadMakgeolliList();
                List<String[]> favoriteList = dataManager.loadFavoriteList();
                mapManager.initializeMap(googleMap, container, makgeolliListTab, favoriteList, bottomSheetView);

            });
        }
        setupListeners(view, container, inflater);

        return view;
    }

    private void setupListeners(View view, ViewGroup container, LayoutInflater inflater) {


        view.findViewById(R.id.btnFilter).setOnClickListener (v -> {
            View actionContainer = view.findViewById(R.id.filter_container);
            View infoContainer = view.findViewById(R.id.info_container);
            // Vérifier la visibilité et inverser l'état
            if (actionContainer.getVisibility() == View.GONE) {
                if (infoContainer.getVisibility() == View.VISIBLE) {
                    infoContainer.setVisibility(View.GONE);
                }
                actionContainer.setVisibility(View.VISIBLE);
            } else {
                actionContainer.setVisibility(View.GONE);
            }
        });

        // Update data
        view.findViewById(R.id.updatebtn).setOnClickListener(v -> {
            dataManager.updateDataFromAPI();
            Toast.makeText(container.getContext(), "Refresh the page, please", Toast.LENGTH_SHORT).show();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(MapFragment.this).attach(MapFragment.this).commit();
        });

        view.findViewById(R.id.infobtn).setOnClickListener (v -> {
            View actionContainer = view.findViewById(R.id.info_container);
            View filterContainer = view.findViewById(R.id.filter_container);
            // Vérifier la visibilité et inverser l'état
            if (actionContainer.getVisibility() == View.GONE) {
                if (filterContainer.getVisibility() == View.VISIBLE) {
                    filterContainer.setVisibility(View.GONE);
                }
                actionContainer.setVisibility(View.VISIBLE);
            } else {
                actionContainer.setVisibility(View.GONE);
            }
        });

        // Filter
        ChipGroup chipGroup = view.findViewById(R.id.filter);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Handle filter change
        });

        view.findViewById(R.id.chipFilter).setOnClickListener(v -> {
            mapManager.filterMarkers(chipGroup, dataManager.loadMakgeolliList(), dataManager.loadFavoriteList());
        });

        view.findViewById(R.id.chipClear).setOnClickListener(v -> {
            chipGroup.clearCheck();
            EditText searchBar = view.findViewById(R.id.search_bar);
            searchBar.getText().clear();
            mapManager.filterMarkers(chipGroup, dataManager.loadMakgeolliList(), dataManager.loadFavoriteList());
        });



    }



    @Override
    public void onResume() {
        super.onResume();
//        if (mapView != null) {
//            mapView.onResume();
//        }
    }

}

////////////////////////////////////////

/*


    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}
*/
