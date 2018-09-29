package Model;


import java.util.ArrayList;

public class Peak {
    private Integer idPeak;
    private Integer givenIdPeak;
    private String provenance;
    private Double longitude;
    private Double latitude;
    private Double altitude;
    private String name;
    private ArrayList<LocalizedName> localizedNames;
    private Boolean toBeAnnotated;

    //costruttore per lettura picco da database
    public Peak(Integer idPeak, Integer givenIdPeak, String provenance, Double longitude, Double latitude, Double altitude, String name,
                ArrayList<LocalizedName> localizedNames, Boolean toBeAnnotated){
        this.idPeak=idPeak;
        this.givenIdPeak=givenIdPeak;
        this.provenance=provenance;
        this.longitude=longitude;
        this.latitude=latitude;
        this.altitude=altitude;
        this.name=name;
        this.localizedNames=localizedNames;
        this.toBeAnnotated=toBeAnnotated;
    }

    public Peak (Integer givenIdPeak, String provenance, Double longitude, Double latitude,
                 ArrayList<LocalizedName> localizedNames ,Boolean toBeAnnotated){
        this.givenIdPeak=givenIdPeak;
        this.provenance=provenance;
        this.longitude=longitude;
        this.latitude=latitude;
        this.localizedNames = localizedNames;
        this.toBeAnnotated=toBeAnnotated;
    }

    //costruttore per inserimento picco nel database
    public Peak(Integer givenIdPeak, String provenance, Double longitude, Double latitude, Double altitude, String name,
                ArrayList<LocalizedName> localizedNames, Boolean toBeAnnotated){
        this.givenIdPeak=givenIdPeak;
        this.provenance=provenance;
        this.longitude=longitude;
        this.latitude=latitude;
        this.altitude=altitude;
        this.name=name;
        this.localizedNames=localizedNames;
        this.toBeAnnotated=toBeAnnotated;
    }

    public Integer getIdPeak() { return idPeak; }

    public Double getLatitude() { return latitude; }

    public Double getLongitude() { return longitude; }

    public String getName() { return name; }

    public Double getAltitude() { return altitude; }

    public Boolean getToBeAnnotated(){ return toBeAnnotated; }

    public Integer getGivenIdPeak() {
        return givenIdPeak;
    }

    public String getProvenance() {
        return provenance;
    }

    public ArrayList<LocalizedName> getLocalizedNames() {
        return localizedNames;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocalizedNames(ArrayList<LocalizedName> localizedNames) {
        this.localizedNames = localizedNames;
    }

    public void setIdPeak(Integer idPeak) {
        this.idPeak = idPeak;
    }
}
