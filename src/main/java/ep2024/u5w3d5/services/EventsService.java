package ep2024.u5w3d5.services;

import ep2024.u5w3d5.entities.Event;
import ep2024.u5w3d5.entities.User;
import ep2024.u5w3d5.exceptions.BadRequestException;
import ep2024.u5w3d5.exceptions.NotFoundException;
import ep2024.u5w3d5.payloads.NewEventDTO;
import ep2024.u5w3d5.repositories.EventsDAO;
import ep2024.u5w3d5.repositories.UsersDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EventsService {

    @Autowired
    private EventsDAO eventsDAO;

    @Autowired
    private UsersDAO usersDAO;

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

    public void findByIdAndDelete(UUID eventId, User currentUser) {
        Event event = eventsDAO.findById(eventId)
                .orElseThrow(() -> new NotFoundException(eventId));

        if (!event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Only the organizer can delete this event.");
        }

        eventsDAO.delete(event);
    }
}
