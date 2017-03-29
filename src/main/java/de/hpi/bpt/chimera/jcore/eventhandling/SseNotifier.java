package de.hpi.bpt.chimera.jcore.eventhandling;

import info.macias.sse.EventTarget;
import info.macias.sse.servlet3.ServletEventTarget;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(asyncSupported = true)
public class SseNotifier extends HttpServlet {

	private static final String ERROR = "error";
	private static final String WARNING = "warning";
	private static final String REFRESH = "refresh";
	private static Logger logger = Logger.getLogger(SseNotifier.class);
	private static EventTarget target;

	public static void notifyRefresh() {
		sendNotification(REFRESH, "");
	}

	public static void notifyError(String errorMessage) {
		sendNotification(ERROR, errorMessage);
	}

	public static void notifyWarning(String errorMessage) {
		sendNotification(WARNING, errorMessage);
	}

	private static void sendNotification(String type, String message) {
		if (null != target) {
			try {
				target.send(type, message);
				logger.info("SSE event sent.");
			} catch (IOException e) {
				logger.warn(e.getMessage(), e);
			}
		} else {
			logger.warn("No SSE target registered.");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		target = new ServletEventTarget(req).ok().open();
		logger.info("SSE target registered.");
	}
}
