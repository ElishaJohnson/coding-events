package org.launchcode.codingevents.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Entity
public class EventDetails extends AbstractEntity {

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

    private boolean registrationRequired;

    public EventDetails(String description, String location, String contactEmail, int attendees, boolean registrationRequired) {
        this.description = description;
        this.location = location;
        this.contactEmail = contactEmail;
        this.attendees = attendees;
        this.registrationRequired = registrationRequired;
    }

    public EventDetails() {}

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

}
