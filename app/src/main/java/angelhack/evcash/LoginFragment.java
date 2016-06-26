package angelhack.evcash;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import angelhack.evcash.util.Loading;
import angelhack.evcash.util.PrefManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    static CallbackManager callbackManager;
    private final String TAG = this.getClass().getSimpleName();
    LoginButton loginButton;
    Firebase ref;
    Context mContext;
    AccessTokenTracker mFacebookAccessTokenTracker;
    PrefManager prefManager;


    public LoginFragment() {
        // Required empty public constructor
    }

    static public CallbackManager getCallbackManager() {
        return callbackManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_login, container, false);


        callbackManager = CallbackManager.Factory.create();
        prefManager = new PrefManager(getActivity());
        loginButton = (LoginButton) v.findViewById(R.id.login_button);


        mFacebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                ref = new Firebase("https://evcash.firebaseio.com/");

                if (currentAccessToken == null) {
                    ref.unauth();

                }

                onFacebookAccessTokenChange(currentAccessToken);

            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Loading(getActivity(), "Carregando");
            }
        });

        return v;
    }

    private void onFacebookAccessTokenChange(AccessToken token) {

        if (token != null) {

            ref.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {

                    HashMap<String, Object> map = (HashMap<String, Object>) authData.getProviderData().get("cachedUserProfile");

                    Map<String, String> mapaBD = new HashMap<>();

                    String nome = (String) map.get("first_name");
                    String sobrenome = (String) map.get("last_name");
                    String email = (String) map.get("email");

                    String sexo = (String) map.get("gender");

                    LinkedHashMap ageRange = (LinkedHashMap) map.get("age_range");

                    mapaBD.put("id", authData.getUid());
                    mapaBD.put("nome", nome + " " + sobrenome);
                    mapaBD.put("email", email);
                    mapaBD.put("sexo", sexo);


                    mapaBD.put("imgURL", authData.getProviderData().get("profileImageURL").toString());

                    String faixa_idade;

                    if (ageRange.get("max") == null)
                        faixa_idade = "21+";
                    else
                        faixa_idade = ageRange.get("min") + "-" + ageRange.get("max");

                    mapaBD.put("faixa_idade", faixa_idade);

                    String aniversario = (String) map.get("birthday");

                    if (aniversario != null)
                        mapaBD.put("aniversario", aniversario);

                    prefManager.setNomeFacebook(nome + " " + sobrenome);
                    prefManager.setImgURLFacebook(authData.getProviderData().get("profileImageURL").toString());

                    ref.child("usuarios").child(authData.getUid()).setValue(mapaBD);


                    new AlertDialog.Builder(getActivity())
                            .setTitle("Login Efetuado")
                            .setMessage("Seja bem vindo(a) " + nome + "!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                            .show();

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    transaction.add(R.id.main_layout, new CartaoFragment(), "CartaoFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();


                    // The Facebook user is now authenticated with your Firebase app
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    Log.println(Log.ASSERT, TAG, "" + firebaseError.getMessage());
                    Log.println(Log.ASSERT, TAG, "" + firebaseError.getDetails());
                }
            });
        } else {
        /* Logged out of Facebook so do a logout from the Firebase app */
            ref.unauth();
        }
    }
}




