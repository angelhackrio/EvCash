package angelhack.evcash;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import angelhack.evcash.extras.CircleTransformation;
import angelhack.evcash.util.Loading;
import angelhack.evcash.util.PrefManager;
import io.card.payment.CardIOActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartaoFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private final int MY_SCAN_REQUEST_CODE = 1000;
    TextView txtNome;
    ImageView imgPerfil;
    Button btnLerCartao;

    public CartaoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (Loading.getDialog() == null)
            new Loading(getActivity(), "Carregando...");
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_cartao, container, false);

        PrefManager prefmanager = new PrefManager(getActivity());

        imgPerfil = (ImageView) v.findViewById(R.id.imageViewPerfil);
        txtNome = (TextView) v.findViewById(R.id.textViewNome);
        btnLerCartao = (Button) v.findViewById(R.id.buttonLerCartao);


        Picasso.with(getActivity())
                .load(prefmanager.getImgURLFacebook())
                .resize(150, 150)
                .transform(new CircleTransformation())
                .centerCrop()
                .into(imgPerfil, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if (Loading.getDialog() != null)
                            Loading.getDialog().dismiss();

                        v.findViewById(R.id.cartao_layout).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        btnLerCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

            }
        });

        Log.println(Log.ASSERT, TAG, "" + prefmanager.getNomeFacebook());
        txtNome.setText(prefmanager.getNomeFacebook());
        return v;
    }


}
