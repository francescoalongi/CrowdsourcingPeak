package Model;

import java.sql.Timestamp;

public class Campaign {

    private Integer idCampaign;
    private String name;
    private String state;
    private Timestamp startDate;
    private Timestamp endDate;
    private Integer idManager;


    //Constructor of campaign used for when you want to INSERT a campaign in db
    public Campaign(String name, String state, Timestamp startDate, Timestamp endDate, Integer idManager) throws IllegalArgumentException {
        this.name = name;
        if(state.equals("CREATED") || state.equals("STARTED") || state.equals("CLOSED"))
            this.state = state;
        else throw new IllegalArgumentException("The value must be CREATED or STARTED or CLOSED");
        this.startDate = startDate;
        this.endDate = endDate;
        this.idManager = idManager;
    }

    //Constructor of campaign used for when you want to GET a campaign from db
    public Campaign(Integer idCampaign, String name, String state, Timestamp startDate, Timestamp endDate, Integer idManager) throws IllegalArgumentException {
        this.idCampaign = idCampaign;
        this.name = name;
        if(state.equals("CREATED") || state.equals("STARTED") || state.equals("CLOSED"))
            this.state = state;
        else throw new IllegalArgumentException("The value must be CREATED or STARTED or CLOSED");
        this.startDate = startDate;
        this.endDate = endDate;
        this.idManager = idManager;
    }

    public Integer getIdCampaign() {
        return idCampaign;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public Integer getIdManager() {
        return idManager;
    }
}
