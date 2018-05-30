package com.example.lyx.starwars.View;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lyx.starwars.Model.Detail;
import com.example.lyx.starwars.Model.Personaje;
import com.example.lyx.starwars.ViewModel.PersonajeNamesViewModel;
import com.example.lyx.starwars.R;

import java.util.ArrayList;

/**
 * Created by lyx on 3/20/18.
 */

public class PersonajeDetailsFragment extends Fragment {
    private static final String DESCRIPTION_ARG = "personaje";
    private PersonajeNamesViewModel personajeNamesViewModel2;
    private PersonajeNamesViewModel personajeNamesViewModel;
    private LiveData<Detail> oSpecie;
    private PersonajeDetailsFragment lcOwner;

    public static PersonajeDetailsFragment newInstance(String name) {

        PersonajeDetailsFragment fragment = new PersonajeDetailsFragment();

        Bundle args = new Bundle();
        args.putString(DESCRIPTION_ARG, name);
        fragment.setArguments(args);

        return fragment;
    }


    public PersonajeDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personajeNamesViewModel2 = ViewModelProviders.of(this).get(PersonajeNamesViewModel.class);
        personajeNamesViewModel = ViewModelProviders.of(this).get(PersonajeNamesViewModel.class);
        lcOwner = this;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView;
        rootView = inflater.inflate(R.layout.personaje_details_fragment, container, false);

        // Si estamos restaurando desde un estado previo no hacemos nada
        if (savedInstanceState != null) {
            return rootView;
        }

        final Bundle args = getArguments();
        final TextView tvName = (TextView) rootView.findViewById(R.id.tvName);
        final TextView tvMass = (TextView) rootView.findViewById(R.id.tvMass);
        final TextView tvHeigth = (TextView) rootView.findViewById(R.id.tvHeight);
        final TextView tvHair = (TextView) rootView.findViewById(R.id.tvHair);
        final TextView tvSkin = (TextView) rootView.findViewById(R.id.tvSkin);
        final TextView tvEye = (TextView) rootView.findViewById(R.id.tvEyes);
        final TextView tvBirth = (TextView) rootView.findViewById(R.id.tvBirth);
        final TextView tvGender = (TextView) rootView.findViewById(R.id.tvGender);
        final TextView tvHomeword = (TextView) rootView.findViewById(R.id.tvHomeword);
        final TextView tvFilms = (TextView) rootView.findViewById(R.id.tvFilms);
        final TextView tvSpecies = (TextView) rootView.findViewById(R.id.tvSpecies);
        final TextView tvVehicles = (TextView) rootView.findViewById(R.id.tvVehicles);
        final TextView tvStarships = (TextView) rootView.findViewById(R.id.tvStarships);

        /*

    int height;
    int mass;
    String hair_color;
    String skin_color;
    String eye_color;
    String birth_year;
    String gender;
    String homeworld;
    ArrayList<String> films;
    String species;
    ArrayList<String> vehicles;
    ArrayList<String> starships;
         */

