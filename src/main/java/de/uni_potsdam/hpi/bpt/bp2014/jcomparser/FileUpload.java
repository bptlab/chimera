package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import javax.servlet.http.HttpServlet;

/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */


/*
As a part of the JComparser we need to provide a manual XML File upload.
 */

/**
 * TODO: do we want to delete this?
 */
public class FileUpload extends HttpServlet {

 /*
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final List pcm = new ArrayList();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h1>JEngine</h1><br>");

        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent) {
            out.println("You are not trying to upload<br/>");
            return;
        }
        out.println("Upload completed.<br/>");

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> fields = upload.parseRequest(request);
            Iterator<FileItem> it = fields.iterator();
            if (!it.hasNext()) {
                out.println("ERROR: No fields found");
                return;
            }
            out.println("<table border=\"1\">");
            while (it.hasNext()) {
                out.println("<tr>");
                FileItem fileItem = it.next();
                //pcm.add(fileItem);
                pcm.add(fileItem.getString());

                boolean isFormField = fileItem.isFormField();
                if (isFormField) {
                    out.println("<td>regular form field</td><td>FIELD NAME: " + fileItem.getFieldName() +
                                    "<br/>STRING: " + fileItem.getString()
                    );
                    out.println("</td>");
                } else {
                    out.println("<td>file form field</td><td>FIELD NAME: " + fileItem.getFieldName() +
                                    "<br/>STRING: " + fileItem.getString() +
                                    "<br/>NAME: " + fileItem.getName() +
                                    "<br/>CONTENT TYPE: " + fileItem.getContentType() +
                                    "<br/>SIZE (BYTES): " + fileItem.getSize() +
                                    "<br/>TO STRING: " + fileItem.toString()
                    );
                    out.println("</td>");
                }
                out.println("</tr>");
            }
            out.println("</table><br><br>");
            out.println("<center><h4>Processing your data</h4><br><img src=\"loading.gif\" alt=\"loading\"> </center>");
        } catch (FileUploadException e) {
            log.error("Error:", e);
        }

        // Calling the postprocessing asynchronously

        *//* we aint support java 1.8 :(
        Thread a = new Thread(() -> {
            de.uni_potsdam.hpi.bpt.bp2014.jcomparser.JComparser.handleFileUpload(pcm);
        });
        *//*

        new Thread()
        {
            public void run() {
                de.uni_potsdam.hpi.bpt.bp2014.jcomparser.JComparser.handleFileUpload(pcm);
            }
        }.start();
    }
    */
}
