package org.launchcode.codingevents.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Event {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")
    private String name;

    private EventType type;

    @Size(max = 500, message = "Description too long!")
    private String description;

    @NotBlank(message = "Location is required.")
    @Size(min = 3, max = 50, message = "Location must be between 3 and 50 characters.")
    private String location;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email. Try again.")
    private String contactEmail;

    @Positive(message = "Event must have attendees!")
    private int attendees;

//    @AssertTrue(message = "Field must be true")
    private boolean registrationRequired;

    public Event(String name, EventType type, String description, String location, String contactEmail, int attendees, boolean registrationRequired) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.location = location;
        this.contactEmail = contactEmail;
        this.attendees = attendees;
        this.registrationRequired = registrationRequired;
    }

    public Event() {}

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public EventType getType() { return type; }
    public void setType(EventType type) { this.type = type; }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public int getAttendees() { return attendees; }
    public void setAttendees(int attendees) { this.attendees = attendees; }
    public boolean isRegistrationRequired() { return registrationRequired; }
    public void setRegistrationRequired(boolean registrationRequired) { this.registrationRequired = registrationRequired; }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
