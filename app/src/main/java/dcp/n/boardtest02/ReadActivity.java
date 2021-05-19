package dcp.n.boardtest02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.naver.maps.map.MapFragment;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;

public class ReadActivity extends AppCompatActivity implements OnMapReadyCallback {
    protected TextView tvTitle;
    protected TextView tvContents;
    protected TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        tvTitle = findViewById(R.id.read_title);
        tvContents = findViewById(R.id.read_contents);
        tvName = findViewById(R.id.read_name);

        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("title"));
        tvContents.setText(intent.getStringExtra("contents"));
        tvName.setText(intent.getStringExtra("name"));

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLogoClickEnabled(false);
    }
}