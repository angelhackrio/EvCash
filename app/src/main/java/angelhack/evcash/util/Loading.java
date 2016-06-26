package angelhack.evcash.util;

import android.app.ProgressDialog;
import android.content.Context;


/**
 * Created by Rodrigo on 07/01/2016.
 */
public class Loading {
    private static ProgressDialog dialog;

    public Loading(Context context, String texto) {
        dialog = new ProgressDialog(context);
        dialog = ProgressDialog.show(context, "", texto, true, true);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);


    }


    public static ProgressDialog getDialog() {
        return dialog;
    }
}
