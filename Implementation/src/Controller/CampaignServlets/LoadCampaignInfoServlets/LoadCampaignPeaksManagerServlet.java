package Controller.CampaignServlets.LoadCampaignInfoServlets;

import Model.Annotation;
import Model.Database.DataSource;
import Model.Manager;
import Model.Peak;
import Model.User;
import Repository.CampaignRepo;
import Repository.PeakRepo;
import Utilities.Inspector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoadCampaignPeaksManagerServlet")
public class LoadCampaignPeaksManagerServlet extends HttpServlet {

    DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Manager && Inspector.checkPermissions(dataSource, user , Integer.parseInt(request.getParameter("idCampaign")))){
            CampaignRepo campaignRepo = new CampaignRepo();
            ArrayList<Peak> peaks = campaignRepo.getCampaignPeaksData(dataSource, Integer.parseInt(request.getParameter("idCampaign")));
                Map<Peak, ArrayList<Annotation>> peakArrayListMap = new HashMap<>();
                PeakRepo peakRepo = new PeakRepo();
                for (Peak peak : peaks) {
                    ArrayList<Annotation> annotations = peakRepo.loadAnnotation(dataSource, peak.getIdPeak(), Integer.parseInt(request.getParameter("idCampaign")));
                    peakArrayListMap.put(peak, annotations);
                }
                request.setAttribute("peaksData", peakArrayListMap);
                if (request.getParameter("forwardTo").equals("2D"))
                    request.getRequestDispatcher("/Manager/2DMap").forward(request, response);
                else if (request.getParameter("forwardTo").equals("3D"))
                    request.getRequestDispatcher("/Manager/3DMap").forward(request, response);
                else response.sendRedirect("/CrowdsourcingPeak/GeneralError");

        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }
}
