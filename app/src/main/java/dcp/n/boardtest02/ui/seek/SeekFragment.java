package dcp.n.boardtest02.ui.seek;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dcp.n.boardtest02.R;
import dcp.n.boardtest02.ReadActivity;

public class SeekFragment extends Fragment {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private SeekBoardAdapter adapter;
    private List<SeekItemModel> list;

    private SeekViewModel mViewModel;

    public static SeekFragment newInstance() {
        return new SeekFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_seek, container, false);

        recyclerView = root.findViewById(R.id.recyclerview_board);

        list = new ArrayList<>();
        firestore.collection("board").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                list = new ArrayList<>();
                for(DocumentChange documentChange: queryDocumentSnapshots.getDocumentChanges()) {
                    QueryDocumentSnapshot document = documentChange.getDocument();
                    String id = (String) document.getData().get("id");
                    String title = (String) document.getData().get("title");
                    String contents = (String) document.getData().get("contents");
                    String name = (String) document.getData().get("name");

                    list.add(new SeekItemModel(id, title, contents, name));
                    Log.d("WriteActivity_firebase", document.getId() + " => " + document.getData());
                }
                adapter = new SeekBoardAdapter(list);
                recyclerView.setAdapter(adapter);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SeekViewModel.class);
        // TODO: Use the ViewModel
    }

    private class SeekBoardAdapter extends RecyclerView.Adapter<SeekBoardAdapter.SeekViewHolder> {

        private List<SeekItemModel> modelList;

        public SeekBoardAdapter(List<SeekItemModel> modelList) {
            this.modelList = modelList;
        }

        @NonNull
        @Override
        public SeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SeekViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seek_board_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SeekViewHolder holder, int position) {
            SeekItemModel data = modelList.get(position);
            holder.txtTitle.setText(data.getTitle());
            holder.txtContents.setText(data.getContents());
            holder.txtName.setText(data.getName());
        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        class SeekViewHolder extends RecyclerView.ViewHolder {

            private TextView txtTitle;
            private TextView txtContents;
            private TextView txtName;

            public SeekViewHolder(@NonNull View itemView) {
                super(itemView);

                txtTitle = itemView.findViewById(R.id.item_board_title);
                txtContents = itemView.findViewById(R.id.item_board_contents);
                txtName = itemView.findViewById(R.id.item_board_name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = txtTitle.getText().toString();
                        String contents = txtContents.getText().toString();
                        String name = txtName.getText().toString();

                        Intent intent = new Intent(getActivity(), ReadActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("contents", contents);
                        intent.putExtra("name", name);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}