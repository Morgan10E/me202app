package com.lfg.morgantenney.capsa;

/**
 * Created by morgantenney on 5/10/16.
 */
public class Garden {
    public String name;
    public GardenStatus status;
    public String firebaseKey;
    public String photonID;

    public Garden(String name, GardenStatus status, String firebaseKey, String photonID) {
        this.name = name;
        this.firebaseKey = firebaseKey;
        this.photonID = photonID;
        this.status = status;
    }

    public Garden() {
        //for Firebase?
    }

    public String toString() {
        return name + "&" + firebaseKey + "&" + photonID;
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
