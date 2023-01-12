package nextstep.reservations.domain.repository.reservation;

import nextstep.reservations.domain.entity.reservation.Reservation;

import java.sql.*;

public interface ReservationRepository {
    Long add(Reservation reservation);

    Reservation findById(Long id);

    void remove(final Long id);

    default PreparedStatement getInsertOnePstmt(Connection connection, Reservation reservation) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(ReservationQuery.INSERT_ONE.get(), new String[]{"id"});
        pstmt.setDate(1, Date.valueOf(reservation.getDate()));
        pstmt.setTime(2, Time.valueOf(reservation.getTime()));
        pstmt.setString(3, reservation.getName());
        pstmt.setString(4, reservation.getTheme().getName());
        pstmt.setString(5, reservation.getTheme().getDesc());
        pstmt.setInt(6, reservation.getTheme().getPrice());

        return pstmt;
    }
}