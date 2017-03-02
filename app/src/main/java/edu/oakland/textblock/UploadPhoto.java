package edu.oakland.textblock;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadPhoto
 */
@WebServlet("/UploadPhoto")
public class UploadPhoto extends HttpServlet {

    /**
     * Default constructor.
     */
    public UploadPhoto() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Server at " + request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String path = (request).getContextPath();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(factory);

        List<FileItem> list = null;
        try {
            list = fileUpload.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        for (FileItem item : list) {
            if (!item.isFormField()) {
                String imageName = item.getName();
                String targetPath = (request).getRealPath("/photos/" + imageName);
                File photo = new File(targetPath + imageName);
                try {
                    item.write(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("here is the fileds from request.");
            }
        }


        // to print the request
        Enumeration<String> headers = request.getParameterNames();
        System.out.println(headers.toString());


        // to get the fileName from the http request to generate the path of file
        final String filePath = request.getParameter("name");
        System.out.println("path: " + filePath);
        final Part image = request.getPart("file");
        final String fileName = image.getSubmittedFileName();
        System.out.println("name: " + fileName);

        OutputStream outputStream = null;
        InputStream inputStream = null;
        final PrintWriter writer = response.getWriter();

        outputStream = new FileOutputStream(new File(filePath));
        inputStream = image.getInputStream();

        // constantly read from the request and write into a file saved on servers.
        int read = 0;
        final byte[] bytes = new byte[1024];
        while ((read = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
        }
        // close the I/O streams  
        inputStream.close();
        outputStream.close();

        writer.println("New file " + fileName + " created at " + fileName);
        writer.flush();
        writer.close();

    }

}
