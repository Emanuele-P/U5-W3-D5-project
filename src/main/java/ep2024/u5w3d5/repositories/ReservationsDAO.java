package ep2024.u5w3d5.repositories;

import ep2024.u5w3d5.entities.Reservation;
import ep2024.u5w3d5.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationsDAO extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByEventId(UUID eventId);
}
