package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FrontController() {
		super();
	}

	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. Client가 보낸 요청을 추출 -> 어떤 액션인지 파악
		// 더이상 파라미터를 들고다니지않음!
		// URL을 분석해야함!!!
		String uri=request.getRequestURI();
		System.out.println("uri: "+uri);
		String cp=request.getContextPath();
		System.out.println("cp: "+cp);
		String command=uri.substring(cp.length());
		System.out.println("command: "+command);

		// 2. 추출한 요청에 맞는 Action클래스의 execute() 메서드를 호출
		ActionForward forward=null;
		if(command.equals("/main.do")) {
			forward=new MainAction().execute(request, response);
		}
		else if(command.equals("/signup.do")) {
			forward=new SignupAction().execute(request, response);
		}
		else {

		}

		// 3. 사용자에게 응답. View로 이동
		if(forward!=null) {
			if(forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			}
			else {
				RequestDispatcher dispatcher=request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
		
		PrintWriter out=response.getWriter();
		out.println("<script>alert('요청처리에 실패했습니다.....');history.go(-1);</script>");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

}
