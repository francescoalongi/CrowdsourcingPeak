package Controller.PeaksRelatedServlets;


import Model.Database.DataSource;
import Model.LocalizedName;
import Model.Manager;
import Model.Peak;
import Model.User;
import Repository.CampaignRepo;
import Utilities.Inspector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

@MultipartConfig
@WebServlet(name = "InsertPeaksServlet")
public class InsertPeaksServlet extends HttpServlet {

    DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Manager && Inspector.checkPermissions(dataSource, user, Integer.parseInt(request.getParameter("idCampaign")))) {

            ArrayList<Peak> validPeaks = new ArrayList<>();

            // Parse the json file with peaks and load it into the database
            CampaignRepo campaignRepo = new CampaignRepo();
            Part filePart = request.getPart("peaksFile"); // Retrieves <input type="file" name="file">
            InputStream fileContent = filePart.getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray;
            try {
                jsonArray = (JSONArray) jsonParser.parse(new InputStreamReader(fileContent, "UTF-8"));
            } catch (ParseException | ClassCastException e) {
                request.setAttribute("errorInformation", "<strong>Warning!</strong> The file must be a correct json file.");
                populateRequest(request);
                request.getRequestDispatcher("/Manager/CampaignDetails").forward(request, response);
                return;
            }

            boolean toBeAnnotated = false;
            if (request.getParameter("toBeAnnotated") != null) {
                toBeAnnotated = true;
            }
            ArrayList<Peak> invalidPeaks = new ArrayList<>();
            //ArrayList<Peak> validPeaks = new ArrayList<>();
            for (Object o : jsonArray) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) o;
                    Peak peak = null;
                    try {
                        // for each peak, i get the localized names
                        // Note: localized names are saved as JSONArray of JSONArray!
                        // e.g.: localized_names: [ ["it","Monte Bianco"], ["fr", "Mont Blanc"] ]
                        ArrayList<LocalizedName> localizedNames = new ArrayList<>();
                        JSONArray localizedNamesJSONArray = (JSONArray) jsonObject.get("localized_names");
                        if (localizedNamesJSONArray != null) {
                            for (Object obj : localizedNamesJSONArray) {
                                if (obj instanceof JSONArray) {
                                    JSONArray localizedNameJSONArray = (JSONArray) obj;
                                    LocalizedName localizedName = new LocalizedName(localizedNameJSONArray.get(0).toString(), localizedNameJSONArray.get(1).toString());
                                    localizedNames.add(localizedName);
                                }
                            }
                        }

                        // mandatory attribute for a peak, if one of them is not present the peak is considered invalid
                        if (jsonObject.get("id") == null || jsonObject.get("latitude") == null ||
                                jsonObject.get("longitude") == null || jsonObject.get("provenance") == null) {
                            throw new Exception();
                        }

                        peak = new Peak(Integer.parseInt(jsonObject.get("id").toString()), jsonObject.get("provenance").toString(), Double.parseDouble(jsonObject.get("longitude").toString()),
                                Double.parseDouble(jsonObject.get("latitude").toString()), localizedNames, toBeAnnotated);

                        if (jsonObject.get("elevation") != null) {
                            peak.setAltitude(Double.parseDouble(jsonObject.get("elevation").toString()));
                        }

                        if (jsonObject.get("name") != null) {
                            peak.setName(jsonObject.get("name").toString());
                        }

                        validPeaks.add(peak);

                    } catch (Exception e) {
                        invalidPeaks.add(peak);
                        continue;
                    }
                }
            }

            if (!validPeaks.isEmpty()) campaignRepo.insertPeaks(dataSource, Integer.parseInt(request.getParameter("idCampaign")), validPeaks);
            if (!invalidPeaks.isEmpty()) {
                request.setAttribute("errorInformation", "<strong>Warning!</strong> " + invalidPeaks.size() + " peaks were corrupted or already exist and were not loaded");
                populateRequest(request);
                request.getRequestDispatcher("/Manager/CampaignDetails").forward(request, response);
                return;
            }

            request.getRequestDispatcher("/LoadCampaignDataManager").forward(request, response);
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    void populateRequest (HttpServletRequest request) {
        // this method is needed because when the form used for load the peaks in CampaignDetailsPage is fired
        // we lose the attribute of the previous request object
        request.setAttribute("stateCampaign", request.getParameter("stateCampaign"));
        request.setAttribute("startDateCampaign", request.getParameter("startDateCampaign"));
        request.setAttribute("endDateCampaign", request.getParameter("endDateCampaign"));
        request.setAttribute("nameCampaign", request.getParameter("nameCampaign"));
    }

}
