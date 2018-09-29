package Controller.PeaksRelatedServlets;

import Model.Database.DataSource;
import Model.User;
import Model.Worker;
import Utilities.Inspector;
import org.apache.commons.lang3.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "CreateAnnotationServlet")
public class CreateAnnotationServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Worker && Inspector.checkPermissions(dataSource, user, Integer.parseInt(request.getParameter("idCampaign")))) {
            String insertAnnotationQuery = "INSERT INTO crowdsourcingpeak.annotation(idWorker, idPeak, idCampaign, creationDate, validity, elevation, name, state) VALUES (?,?,?,?,?,?,?,?)";
            String insertLocalizedNameQuery = "INSERT INTO crowdsourcingpeak.localizedname(country, localizedname) VALUES (?,?)";
            String insertAnnotationLocalizedQuery = "INSERT INTO crowdsourcingpeak.annotationlocalizedname(idLocalizedName, idAnnotation) VALUES (?,?)";
            String getLastInsertID = "SELECT LAST_INSERT_ID() AS last_id";

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement lastInsertPS = conn.prepareStatement(getLastInsertID)) {

                PreparedStatement preparedStatement = conn.prepareStatement(insertAnnotationQuery);
                preparedStatement.setInt(1, ((Worker) request.getSession(false).getAttribute("user")).getIdUser());
                preparedStatement.setInt(2, Integer.parseInt(request.getParameter("idPeak")));
                preparedStatement.setInt(3, Integer.parseInt(request.getParameter("idCampaign")));
                Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
                preparedStatement.setTimestamp(4, timeStamp);
                if (request.getParameter("optradio").equals("valid"))
                    preparedStatement.setBoolean(5, true);
                else if (request.getParameter("optradio").equals("invalid"))
                    preparedStatement.setBoolean(5, false);
                //else //Do something
                if (request.getParameter("altitude") != null && !request.getParameter("altitude").isEmpty())
                    preparedStatement.setDouble(6, Double.parseDouble(request.getParameter("altitude")));
                else preparedStatement.setNull(6, Types.DOUBLE);
                if (request.getParameter("name") != null && !request.getParameter("name").isEmpty())
                    preparedStatement.setString(7, request.getParameter("name"));
                else preparedStatement.setNull(7, Types.VARCHAR);
                preparedStatement.setBoolean(8, true);
                preparedStatement.executeUpdate();

                Integer idAnnotation = null;
                ResultSet lastInsertRS = lastInsertPS.executeQuery();
                while (lastInsertRS.next()) {
                    idAnnotation = lastInsertRS.getInt("last_id");
                }

                Integer localizedNamesCounter = 0;
                Integer idLocalizedName = null;
                while (request.getParameter("country" + localizedNamesCounter.toString()) != null &&
                        request.getParameter("localizedName" + localizedNamesCounter.toString()) != null) {
                    preparedStatement = conn.prepareStatement(insertLocalizedNameQuery);
                    preparedStatement.setString(1, request.getParameter("country" + localizedNamesCounter.toString()));
                    preparedStatement.setString(2, request.getParameter("localizedName" + localizedNamesCounter.toString()));
                    preparedStatement.executeUpdate();
                    lastInsertRS = lastInsertPS.executeQuery();
                    while (lastInsertRS.next()) {
                        idLocalizedName = lastInsertRS.getInt("last_id");
                    }
                    preparedStatement = conn.prepareStatement(insertAnnotationLocalizedQuery);
                    preparedStatement.setInt(1, idLocalizedName);
                    preparedStatement.setInt(2, idAnnotation);
                    preparedStatement.executeUpdate();
                    localizedNamesCounter++;
                }
                lastInsertRS.close();
                preparedStatement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                request.setAttribute("errorInformation", "<strong>Warning!</strong> Parsing error. Try again.");
                request.getRequestDispatcher("/LoadPeakWorkerData").forward(request, response);
                return;
            }

            request.getRequestDispatcher("/LoadCampaignPeaksData").forward(request, response);
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
