package Controller.AccessingServlets;

import Model.Database.DataSource;
import Model.Manager;
import Model.User;
import Model.Worker;
import Repository.ManagerRepo;
import Repository.WorkerRepo;
import com.ja.security.PasswordHash;

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

@WebServlet(name = "Controller.AccessingServlets.LoginCheckServlet")
public class LoginCheckServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));

        try {
            user = verifyCredentials(user);
        } catch (SQLException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            handleException(e, request);
            request.getRequestDispatcher("/Login").forward(request, response);
            return;
        }

        if(user == null){
            request.setAttribute("errorInformation", "<strong>Warning!</strong> Your username or password is wrong.");
            request.getRequestDispatcher("/Login").forward(request, response);
            return;
        }

        if (user instanceof Manager) {
            Manager manager = (Manager) user;
            HttpSession session = request.getSession(true);
            session.setAttribute("user", manager);
            ManagerRepo managerRepo = new ManagerRepo();
            request.setAttribute("createdCampaigns", managerRepo.getCreatedCampaign(this.dataSource, manager));
            request.getRequestDispatcher("/Manager/HomePage").forward(request, response);
        } else {
            Worker worker = (Worker) user;
            HttpSession session = request.getSession(true);
            session.setAttribute("user", worker);
            WorkerRepo workerRepo = new WorkerRepo();
            request.setAttribute("registeredCampaigns", workerRepo.getRegisteredCampaigns(dataSource,worker));
            request.setAttribute("availableCampaigns", workerRepo.getAvailableToRegisterCampaigns(dataSource,worker));
            request.getRequestDispatcher("/Worker/HomePage").forward(request, response);

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/CrowdsourcingPeak");
    }

    private User verifyCredentials (User user) throws SQLException, InvalidKeySpecException, NoSuchAlgorithmException {
        String workerLoginQuery = "SELECT * FROM worker WHERE username = ?";
        String managerLoginQuery = "SELECT * FROM manager WHERE username = ?";

        try (Connection conn = this.dataSource.getConnection();
             PreparedStatement workerStm = conn.prepareStatement(workerLoginQuery);
             PreparedStatement managerStm = conn.prepareStatement(managerLoginQuery)){
            workerStm.setString(1, user.getUsername());
            managerStm.setString(1, user.getUsername());
            ResultSet managerRS = managerStm.executeQuery();
            ResultSet workerRS = workerStm.executeQuery();
            if((managerRS != null && workerRS != null) && (!managerRS.next() && !workerRS.next()))
                return null;
            if (managerRS != null) {
                managerRS.beforeFirst();
                if (managerRS.next())
                    if (new PasswordHash().validatePassword(user.getPassword(), managerRS.getString("hash"))) {
                        user.setEmail(managerRS.getString("email"));
                        user.setIdUser(managerRS.getInt("idUser"));
                        return new Manager(user);
                    }
            }
            if (workerRS != null) {
                workerRS.beforeFirst();
                if (workerRS.next())
                    if (new PasswordHash().validatePassword(user.getPassword(), workerRS.getString("hash"))) {
                        user.setEmail(workerRS.getString("email"));
                        user.setIdUser(workerRS.getInt("idUser"));
                        return new Worker(user);
                    }
            }
        }
        return null;

    }


    private void handleException(Exception e, HttpServletRequest request) {

        if (e instanceof SQLException) {
            // TODO: handle all SQLException

            if (((SQLException) e).getSQLState() == null) {
                request.setAttribute("errorInformation", "<strong>Warning!</strong> Server offline!");
                return;
            }
            // reference to all the errors: https://dev.mysql.com/doc/refman/5.5/en/error-messages-server.html
            switch (((SQLException) e).getSQLState()) {
                case "23000": // Duplicate insertion
                    request.setAttribute("errorInformation", "<strong>Warning!</strong> Already exist a user with this username or email.");
                    break;
            }
        }
    }
}
