package de.heardis.vaadin.custom;

import java.io.BufferedWriter;
import java.io.IOException;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.ui.Window;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet implementation class CustomComponentsApplicationServlet
 */
@WebServlet("/cc")
public class CustomComponentsApplicationServlet extends ApplicationServlet implements Servlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void writeAjaxPageHtmlVaadinScripts(Window window,
			String themeName, Application application, BufferedWriter page,
			String appUrl, String themeUri, String appId,
			HttpServletRequest request) throws ServletException, IOException {
		super.writeAjaxPageHtmlVaadinScripts(window, themeName, application, page,
				appUrl, themeUri, appId, request);
		page.write("<script type=\"text/javascript\">\n");
		page.write("//<![CDATA[\n");
		page.write("document.write(\"<script language='javascript' src='VAADIN/js/jquery-1.7.1.min.js'><\\/script>\");\n");
		page.write("document.write(\"<script language='javascript' src='VAADIN/js/jquery-ui.min.js'><\\/script>\");\n");
		page.write("document.write(\"<script language='javascript' src='VAADIN/js/jquery.jplayer.min.js'><\\/script>\");\n");
		page.write("//]]>\n</script>\n");
	}
}
