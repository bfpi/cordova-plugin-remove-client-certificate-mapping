package de.bfpi.cordova.client;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Map;
import java.lang.Runnable;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CertificateAuthenticationChain extends CordovaPlugin {
  private static final String LOG_TAG = "Client Certificate Authentication Chain";

  @Override
  public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
    if (action.equals("clean")) {
      String key = args.getString(0);
      cordova.getThreadPool().execute(new Runnable() {
        @Override
        public void run() {
          callbackContext.sendPluginResult(clean(key));
        }
      });
    } else if (action.equals("getPreferences")) {
      cordova.getThreadPool().execute(new Runnable() {
        @Override
        public void run() {
          callbackContext.sendPluginResult(getPreferences());
        }
      });
    }
    return true;
  }

  private PluginResult clean(String spKeyName) {
    try {
      SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(cordova.getActivity()).edit();
      editor.remove(spKeyName);
      editor.commit();
      Log.v(LOG_TAG, "Removed key " + spKeyName + " from preferences");
    } catch (Exception e) {
      Log.e(LOG_TAG, "Unexpected error cleaning key chain", e);
      return new PluginResult(PluginResult.Status.ERROR, e.getMessage());
    }
    return new PluginResult(PluginResult.Status.OK);
  }

  private PluginResult getPreferences() {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(cordova.getActivity());
    Map<String, ?> map = sp.getAll();
    JSONObject jsonObject = new JSONObject();

    for (String key : map.keySet()) {
      try {
        jsonObject.put(key, map.get(key));
      }
      catch (JSONException jsone) {
        Log.e(LOG_TAG, "Failed to put value for " + key + " into JSONObject.", jsone);
        return new PluginResult(PluginResult.Status.JSON_EXCEPTION, jsone.getMessage());
      }
    }
    return new PluginResult(PluginResult.Status.OK, jsonObject);
  }
}
