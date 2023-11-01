package com.example.datingappdev.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datingappdev.MatchActivity;
import com.example.datingappdev.R;
import com.example.datingappdev.adapter.FeedTestCardAdapter;
import com.example.datingappdev.config.FirebaseConfig;
import com.example.datingappdev.helper.CardStackCallback;
import com.example.datingappdev.helper.UserFirebase;
import com.example.datingappdev.model.ItemModel;
import com.example.datingappdev.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragmentTestCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragmentTestCard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Variables for Card:
    private CardStackLayoutManager manager;
    private FeedTestCardAdapter adapter;

    private DatabaseReference usuariosRef;

    private FirebaseUser usuarioAtual;

    private ArrayList<User> listPossibleMatches = new ArrayList<>();

    private ArrayList<ItemModel> listPossibleMatchesItemModel = new ArrayList<>();

    private ValueEventListener valueEventListenerContatos;

    private static final String TAG = "MainActivity";

    private List<ItemModel> items = new ArrayList<>();
    private int n;



    public FeedFragmentTestCard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragmentTestCard.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragmentTestCard newInstance(String param1, String param2) {
        FeedFragmentTestCard fragment = new FeedFragmentTestCard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_feed_test_card, container, false);


        //Testes Puxar dados
        usuariosRef = FirebaseConfig.getFirebaseDatabase().child("users");
        usuarioAtual = UserFirebase.getCurrentUser();

        recuperarContatos();


        CardStackView cardStackView = view.findViewById(R.id.card_stack_view);

        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" +direction.name()
                + "ratio=" + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition()
                + "d=" +direction);

                if(direction == Direction.Right) {
                    Toast.makeText(getContext(), "Direction Right", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getActivity(), MatchActivity.class);
                    startActivity(i);

                }
                if(direction == Direction.Left) {
                    Toast.makeText(getContext(), "Direction Left", Toast.LENGTH_SHORT).show();
                }
                if(direction == Direction.Top) {
                    Toast.makeText(getContext(), "Direction Top", Toast.LENGTH_SHORT).show();
                }
                if(direction == Direction.Bottom) {
                    Toast.makeText(getContext(), "Direction Bottom", Toast.LENGTH_SHORT).show();
                }

                //Paginating
                if(manager.getTopPosition() == adapter.getItemCount() - 5 ) {

                    paginate();

                }
            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: p=" +manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " +manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: "+position+", nama: "+tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {

                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: "+position+", nama: "+tv.getText());

            }
        });
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());
        //adapter = new FeedTestCardAdapter(addList());
        adapter = new FeedTestCardAdapter(items);
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void onStop() {
        super.onStop();
        usuariosRef.removeEventListener(valueEventListenerContatos);
    }

    public void recuperarContatos(){
        valueEventListenerContatos = usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                limparListaContatos();
                //Log.i("Limpou Contatos: " , "1");

                for(DataSnapshot dados: snapshot.getChildren()){

                    //Log.i("Limpou Contatos: " , "2");
                    User usuario = dados.getValue(User.class);
                    String emailUsuarioAtual = usuarioAtual.getEmail();
                    Log.d(TAG, "tag: "+ usuario.getEmail());
                    if(!emailUsuarioAtual.equals(usuario.getEmail())){

                        listPossibleMatches.add(usuario);
                        Log.d(TAG, "tagEntrou: "+ usuario.getEmail());

                    }
                    addList();

                }
                //Log.i("Limpou Contatos: " , "3");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void limparListaContatos(){
        listPossibleMatches.clear();
    }

    private void paginate() {

        //Log.d(TAG, "tag: "+listPossibleMatches.size());

        List<ItemModel> old = adapter.getItems();
        //List<ItemModel> baru = new ArrayList<>(addList());
        List<ItemModel> baru = items;
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);

    }

    private List<ItemModel> addList() {


        //List<ItemModel> items = new ArrayList<>();
        Log.d(TAG, "itemsSize2: "+items.size());

        for ( n = n; n < listPossibleMatches.size(); n ++){

            //items.add(new ItemModel(R.drawable.bluebckground, listPossibleMatches.get(n).getName(), listPossibleMatches.get(n).getAge(), "Belo Horizonte"));
            items.add(new ItemModel(R.drawable.bluebckground, listPossibleMatches.get(n)));

        }

        /*
        items.add(new ItemModel(R.drawable.sample1, "Markonah", "24", "Jember"));
        items.add(new ItemModel(R.drawable.sample2, "Pietro", "20", "Madrid"));
        items.add(new ItemModel(R.drawable.sample3, "Jhon", "17", "New York"));
        items.add(new ItemModel(R.drawable.sample4, "Jubalu", "32", "Africa"));
        items.add(new ItemModel(R.drawable.sample5, "Alexandre", "21", "Belo Horizonte"));

        items.add(new ItemModel(R.drawable.sample1, "Markonah", "24", "Jember"));
        items.add(new ItemModel(R.drawable.sample2, "Pietro", "20", "Madrid"));
        items.add(new ItemModel(R.drawable.sample3, "Jhon", "17", "New York"));
        items.add(new ItemModel(R.drawable.sample4, "Jubalu", "32", "Africa"));
        items.add(new ItemModel(R.drawable.sample5, "Alexandre", "21", "Belo Horizonte"));

         */

        return items;
    }
}