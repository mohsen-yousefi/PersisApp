package ir.rahcode.persisapp.models;

/** Go - TAXI - On Demand All in One App Services Android
 * Created by Androgo on 10/13/2018.
 */

public class FirebaseToken    {
    private String tokenId;

    public FirebaseToken(String tokenId) {
        this.tokenId = tokenId;
    }

    public FirebaseToken() {
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
