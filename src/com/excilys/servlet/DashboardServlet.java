package com.excilys.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.dao.ComputerDAO;
import com.excilys.data.Computer;

/**
 * Servlet implementation class DashboardServlet
 */
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int nbElementsParPage = 15;

	final Logger logger = LoggerFactory.getLogger(DashboardServlet.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DashboardServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		String search = request.getParameter("search");
		String order = request.getParameter("order");
		String sens = request.getParameter("sens");
		
		List<Computer> computers;
		logger.info("Request parameters : Search : {}, Order : {}, Sens : {}",search,order,sens);
		if(search == null)
			computers = ComputerDAO.getInstance().retrieveAll(order,sens);
		else{
			computers = ComputerDAO.getInstance().searchByName(search,order,sens);
			request.setAttribute("search", search);
		}
		
		if(order != null)
			request.setAttribute("order", order);
		
		if(sens !=null){
			request.setAttribute("sens", sens);
		}
		
		
		int nbComputers = computers.size();
		int pageMax = (int)Math.floor(nbComputers/nbElementsParPage);
		if (nbComputers%nbElementsParPage != 0)
			pageMax++;
		request.setAttribute("computers", computers);
		request.setAttribute("nbComputers", nbComputers);
		request.setAttribute("nbElementsParPage", nbElementsParPage);
		request.setAttribute("pageMax",pageMax);
		ServletContext ctx = getServletContext();
		RequestDispatcher rd = ctx.getRequestDispatcher("/dashboard.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String search = request.getParameter("search");
		
		if(search != ""){
			HttpSession session = request.getSession() ;
			session.setAttribute("search", search);
		}

		doGet(request,response);
		
	}

}
