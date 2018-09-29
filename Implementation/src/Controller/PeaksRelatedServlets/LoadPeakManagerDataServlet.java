package Controller.PeaksRelatedServlets;

import Model.*;
import Model.Database.DataSource;
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

@WebServlet(name = "LoadPeakManagerDataServlet")
public class LoadPeakManagerDataServlet extends HttpServlet {
    DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Manager && Inspector.checkPermissions(dataSource, user, Integer.parseInt(request.getParameter("idCampaign")))) {

            String loadPeakQuery = "SELECT * FROM crowdsourcingpeak.peak JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak = peakbelongings.idPeak WHERE peakbelongings.idCampaign = ? AND peak.idPeak = ?";
            ArrayList<Annotation> annotations = new ArrayList<>();
            Peak peak = null;
            PeakRepo peakRepo = new PeakRepo();
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement loadPeak = conn.prepareStatement(loadPeakQuery)) {
                loadPeak.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                loadPeak.setInt(2, Integer.parseInt(request.getParameter("idPeak")));
                ResultSet resultSet = loadPeak.executeQuery();
                while (resultSet.next()) {
                    ArrayList<LocalizedName> localizedNames = peakRepo.loadLocalizedName(dataSource, resultSet.getInt("idPeak"));
                    Double altitude = resultSet.getDouble("altitude");
                    if (resultSet.wasNull()) altitude = null;
                    peak = new Peak(resultSet.getInt("idPeak"), resultSet.getInt("givenIdPeak"),
                            resultSet.getString("provenance"), resultSet.getDouble("longitude"),
                            resultSet.getDouble("latitude"), altitude, resultSet.getString("name"),
                            localizedNames, resultSet.getBoolean("toBeAnnotated"));
                    annotations = peakRepo.loadAnnotation(dataSource, peak.getIdPeak(), Integer.parseInt(request.getParameter("idCampaign")));

                }
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            request.setAttribute("peak", peak);
            request.setAttribute("annotations", annotations);
            request.getRequestDispatcher("/Manager/PeakDetails").forward(request, response);
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }
}
