package com.ohrlings.learning.trainyard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohrlings.learning.trainyard.model.Locomotive;
import com.ohrlings.learning.trainyard.service.LocomotiveDAO;
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

@WebServlet(name = "Locomotive Servlet", urlPatterns = {"/locomotive"})
public class LocomotiveServlet extends HttpServlet {
    private VehicleDAO locomotiveDAO = new LocomotiveDAO();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            if(request.getParameter("vehicleNr") != null) {
                Optional<Locomotive> locomotive = locomotiveDAO.getVehicle(Integer.parseInt(request.getParameter("vehicleNr")));
                if(locomotive.isPresent()) out.println(objectMapper.writeValueAsString(locomotive.get()));
            } else {
                out.println(objectMapper.writeValueAsString(locomotiveDAO.getAll().collect(Collectors.toList())));
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.getMessage());
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        try {
            Locomotive updatedLocomotive = objectMapper.readValue(body, Locomotive.class);
            locomotiveDAO.updateVehicle(updatedLocomotive);
            out.println(objectMapper.writeValueAsString(updatedLocomotive));
        } catch (Exception e) {
            e.printStackTrace();
            out.println(e.getMessage());
        }
        out.close();
    }
}
