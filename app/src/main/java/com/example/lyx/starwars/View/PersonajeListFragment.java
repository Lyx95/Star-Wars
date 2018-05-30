package com.example.lyx.starwars.View;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;

import com.example.lyx.starwars.ViewModel.PersonajeNamesViewModel;
import com.example.lyx.starwars.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyx on 3/20/18.
 */

public class PersonajeListFragment extends Fragment implements
        AdapterView.OnItemClickListener,ListItemOnClickInterface {

    private static final String DESCRIPTION_ARG = "description";

    private Callbacks mCallback;
    public PersonajeNamesViewModel personajeNamesViewModel;
    PersonajeListAdapter personajeListAdapter;
    private String peli;
    private EditText inputSearch;
    private RecyclerView recyclerView;
    ArrayList<String> personajes;

    public static PersonajeListFragment newInstance(String peli) {

        PersonajeListFragment fragment = new PersonajeListFragment();

        Bundle args=new Bundle();
        args.putString("peli",peli);
        fragment.setArguments(args);

        return fragment;
    }

    public PersonajeListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        peli=args.getString("peli");

        View rootView;
        rootView = inflater.inflate(R.layout.personaje_list_fragment,
                container, false);

        // Inicializar el adaptador
        personajeListAdapter=new PersonajeListAdapter(getContext());

        //View model
        personajeNamesViewModel = ViewModelProviders.of(this).get(PersonajeNamesViewModel.class);
        //Observador en los datos del viewmodel
        personajeNamesViewModel.getPersonajeNames(peli).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> personajes) {
                personajeListAdapter.setNames(personajes);
            }
        });

        //Crea Recycler View
        recyclerView=(RecyclerView) rootView.findViewById(R.id.listaPers);
        recyclerView.setAdapter(personajeListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Edit text para hacer busquedas filtradas por nombre
        inputSearch = (EditText) rootView.findViewById(R.id.inputSearch);
        //Manejador de eventos del edit text
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                personajeListAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) {}
        });

        return rootView;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String name = (String) parent.getItemAtPosition(position);

        Intent intent = new Intent(getContext(), PersonajeDetailsActivity.class);
        intent.putExtra(PersonajeDetailsActivity.DESCRIPTION, name);
        startActivity(intent);

        mCallback.onPersonajeSelected(name);
    }

    @Override
    public void onItemClick(String name) {

        Intent intent = new Intent(getContext(), PersonajeDetailsActivity.class);
        intent.putExtra(PersonajeDetailsActivity.DESCRIPTION, name);
        startActivity(intent);

        mCallback.onPersonajeSelected(name);
    }


    public interface Callbacks {
        public void onPersonajeSelected(String personaje);
    }


    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        try {
            mCallback = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement Callbacks");
        }
    }

    public void filtraPeli(String peli) {
        this.peli=peli;
        recyclerView.removeAllViews();
        personajeListAdapter.notifyDataSetChanged();
    }

}