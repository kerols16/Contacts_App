package com.example.contects.view;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.example.contects.R;

public class SearchFragment extends Fragment {

    private OnSearchListener searchListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragent_search, container, false);

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchListener != null) {
                    searchListener.onSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchListener != null) {
                    searchListener.onSearch(newText);
                }
                return true;
            }
        });

        return view;
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.searchListener = listener;
    }

    public interface OnSearchListener {
        void onSearch(String query);
    }
}