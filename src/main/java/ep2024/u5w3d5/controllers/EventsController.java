package ep2024.u5w3d5.controllers;

import ep2024.u5w3d5.entities.Event;
import ep2024.u5w3d5.entities.User;
import ep2024.u5w3d5.exceptions.BadRequestException;
import ep2024.u5w3d5.payloads.NewEventDTO;
import ep2024.u5w3d5.payloads.NewEventResponseDTO;
import ep2024.u5w3d5.services.EventsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

    // POST http://localhost:3001/events
    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZER')")
    @ResponseStatus(HttpStatus.CREATED)
    public NewEventResponseDTO createEvent(@RequestBody @Valid NewEventDTO newEventDTO, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Event createdEvent = eventsService.createEvent(newEventDTO, currentUser);
        return new NewEventResponseDTO(createdEvent.getId());
    }

    // PUT http://localhost:3001/events/{id}
    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public ResponseEntity<Event> updateEvent(@PathVariable UUID eventId, @RequestBody @Valid NewEventDTO updatedEventDTO, BindingResult validationResult, Authentication authentication) {
        if (validationResult.hasErrors()) {
            throw new BadRequestException(validationResult.getAllErrors());
        }
        User currentUser = (User) authentication.getPrincipal();
        Event updatedEvent = eventsService.updateEvent(eventId, updatedEventDTO, currentUser);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    // DELETE http://localhost:3001/events/{id}
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable UUID eventId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        eventsService.findByIdAndDelete(eventId, currentUser);
    }

    // GET http://localhost:3001/events
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Event> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date") String sortBy
    ) {
        return eventsService.getAllEvents(page, size, sortBy);
    }

    // POST http://localhost:3001/events/{id}/reservations
    @PostMapping("/{eventId}/reservations")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.OK)
    public void reserveEvent(@PathVariable UUID eventId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        eventsService.reserveEvent(eventId, currentUser);
    }
}
