package angelhack.evcash.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PrefManager {
    // Shared pref file name

    private static final String PREF_NAME = "FacebookUtil";
    private static final String MEU_NOME = "nome";
    private static final String MINHA_IMG = "imgURL";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public PrefManager(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

        editor = pref.edit();
    }


    public String getNomeFacebook() {

        return pref.getString(MEU_NOME, null);
    }

    public void setNomeFacebook(String meuNome) {

        editor.putString(MEU_NOME, meuNome);
        editor.commit();

    }

    public String getImgURLFacebook() {

        return pref.getString(MINHA_IMG, null);
    }

    public void setImgURLFacebook(String imgURLFacebook) {

        editor.putString(MINHA_IMG, imgURLFacebook);
        editor.commit();

    }


    public void apagar() {
        editor.clear();
        editor.commit();
    }
}