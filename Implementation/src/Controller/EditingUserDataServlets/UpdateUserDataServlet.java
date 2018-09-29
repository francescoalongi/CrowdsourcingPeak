package Controller.EditingUserDataServlets;

import Model.Database.DataSource;
import Model.Manager;
import Model.User;
import Model.Worker;
import Repository.ManagerRepo;
import Repository.UserRepo;
import Repository.WorkerRepo;
import org.apache.commons.validator.routines.EmailValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "UpdateUserDataServlet")
public class UpdateUserDataServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (!request.getParameter("newPassword").equals(request.getParameter("newPasswordConfirm"))) {
            request.setAttribute("errorInformation", "<strong>Warning!</strong> The two forms related to the password must be the same!");
            request.getRequestDispatcher("/User/EditPersonalData").forward(request, response);
            return;
        }

        if (request.getParameter("username").equals("") && request.getParameter("email").equals("")
                && request.getParameter("newPassword").equals("")) {

            request.setAttribute("errorInformation", "<strong>Warning!</strong> You are not modifying anything.");
            request.getRequestDispatcher("/User/EditPersonalData").forward(request, response);
            return;

        }

        EmailValidator emailValidator = EmailValidator.getInstance();
        if (request.getParameter("email") != null && !request.getParameter("email").isEmpty() && !emailValidator.isValid(request.getParameter("email"))) {
            request.setAttribute("errorInformation", "<strong>Warning!</strong> The email is not valid.");
            request.getRequestDispatcher("/User/EditPersonalData").forward(request, response);
            return;
        }

        User user = null;
        if (session.getAttribute("user") instanceof Manager)
            user = new Manager((Manager)session.getAttribute("user"));
        if (session.getAttribute("user") instanceof Worker)
            user = new Worker((Worker)session.getAttribute("user"));
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("newPassword"));

        UserRepo userRepo = new UserRepo();
        try {
            userRepo.updatePersonalData(this.dataSource, user);

        } catch (SQLException e) {
            handleException(e, request);
            request.getRequestDispatcher("/User/EditPersonalData").forward(request, response);
            return;
        }


        if (request.getParameter("username")!= null && !request.getParameter("username").equals("")) ((User) session.getAttribute("user")).setUsername(request.getParameter("username"));
        if (request.getParameter("email")!= null && !request.getParameter("email").equals("")) ((User) session.getAttribute("user")).setEmail(request.getParameter("email"));
        if (user instanceof Manager) {
            ManagerRepo managerRepo = new ManagerRepo();
            request.setAttribute("createdCampaigns", managerRepo.getCreatedCampaign(this.dataSource, (Manager)user));
            request.getRequestDispatcher("/Manager/HomePage").forward(request,response);
        } else if (user instanceof Worker) {
            WorkerRepo workerRepo = new WorkerRepo();
            request.setAttribute("registeredCampaigns", workerRepo.getRegisteredCampaigns(dataSource, (Worker) user));
            request.setAttribute("availableCampaigns", workerRepo.getAvailableToRegisterCampaigns(dataSource, (Worker) user));
            request.getRequestDispatcher("/Worker/HomePage").forward(request,response);
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/CrowdsourcingPeak");
    }


    private void handleException(Exception e, HttpServletRequest request) {

        if (e instanceof SQLException) {
            // TODO: handle all SQLException

            // reference to all the errors: https://dev.mysql.com/doc/refman/5.5/en/error-messages-server.html
            switch (((SQLException) e).getSQLState()) {
                case "23000": // Duplicate insertion
                    request.setAttribute("errorInformation", "<strong>Warning!</strong> Already exist a user with this username or email.");
                    break;
            }
        }
    }

}
