package com.jongewaard.dev.duckhunt.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jongewaard.dev.duckhunt.Models.User;
import com.jongewaard.dev.duckhunt.R;

import java.util.ArrayList;
import java.util.List;

public class UserRankingFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    RecyclerView recyclerView;

    List<User> userList;

    MyUserRecyclerViewAdapter adapter;
    FirebaseFirestore db;




    public UserRankingFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserRankingFragment newInstance(int columnCount) {
        UserRankingFragment fragment = new UserRankingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        db = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            //Accediendo a los datos de Firebasefirestore
            db.collection("users")
                    .orderBy("duck", Query.Direction.DESCENDING) //ordenando los datos
                    .limit(10)                             //limite de 10 puntuaciones
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            userList = new ArrayList<>();
    /**Recorrro con un for enriquecido, el objeto task que recibo por el
       evento oncomplete voy a obtener la lista de resultados, y por cada resultado
       voy a incluirlo como un elemento que es un UserItem de la Lista, voy por
       tanto a obtener un objeto document y lo voy a transformar de tipo User.class
        De manera que voy a añadir a nuestro UserList un elemento de tipo User
    **/
                            for(DocumentSnapshot document:task.getResult()){

                                User userItem = document.toObject(User.class);
                                userList.add(userItem);

                                adapter = new MyUserRecyclerViewAdapter(userList);

                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });


        }
        return view;
    }





}
