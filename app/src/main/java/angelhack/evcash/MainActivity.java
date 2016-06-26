package angelhack.evcash;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

import angelhack.evcash.util.PrefManager;
import io.card.payment.CreditCard;

public class MainActivity extends AppCompatActivity {

    private final int MY_SCAN_REQUEST_CODE = 1000;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        PrefManager prefManager = new PrefManager(this);
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);


        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (prefManager.getImgURLFacebook() == null) {

            transaction.add(R.id.main_layout, new LoginFragment(), "LoginFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            transaction.add(R.id.main_layout, new CartaoFragment(), "CartaoFragment");
            transaction.addToBackStack(null);
            transaction.commit();

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.println(Log.ASSERT, TAG, "onActivityResult " + requestCode);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(io.card.payment.CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(io.card.payment.CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            } else {
                resultDisplayStr = "Scan was canceled.";
            }

            Log.println(Log.ASSERT, TAG, "" + resultDisplayStr);
        } else
            LoginFragment.getCallbackManager().onActivityResult(requestCode, resultCode, data);

    }

}

