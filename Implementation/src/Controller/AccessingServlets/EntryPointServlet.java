package Controller.AccessingServlets;

import Model.Database.DataSource;
import Model.Manager;
import Model.User;
import Model.Worker;
import Repository.ManagerRepo;
import Repository.WorkerRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "EntryPointServlet")
public class EntryPointServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // not logged in
            response.sendRedirect(getServletContext().getContextPath() + "/Login");
        } else {
            if (session.getAttribute("user") instanceof Worker) {
                Worker worker = (Worker) session.getAttribute("user");
                WorkerRepo workerRepo = new WorkerRepo();
                request.setAttribute("registeredCampaigns",workerRepo.getRegisteredCampaigns(this.dataSource, worker));
                request.setAttribute("availableCampaigns", workerRepo.getAvailableToRegisterCampaigns(dataSource,worker));
                request.getRequestDispatcher("/Worker/HomePage").forward(request,response);
            } else {
                Manager manager = (Manager) session.getAttribute("user");
                ManagerRepo managerRepo = new ManagerRepo();
                request.setAttribute("createdCampaigns", managerRepo.getCreatedCampaign(this.dataSource, manager));
                request.getRequestDispatcher("/Manager/HomePage").forward(request,response);
            }
        }
    }
}
