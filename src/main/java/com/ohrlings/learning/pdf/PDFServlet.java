package com.ohrlings.learning.pdf;

import com.ohrlings.learning.pdf.worker.GeneratePdf;
import com.ohrlings.learning.trainyard.model.Vehicle;
import com.ohrlings.learning.trainyard.service.CarriageDAO;
import com.ohrlings.learning.trainyard.service.VehicleDAO;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@WebServlet(name = "PDFServlet", urlPatterns = {"/pdf"})
public class PDFServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        VehicleDAO carriageDAO = new CarriageDAO();
        response.setContentType("application/pdf;charset=UTF-8");
        response.addHeader("Content-Disposition", "inline; filename=" + "cities.pdf");
        ServletOutputStream out = response.getOutputStream();
        Stream<Vehicle> vehiclesStream = carriageDAO.getAll();
        ByteArrayOutputStream baos = GeneratePdf.getPdfFile(vehiclesStream.collect(Collectors.toList()));
        baos.writeTo(out);
    }
}
