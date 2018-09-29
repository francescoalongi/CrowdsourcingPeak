package Model;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Annotation {
    private Integer idAnnotation;
    private Integer idPeak;
    private Integer idWorker;
    private Timestamp creationDate;
    private Boolean validity;
    private Double elevation;
    private String name;
    private ArrayList<LocalizedName> localizedNames;
    private Boolean state;


    public Annotation(Integer idPeak, Integer idWorker, Timestamp creationDate, Boolean validity, Double elevation, String name, ArrayList<LocalizedName> localizedNames, Boolean state) {
        this.idPeak = idPeak;
        this.idWorker = idWorker;
        this.creationDate = creationDate;
        this.validity = validity;
        this.elevation = elevation;
        this.name = name;
        this.localizedNames = localizedNames;
        this.state = state;
    }

    public Annotation(Integer idAnnotation, Integer idPeak, Integer idWorker, Timestamp creationDate, Boolean validity, Double elevation, String name, ArrayList<LocalizedName> localizedNames, Boolean state) {
        this.idAnnotation = idAnnotation;
        this.idPeak = idPeak;
        this.idWorker = idWorker;
        this.creationDate = creationDate;
        this.validity = validity;
        this.elevation = elevation;
        this.name = name;
        this.localizedNames = localizedNames;
        this.state = state;
    }


    public Integer getIdAnnotation() {
        return idAnnotation;
    }

    public Integer getIdPeak() {
        return idPeak;
    }

    public Integer getIdWorker() {
        return idWorker;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Boolean getValidity() {
        return validity;
    }

    public Double getElevation() {
        return elevation;
    }

    public String getName() {
        return name;
    }

    public Boolean getState() {
        return state;
    }

    public ArrayList<LocalizedName> getLocalizedNames() {
        return localizedNames;
    }
}
