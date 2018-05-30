package com.example.lyx.starwars.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lyx.starwars.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyx on 4/25/18.
 */

public class PersonajeListAdapter
        extends RecyclerView.Adapter<PersonajeListAdapter.PersonajeViewHolder> implements Filterable {

    private final LayoutInflater mlayoutInflater;
    private List<String> mpersonajes;
    private List<String> items;
    private ListItemOnClickInterface listItemOnClickInterface;
    private boolean filtered=false;


    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults result = new FilterResults();
                List<String> allJournals = mpersonajes;
                if(constraint == null || constraint.length() == 0){
                    filtered=false;
                    items=mpersonajes;
                    result.values = allJournals;
                    result.count = allJournals.size();
                }else{
                    filtered=true;
                    ArrayList<String> filteredList = new ArrayList<String>();
                    for(String j: allJournals){
                        if(j.toLowerCase().contains(constraint.toString().toLowerCase()))
                            filteredList.add(j);
                    }
                    result.values = filteredList;
                    result.count = filteredList.size();
                }

                return result;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    public class PersonajeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView name;

        public PersonajeViewHolder(View itemView) {
            super(itemView);
            name=(TextView) itemView.findViewById(R.id.tvNameList);
            //Mostrar un icono segun preferencias
            // load the XML preferences file
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(itemView.getContext());
            Boolean darkSide = preferences.getBoolean("dark_side_preference",false);
            ImageView icon=(ImageView) itemView.findViewById(R.id.imageView);
            if(darkSide)
                icon.setImageResource(R.drawable.ic_imperial);
            else
                icon.setImageResource(R.drawable.ic_jedi);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listItemOnClickInterface.onItemClick(this.name.getText().toString());
        }
    }

    public PersonajeListAdapter(Context context) {
        this.mlayoutInflater = LayoutInflater.from(context);
        if (context instanceof ListItemOnClickInterface)
            listItemOnClickInterface = (ListItemOnClickInterface) context;
    }

    public void setNames(List<String> mpersonajes) {
        this.mpersonajes = mpersonajes;
        if(!filtered)
            this.items = mpersonajes;
        notifyDataSetChanged();
    }

    @Override
    public PersonajeListAdapter.PersonajeViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View rowView = mlayoutInflater.inflate(R.layout.list_item_personaje, parent, false);
        return new PersonajeViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(PersonajeListAdapter.PersonajeViewHolder holder,
                                 int position) {
        if(items.size()>0 && items.contains(items.get(position)))
            holder.name.setText(items.get(position));
        else
            holder.name.setText("");
    }

    @Override
    public int getItemCount() {
        if(items!=null)
            return items.size();
        else return 0;
    }
}