        if (args != null) {
            String name = args.getString(DESCRIPTION_ARG);
            personajeNamesViewModel.getPersonajeByName(name).observe(this, new Observer<Personaje>() {
                @Override
                public void onChanged(@Nullable Personaje personaje) {

                    tvName.setText(String.format("%s: %s", getString(R.string.name), personaje.getName()));
                    tvHeigth.setText(String.format("%s: %s",getString(R.string.heigth), personaje.getHeight()));
                    tvMass.setText(String.format("%s: %s",getString(R.string.mass), personaje.getMass()));
                    tvHair.setText(String.format("%s: %s",getString(R.string.hair), personaje.getHair_color()));
                    tvSkin.setText(String.format("%s: %s",getString(R.string.skin), personaje.getSkin_color()));
                    tvEye.setText(String.format("%s: %s",getString(R.string.eye), personaje.getEye_color()));
                    tvBirth.setText(String.format("%s: %s",getString(R.string.birth), personaje.getBirth_year()));
                    tvGender.setText(String.format("%s: %s",getString(R.string.gender), personaje.getGender()));
                    tvFilms.setText(R.string.films);
                    tvVehicles.setText(R.string.vehicles);
                    tvStarships.setText(R.string.stars);

                    String specie = personaje.getSpecies();

                    if(!specie.equals("")){
                        oSpecie=personajeNamesViewModel2.getDetailByUrl(specie);
                        oSpecie.observe(lcOwner, new Observer<Detail>() {
                            @Override
                            public void onChanged(@Nullable Detail detail) {
                                tvSpecies.setText(String.format("%s: %s", getString(R.string.specie), detail.getName()));
                            }
                        });
                    }
                    else{
                        tvSpecies.setText(String.format("%s: %s", getString(R.string.specie), getString(R.string.unknown)));
                    }

                    String home = personaje.getHomeworld();

                    personajeNamesViewModel2.getDetailByUrl(home).observe(lcOwner, new Observer<Detail>() {
                        @Override
                        public void onChanged(@Nullable Detail detail) {
                            tvHomeword.setText(String.format("%s: %s",getString(R.string.home), detail.getName()));
                        }
                    });

                    final ArrayList<String> vehicles=personaje.getVehicles();

                    //Crea Lista
                    LinearLayout listaV = (LinearLayout) rootView.findViewById(R.id.listV);
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    listaV.removeAllViews();
                    for (String v:vehicles) {
                        View view  = inflater.inflate(android.R.layout.simple_list_item_1, listaV, false);
                        // set item content in view
                        final TextView t =view.findViewById(android.R.id.text1);

                        personajeNamesViewModel2.getDetailByUrl(v).observe(lcOwner, new Observer<Detail>() {
                            @Override
                            public void onChanged(@Nullable Detail detail) {
                                t.setText(detail.getName());
                            }
                        });
                        listaV.addView(view);
                    }

                    final ArrayList<String> star=personaje.getStarships();
                    //Crea Lista
                    LinearLayout listaS = (LinearLayout) rootView.findViewById(R.id.listS);
                    inflater = LayoutInflater.from(getActivity());
                    listaS.removeAllViews();
                    for (String s:star) {
                        View view  = inflater.inflate(android.R.layout.simple_list_item_1, listaS, false);
                        // set item content in view
                        final TextView t =view.findViewById(android.R.id.text1);

                        personajeNamesViewModel2.getDetailByUrl(s).observe(lcOwner, new Observer<Detail>() {
                            @Override
                            public void onChanged(@Nullable Detail detail) {
                                t.setText(detail.getName());
                            }
                        });
                        listaS.addView(view);
                    }

                    ArrayList<String> films = personaje.getFilms();
                    for(int i=0;i<films.size();i++) {
                        String f;
                        switch (films.get(i)) {
                            case "1":
                                f = getString(R.string.peli1);
                                break;
                            case "2":
                                f = getString(R.string.peli2);
                                break;
                            case "3":
                                f = getString(R.string.peli3);
                                break;
                            case "4":
                                f = getString(R.string.peli4);
                                break;
                            case "5":
                                f = getString(R.string.peli5);
                                break;
                            case "6":
                                f = getString(R.string.peli6);
                                break;
                            case "7":
                                f = getString(R.string.peli7);
                                break;
                            default:
                                f="Unknown";
                                break;

                        }

                        films.set(i,f);

                    }

                    //Crea Lista
                    LinearLayout listaPelis = (LinearLayout) rootView.findViewById(R.id.listF);
                    inflater = LayoutInflater.from(getActivity());
                    listaPelis.removeAllViews();
                    for (String film:films) {
                        View view  = inflater.inflate(android.R.layout.simple_list_item_1, listaPelis, false);
                        // set item content in view
                        TextView t =view.findViewById(android.R.id.text1);
                        t.setText(film);
                        listaPelis.addView(view);
                    }

                }
            });
        }

        return rootView;
    }

}
