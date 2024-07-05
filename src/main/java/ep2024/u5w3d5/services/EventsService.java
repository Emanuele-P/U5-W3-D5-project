package ep2024.u5w3d5.services;

import ep2024.u5w3d5.entities.Event;
import ep2024.u5w3d5.entities.Reservation;
import ep2024.u5w3d5.entities.User;
import ep2024.u5w3d5.exceptions.BadRequestException;
import ep2024.u5w3d5.exceptions.NotFoundException;
import ep2024.u5w3d5.payloads.NewEventDTO;
import ep2024.u5w3d5.repositories.EventsDAO;
import ep2024.u5w3d5.repositories.ReservationsDAO;
import ep2024.u5w3d5.repositories.UsersDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventsService {

    @Autowired
    private EventsDAO eventsDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private ReservationsDAO reservationsDAO;

    @Transactional
    public Event createEvent(NewEventDTO newEventDTO, User currentUser) {
        Event newEvent = new Event();
        newEvent.setTitle(newEventDTO.title());
        newEvent.setDescription(newEventDTO.description());
        newEvent.setDate(newEventDTO.date());
        newEvent.setLocation(newEventDTO.location());
        newEvent.setAvailableSeats(newEventDTO.availableSeats());
        newEvent.setOrganizer(currentUser);

        return eventsDAO.save(newEvent);
    }

    @Transactional
    public Event updateEvent(UUID eventId, NewEventDTO updatedEventDTO, User currentUser) {
        Event event = eventsDAO.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Only the organizer can update this event.");
        }

        event.setTitle(updatedEventDTO.title());
        event.setDescription(updatedEventDTO.description());
        event.setDate(updatedEventDTO.date());
        event.setLocation(updatedEventDTO.location());
        event.setAvailableSeats(updatedEventDTO.availableSeats());

        return eventsDAO.save(event);
    }

    @Transactional
    public void findByIdAndDelete(UUID eventId, User currentUser) {
        Event event = eventsDAO.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Only the organizer can delete this event.");
        }

        List<Reservation> reservations = reservationsDAO.findByEventId(eventId);
        reservations.forEach(reservation -> reservationsDAO.delete(reservation));
        eventsDAO.delete(event);
    }

    @Transactional
    public Page<Event> getAllEvents(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 20) pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        return eventsDAO.findAll(pageable);
    }

    @Transactional
    public void reserveEvent(UUID eventId, User currentUser) {
        Event event = eventsDAO.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));

        if (event.getAvailableSeats() <= 0) {
            throw new BadRequestException("No available seats for this event.");
        }

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventsDAO.save(event);

        Reservation reservation = new Reservation();
        reservation.setEvent(event);
        reservation.setUser(currentUser);
        reservationsDAO.save(reservation);
    }

    @Transactional
    public List<Reservation> getUserReservations(User currentUser) {
        return reservationsDAO.findByUser(currentUser);
    }
}
