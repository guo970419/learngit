package web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exception.UserException;
import bean.User;
import service.UserService;

public class LoginServelt extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String name=request.getParameter("name");
		String pass=request.getParameter("pass");
		UserService service=new UserService();
		String url="";
		try {
			User user=service.login(name, pass);
			int id = user.getId();//用于查找推荐的人数
			int did=user.getDid();//用于查找推荐者
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			
			int count=service.findDriectRecomment(id);
			request.setAttribute("count", count);
			
			List<Integer> list=service.findIndriectRecomment(id);
			int all=0;
			for (int i = 0; i < list.size(); i++) {
				int idd=list.get(i);
				all=all+service.findDriectRecomment(idd);
			}
			request.setAttribute("all", all);
			
			User recommentName=service.findRecommenter(did);
			String name1="";
			String name2="";
			int did1;
			User indriectIdRecommentName=null;
			if(null!=recommentName){
				name1=recommentName.getName();
				did1=recommentName.getDid();
				indriectIdRecommentName=service.findIndriectRecommenter(did1);
				request.setAttribute("recommentName", name1);
				if(null!=indriectIdRecommentName){
					name2=indriectIdRecommentName.getName();
				}
				request.setAttribute("indriectIdRecommentName", name2);
			}
			url = "/page/index.jsp";
		} catch (UserException e) {
			String errorMsg = e.getMessage();
			request.setAttribute("errorMsg", errorMsg);
			url = "/index.jsp";
		}
		request.getRequestDispatcher(url).forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

	public void init() throws ServletException {
		// 
	}

}
