package com.lfg.morgantenney.capsa;

/**
 * Created by morgantenney on 5/10/16.
 */
public class Garden {
    public String name;
    public String waterLevel;
    public String lightState;
    public String firebaseKey;
    public String photonID;

    public Garden(String name, String waterLevel, String lightState, String firebaseKey, String photonID) {
        this.name = name;
        this.waterLevel = waterLevel;
        this.lightState = lightState;
        this.firebaseKey = firebaseKey;
        this.photonID = photonID;
    }

    public Garden() {
        //for Firebase?
    }

    public String toString() {
        return name + "&" + waterLevel + "&" + lightState + "&" + firebaseKey + "&" + photonID;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (!(object instanceof Garden)) return false;

        Garden ent = (Garden)object;

        return this.photonID.equals(ent.photonID);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
