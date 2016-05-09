package de.uni_potsdam.hpi.bpt.bp2014.jcore.eventhandling;

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

    private static Logger logger = Logger.getLogger(SseNotifier.class);

    private static EventTarget target;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        target = new ServletEventTarget(req).ok().open();
        logger.info("SSE target registered.");
    }

    public static void notifyRefresh() {
        if (null != target) {
            try {
                target.send("refresh", "");
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
            }
        }
        logger.info("SSE event sent.");
    }
}
