package Controller.PeaksRelatedServlets;

import Model.Database.DataSource;
import Model.Manager;
import Model.User;
import Utilities.Inspector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "ChangeAnnotationStateServlet")
public class ChangeAnnotationStateServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Manager && Inspector.checkPermissions(dataSource, user, Integer.parseInt(request.getParameter("idCampaign")))) {


            String changeAnnotationStateQuery = "UPDATE crowdsourcingpeak.annotation SET annotation.state = ? " +
                    "WHERE annotation.idCampaign = ? AND annotation.idPeak = ? AND annotation.idAnnotation = ?";

            String[] refusedAnnotationsID = request.getParameterValues("refusedAnnotation");
            String[] notRefusedAnnotationsID = request.getParameterValues("notRefusedAnnotation");
            try (Connection conn = dataSource.getConnection()) {
                if (refusedAnnotationsID != null) {
                    for (int i = 0; i < refusedAnnotationsID.length; i++) {
                        PreparedStatement refuseAnnotation = conn.prepareStatement(changeAnnotationStateQuery);
                        refuseAnnotation.setBoolean(1, false);
                        refuseAnnotation.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                        refuseAnnotation.setInt(3, Integer.parseInt(request.getParameter("idPeak")));
                        refuseAnnotation.setInt(4, Integer.parseInt(refusedAnnotationsID[i]));
                        refuseAnnotation.executeUpdate();
                        refuseAnnotation.close();
                    }
                }

                if (notRefusedAnnotationsID != null) {
                    for (int i = 0; i < notRefusedAnnotationsID.length; i++) {
                        PreparedStatement refuseAnnotation = conn.prepareStatement(changeAnnotationStateQuery);
                        refuseAnnotation.setBoolean(1, true);
                        refuseAnnotation.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                        refuseAnnotation.setInt(3, Integer.parseInt(request.getParameter("idPeak")));
                        refuseAnnotation.setInt(4, Integer.parseInt(notRefusedAnnotationsID[i]));
                        refuseAnnotation.executeUpdate();
                        refuseAnnotation.close();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorInformation", "<strong>Warning!</strong> It was not possible to update the annotations state.");
                request.getRequestDispatcher("/LoadPeakDataMap").forward(request, response);
                return;
            }
            request.getRequestDispatcher("/LoadCampaignDataManager").forward(request, response);
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
