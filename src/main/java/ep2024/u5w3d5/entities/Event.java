package ep2024.u5w3d5.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Event {
        @Id
        @GeneratedValue
        private UUID id;

        private String title;
        private String description;
        private LocalDate date;
        private String location;
        private int availableSeats;

        @ManyToOne
        @JoinColumn(name = "organizer_id")
        private User organizer;

        @OneToMany(mappedBy = "event")
        @JsonIgnore
        private List<Reservation> reservations;
}

