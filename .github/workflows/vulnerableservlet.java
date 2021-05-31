package com.pnc.eht.interview;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.encoder.Encode;

@WebServlet("/VulnerableServlet")
public class VulnerableServlet extends HttpServlet {
	/**
	 *
	 */
	private static final long serialVersionUID = -3826279010571161112L;

	public VulnerableServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String user = request.getParameter("user");
		String name = request.getParameter("name");
		String url = request.getParameter("corpTaxAppURL");

		PrintWriter out = response.getWriter();

		try {
            Class.forName(request.getParameter("forName"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            out.println(String.format("<!-- Exception occurred with forName: %s -->", request.getParameter("forName")));
        }

        String username = "system";
		String password = "D4t4b4$3";

        try (Connection connection = DriverManager.getConnection("jdbc:oracle:thin:localhost:1521:xe", username, password)) {
			try (Statement statement = connection.createStatement()) {
			    statement.executeUpdate("INSERT INTO WEB_USE VALUES (WEB_USE_ID_SEQ.NEXTVAL, '" + user + "', '" + name + "')");

                request.getSession().setAttribute("user", user);
            } catch (SQLException e) {
                e.printStackTrace();

                out.println(String.format("<!-- Exception occurred with username: %s, password: %s -->", username, password));
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(String.format("cmd /c dir C:\\Users\\%s", user));

		BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset=\"utf-8\">");
        out.println("<title>");
        out.println(String.format("Thanks %s", name));
        out.println("</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"#f0f0f0\">");
        out.println(String.format("<b>Name:</b> %s", name));
        out.println("<br>");
        out.println(String.format("<b>User ID:</b> %s", user));
        out.println(String.format("<!-- Contents of C:\\Users\\%s -->", user));

		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("SHA1");

            if (md != null) {
                byte[] messageDigest = md.digest(name.getBytes());
                BigInteger number = new BigInteger(1, messageDigest);
                String hash = number.toString(16);

                // Now we need to zero pad it if you actually want the full 32 chars.
                while (hash.length() < 32) {
                    hash = "0" + hash;
                }

                // Alert the user to paste this hash into the employee app to retrieve their W2 tax form.
                request.getSession().setAttribute("sha1", hash);

                out.println("<p>");
                out.println(String.format("Please copy and paste this unique value into the corporate employee tax application to retrieve your W2: %s", hash));
                out.println("</p>");
                out.println("<p>");
                out.println(String.format("Click <a href=\"%s\">here</a> to access the tax application.", url));
                out.println("</p>");
            }
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

        out.println("<pre>");

		String line = null;
		while ((line = input.readLine()) != null) {
			out.println(Encode.forHtml(line));
		}

        out.println("</pre>");
		out.println("</body>");
        out.println("</html>");
		out.close();
	}

	/**
	 * @see VulnerableServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
