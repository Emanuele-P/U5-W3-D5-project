package ep2024.u5w3d5.repositories;

import ep2024.u5w3d5.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventsDAO extends JpaRepository<Event, UUID> {
}
