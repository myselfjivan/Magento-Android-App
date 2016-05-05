package in.co.mrfoody.mrfoody.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.co.mrfoody.mrfoody.R;

/**
 * Created by om on 20/3/16.
 */
public class fragmentRestaurant extends Fragment {
    MrFoody mrFoody = new MrFoody();

    public fragmentRestaurant() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mrFoody.updateHomeViewTopProductsGone();
        return inflater.inflate(R.layout.fragment_restaurnts, container, false);
    }
}
