package Controller.CampaignServlets.LoadCampaignInfoServlets;

import Model.Database.DataSource;
import Model.User;
import Model.Worker;
import Repository.CampaignRepo;
import Utilities.Inspector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoadCampaignPeaksWorkerServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        dataSource = DataSource.getInstance();
        super.init();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Worker && Inspector.checkPermissions(dataSource, user , Integer.parseInt(request.getParameter("idCampaign")))){

            CampaignRepo campaignRepo = new CampaignRepo();
            request.setAttribute("campaignPeaks", campaignRepo.getCampaignPeaksData(dataSource, Integer.parseInt(request.getParameter("idCampaign"))));
            if (request.getParameter("forwardTo").equals("2D"))
                request.getRequestDispatcher("/Worker/2DMap").forward(request, response);
            else if (request.getParameter("forwardTo").equals("3D"))
                request.getRequestDispatcher("/Worker/3DMap").forward(request, response);
            else response.sendRedirect("/CrowdsourcingPeak/GeneralError");

        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }
}
