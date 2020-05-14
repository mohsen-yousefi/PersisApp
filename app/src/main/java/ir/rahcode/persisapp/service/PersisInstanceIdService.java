package ir.rahcode.persisapp.service;

import com.google.android.gms.common.api.internal.PendingResultFacade;
import com.google.firebase.messaging.FirebaseMessagingService;

import ir.rahcode.persisapp.utils.PrefranceHelper;


/**
 * Created by Androgo on 10/13/2018.
 */

public class PersisInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        saveToken(s);
    }

    private void saveToken(String tokenId) {
         PrefranceHelper.SetFCMToken(tokenId);
    }

}
