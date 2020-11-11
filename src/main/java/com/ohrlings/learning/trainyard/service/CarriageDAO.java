package com.ohrlings.learning.trainyard.service;

import com.ohrlings.learning.trainyard.exception.CustomException;
import com.ohrlings.learning.trainyard.model.Carriage;
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
public class CarriageDAO implements VehicleDAO {


    private final String url = "secret";
    private final String user = "secret";
    private final String pass = "secret";

    @Override
    public Stream<Carriage> getAll() {
        Connection connection;

        try {
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM carriages JOIN carriage_individuals ci on carriages.littera = ci.littera;");
            ResultSet rs = ps.executeQuery();
            return StreamSupport.stream(new Spliterators.AbstractSpliterator<Carriage>(Long.MAX_VALUE, Spliterator.ORDERED) {

                @Override
                public boolean tryAdvance(Consumer<? super Carriage> action) {
                    try {
                        if(!rs.next()) {
                            return false;
                        }
                        action.accept(createCarriage(rs));
                        return true;
                    } catch (SQLException exception) {
                        throw new RuntimeException(exception);
                    }
                }
            }, false).onClose(() -> mutedClose(connection, ps, rs));
        } catch (SQLException | ClassNotFoundException exception) {
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

    private Carriage createCarriage(ResultSet rs) throws SQLException {
        return new Carriage(
                rs.getInt("vehicle_nr"),
                rs.getString("littera"),
                rs.getDouble("length"),
                rs.getBoolean("cleaned"),
                rs.getDate("comfort_checked"),
                rs.getBoolean("traffic_ready"),
                rs.getInt("vehicle_weight"),
                rs.getInt("clog_weight"),
                rs.getInt("first_class"),
                rs.getInt("second_class"),
                rs.getBoolean("disc_brakes")
        );
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(url, user, pass);
    }

    @Override
    public Optional<Carriage> getVehicle(int id) throws Exception {
        ResultSet rs = null;

        try(Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM carriages JOIN carriage_individuals ci on carriages.littera = ci.littera WHERE ci.vehicle_nr = ?;")) {
            statement.setInt(1, id);
            rs = statement.executeQuery();
            if(rs.next()) {
                return Optional.of(createCarriage(rs));
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
    public boolean updateVehicle(Vehicle vehicle) throws Exception {
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement("UPDATE carriage_individuals SET comfort_checked = ?, cleaned = ?, traffic_ready = ? WHERE vehicle_nr = ?;")) {
            ps.setDate(1, (Date) ((Carriage) vehicle).getComfortChecked());
            ps.setBoolean(2, ((Carriage) vehicle).isCleaned());
            ps.setBoolean(3, vehicle.isClearedForTraffic());
            ps.setInt(4, vehicle.getVehicleNr());
            return ps.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new CustomException(exception.getMessage(), exception);
        }
    }
}
