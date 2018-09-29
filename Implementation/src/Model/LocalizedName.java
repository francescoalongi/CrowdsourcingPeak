package Model;

public class LocalizedName {
    private Integer idLocalizedName;
    private String country;
    private String localizedName;

    public LocalizedName (String country, String localizedName) {
        this.country = country;
        this.localizedName = localizedName;
    }

    public LocalizedName (Integer idLocalizedName, String country, String localizedName) {
        this.idLocalizedName = idLocalizedName;
        this.country = country;
        this.localizedName = localizedName;
    }

    public String getCountry() {
        return country;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public Integer getIdLocalizedName() {
        return idLocalizedName;
    }
}
