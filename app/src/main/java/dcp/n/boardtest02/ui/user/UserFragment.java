package dcp.n.boardtest02.ui.user;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import dcp.n.boardtest02.R;
import dcp.n.boardtest02.ReadActivity;

public class UserFragment extends Fragment {
    public static final String TAG = "logT";

    private FirebaseAuth mAuth = null;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<UserItemModel> list;

    private UserViewModel mViewModel;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root;
        mAuth = FirebaseAuth.getInstance();
        root = inflater.inflate(R.layout.fragment_user, container, false);
        FirebaseUser user = mAuth.getCurrentUser();
        setUser(root, user);


        recyclerView = root.findViewById(R.id.recyclerview_board);
        list = new ArrayList<>();

        list.add(new UserItemModel("id", "사역 신청이 왔습니다", "테스트", "admin"));
        list.add(new UserItemModel("id", "답변이 왔습니다.", "테스트", "admin"));
        list.add(new UserItemModel("id", "가입을 환영합니다", "테스트", "admin"));
        list.add(new UserItemModel("id", "쓸게 없습니다", "테스트", "admin"));
        list.add(new UserItemModel("id", "하나더", "테스트", "admin"));

        adapter = new UserAdapter(list);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // TODO: Use the ViewModel
    }

    private void setUser(View root, FirebaseUser user) {
        TextView tvName = root.findViewById(R.id.tv_user_name);
        TextView tvMail = root.findViewById(R.id.tv_user_mail);

        tvName.setText(user.getDisplayName());
        tvMail.setText(user.getEmail());
    }
    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

        private List<UserItemModel> modelList;

        public UserAdapter(List<UserItemModel> modelList) {
            this.modelList = modelList;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seek_board_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            UserItemModel data = modelList.get(position);
            holder.txtTitle.setText(data.getTitle());
            holder.txtContents.setText(data.getContents());
            holder.txtName.setText(data.getName());
        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {

            private TextView txtTitle;
            private TextView txtContents;
            private TextView txtName;

            public UserViewHolder(@NonNull View itemView) {
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