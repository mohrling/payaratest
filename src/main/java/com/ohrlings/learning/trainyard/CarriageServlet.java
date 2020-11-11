package com.ohrlings.learning.trainyard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohrlings.learning.trainyard.model.Carriage;
import com.ohrlings.learning.trainyard.model.Vehicle;
import com.ohrlings.learning.trainyard.service.CarriageDAO;
import com.ohrlings.learning.trainyard.service.VehicleDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "Carriage Servlet", urlPatterns = {"/carriage"})
public class CarriageServlet extends HttpServlet {
    private VehicleDAO carriageDAO = new CarriageDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if(request.getParameter("vehicleNr") != null) {
                Optional<Vehicle> carriage = carriageDAO.getVehicle(Integer.parseInt(request.getParameter("vehicleNr")));
                if (carriage.isPresent()) out.println(objectMapper.writeValueAsString(carriage.get()));
            } else {
                out.println(objectMapper.writeValueAsString(carriageDAO.getAll().collect(Collectors.toList())));
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.getMessage());
            out.flush();
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        try {
            Carriage updatedCarriage = objectMapper.readValue(body, Carriage.class);
            carriageDAO.updateVehicle(updatedCarriage);
            out.println(objectMapper.writeValueAsString(updatedCarriage));
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.getMessage());
        }
        out.close();
    }
}
