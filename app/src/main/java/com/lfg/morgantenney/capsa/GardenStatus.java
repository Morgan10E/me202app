package com.lfg.morgantenney.capsa;

/**
 * Created by morgantenney on 5/10/16.
 */
public class GardenStatus {
    public String waterLevel;
    public String lightState;
    public String conductivity;
    public String phValue;

    public GardenStatus(String waterLevel, String lightState, String conductivity, String phValue) {
        this.waterLevel = waterLevel;
        this.lightState = lightState;
        this.conductivity = conductivity;
        this.phValue = phValue;
    }

    public GardenStatus() {
        //for Firebase?
    }

    public String toString() {
        return waterLevel + "&" + lightState + "&" + conductivity + "&" + phValue;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (!(object instanceof GardenStatus)) return false;

        GardenStatus ent = (GardenStatus)object;

        return this.waterLevel == ent.waterLevel;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
