package Controller.AccessingServlets;

import Model.Database.DataSource;
import Model.Manager;
import Model.User;
import Model.Worker;
import Repository.WorkerRepo;
import com.ja.security.PasswordHash;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "Controller.AccessingServlets.SignupCheckServlet")
public class SignupCheckServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        String email = request.getParameter("email");
        String isManager = request.getParameter("mngCheckBox");

        if (username.equals("") || password.equals("") || email.equals("")) {
            request.setAttribute("errorInformation", "<strong>Warning!</strong> All forms must be filled.");
            request.getRequestDispatcher("/Signup").forward(request, response);
            return;
        }

        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(email)) {
            request.setAttribute("errorInformation", "<strong>Warning!</strong> The email is not valid.");
            request.getRequestDispatcher("/Signup").forward(request, response);
            return;
        }

        if (!password.equals(passwordConfirm)) {
            request.setAttribute("errorInformation", "<strong>Warning!</strong> The two forms related to the password must be the same!");
            request.getRequestDispatcher("/Signup").forward(request, response);
            return;
        }
        String insertQuery;
        HttpSession session;
        if(isManager == null){ //is a worker

            insertQuery = "INSERT INTO crowdsourcingpeak.worker (username, hash, email) " +
                    "VALUES (?, ?, ?)";
            Worker worker = new Worker();
            worker.setUsername(username);
            worker.setPassword(password);
            worker.setEmail(email);


            try {
                insertUser(insertQuery, worker);
            } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                handleException(e, request);
                request.getRequestDispatcher("/Signup").forward(request, response);
                return;
            }
            session = request.getSession(true);
            session.setAttribute("user", worker);

            WorkerRepo workerRepo= new WorkerRepo();
            //request.setAttribute("registeredCampaigns", workerRepo.getRegisteredCampaigns(dataSource,worker));
            request.setAttribute("availableCampaigns", workerRepo.getAvailableToRegisterCampaigns(dataSource,worker));
            request.getRequestDispatcher("/Worker/HomePage").forward(request, response);

        } else { //is a manager

            insertQuery = "INSERT INTO crowdsourcingpeak.manager (username, hash, email) " +
                    "VALUES (?, ?, ?)";
            Manager manager = new Manager();
            manager.setUsername(username);
            manager.setPassword(password);
            manager.setEmail(email);
            try {
                insertUser(insertQuery, manager);
            } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                handleException(e, request);
                request.getRequestDispatcher("/Signup").forward(request, response);
                return;
            }
            session = request.getSession(true);
            session.setAttribute("user", manager);
            response.sendRedirect("/CrowdsourcingPeak/Manager/HomePage");

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void insertUser (String insertQuery, User user) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        String getIdUserQuery = "SELECT LAST_INSERT_ID() AS last_id";

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement insertUser = connection.prepareStatement(insertQuery);
             PreparedStatement getIdUser = connection.prepareStatement(getIdUserQuery)) {
            insertUser.setString(1, user.getUsername());
            insertUser.setString(2, new PasswordHash().createHash(user.getPassword()));
            insertUser.setString(3, user.getEmail());
            insertUser.executeUpdate();

            ResultSet resultSet = getIdUser.executeQuery();
            if (resultSet.next()) {
                 user.setIdUser(resultSet.getInt("last_id"));
            }

        }
    }

    private void handleException(Exception e, HttpServletRequest request) {

        if (e instanceof SQLException) {
            // TODO: handle all SQLException

            // reference to all the errors: https://dev.mysql.com/doc/refman/5.5/en/error-messages-server.html
            if (((SQLException) e).getSQLState() == null) {
                request.setAttribute("errorInformation", "<strong>Warning!</strong> Server offline!");
                return;
            }

            switch (((SQLException) e).getSQLState()) {
                case "23000": // Duplicate insertion
                    request.setAttribute("errorInformation", "<strong>Warning!</strong> Already exist a user with this username or email.");
                    break;
            }
        }
    }
}
