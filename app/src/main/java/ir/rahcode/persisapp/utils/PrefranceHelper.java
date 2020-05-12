package ir.rahcode.persisapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefranceHelper {

   static SharedPreferences pref;
  static  SharedPreferences.Editor editor;
    public PrefranceHelper(Context context){
        pref = context.getSharedPreferences("persis",Context.MODE_PRIVATE);
        editor = pref.edit();


    }





    public static  void SetUsername(String username){
        editor.putString("username",username);
        editor.commit();
    }
    public  static  void SetPassword(String password){
        editor.putString("password",password);
        editor.commit();
    }
    public  static  String getUsername(){
        return  pref.getString("username","");
    }
    public  static  String getPassword(){
        return pref.getString("password","");
    }









}
