package io.github.wu3976.csci571hw9;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
    public List<String> objects;
    public AutocompleteAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.objects = objects;

    }

    public AutocompleteAdapter(@NonNull Context context, int resource) {
        this(context, resource, new ArrayList<String>());
    }

    public AutocompleteAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public void add(String str) {
        objects.add(str);
    }

    @Override
    public void clear() {
        objects.clear();
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults fr = new FilterResults();
                fr.values = objects;
                fr.count = objects.size();
                return fr;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }
}
