package com.ohrlings.learning.trainyard.service;

import com.ohrlings.learning.trainyard.exception.CustomException;
import com.ohrlings.learning.trainyard.model.Locomotive;
import com.ohrlings.learning.trainyard.model.Vehicle;

import javax.annotation.ManagedBean;
import java.sql.*;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@ManagedBean
public class LocomotiveDAO implements VehicleDAO {

    private final String url = "secret";
    private final String user = "secret";
    private final String pass = "secret";

    @Override
    public Stream<Locomotive> getAll() {
        Connection connection;
        try {
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM locomotives JOIN locomotive_individuals li on locomotives.littera = li.littera;");
            ResultSet rs = ps.executeQuery();
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<Locomotive>(Long.MAX_VALUE, Spliterator.ORDERED) {
                @Override
                public boolean tryAdvance(Consumer<? super Locomotive> action) {
                    try {
                        if(!rs.next()) {
                            return false;
                        }
                        action.accept(createLocomotive(rs));
                        return true;
                    } catch (SQLException exception) {
                        throw new RuntimeException(exception);
                    }
                }
            }, false).onClose(() -> mutedClose(connection, ps, rs));

        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    private void mutedClose(Connection connection, PreparedStatement ps, ResultSet rs) {
        try {
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    private Locomotive createLocomotive(ResultSet rs) throws SQLException {
        return new Locomotive(
                rs.getInt("vehicle_nr"),
                rs.getString("littera"),
                rs.getInt("clog_weight"),
                rs.getInt("vehicle_weight"),
                rs.getDouble("length"),
                rs.getBoolean("traffic_ready"),
                rs.getInt("dynamic_weight")
        );
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, pass);
    }

    @Override
    public Optional<Locomotive> getVehicle(int id) throws Exception {
        ResultSet rs = null;
        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM locomotives JOIN locomotive_individuals li on locomotives.littera = li.littera WHERE li.vehicle_nr = ?;")) {
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if(rs.next()) {
                return Optional.of(createLocomotive(rs));
            }
            return Optional.empty();
        } catch (SQLException exception) {
            throw new CustomException(exception.getMessage(), exception);
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public boolean updateVehicle(Vehicle vehicle) throws CustomException, Exception {
        return false;
    }
}
