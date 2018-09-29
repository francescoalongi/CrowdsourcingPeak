package Controller.CampaignServlets.LifecycleCampaignServlets;

import Model.Campaign;
import Model.Database.DataSource;
import Model.Manager;
import Repository.ManagerRepo;
import com.sun.xml.internal.bind.v2.TODO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "CreateCampaignServlet", urlPatterns = "/CreateCampaign")
public class CreateCampaignServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false).getAttribute("user") instanceof Manager) {

            String name = request.getParameter("name");

            if (name.equals("")) {
                request.setAttribute("errorInformation", "<strong>Warning!</strong> Write the name of the campaign.");
                request.getRequestDispatcher("/Manager/CreateCampaign").forward(request, response);
            }

            Campaign campaign = new Campaign(name, "CREATED", null, null, ((Manager) request.getSession(false).getAttribute("user")).getIdUser());
            ManagerRepo managerRepo = new ManagerRepo();
            Integer idCampaign = managerRepo.insertCampaign(dataSource, campaign);
            request.setAttribute("nameCampaign", name);
            request.setAttribute("stateCampaign", "CREATED");
            request.getRequestDispatcher("/Manager/CampaignDetails?idCampaign=" + idCampaign).forward(request, response);

        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
