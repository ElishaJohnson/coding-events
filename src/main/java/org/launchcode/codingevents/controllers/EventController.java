package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventData;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("events")
public class EventController {

    @GetMapping
    public String displayAllEvents(Model model) {
        model.addAttribute("events", EventData.getAll());
        return "events/index";
    }

    // lives at /events/create
    @GetMapping("create")
    public String renderCreateEventForm(Model model) {
        model.addAttribute(new Event());
        model.addAttribute("formType", "Create Event");
        model.addAttribute("target", "/events/create");
        model.addAttribute("types", EventType.values());
        return "events/form";
    }

    // lives at /events/create
    @PostMapping("create")
    public String createEvent(@ModelAttribute @Valid Event newEvent, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("formType", "Create Event");
            return "events/form";
        }
        EventData.add(new Event(newEvent.getName(),
                newEvent.getType(),
                newEvent.getDescription(),
                newEvent.getLocation(),
                newEvent.getContactEmail(),
                newEvent.getAttendees(),
                newEvent.isRegistrationRequired()));
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteEventForm(Model model) {
        model.addAttribute("events", EventData.getAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String processDeleteEventsForm(@RequestParam(required = false) int[] eventIds) {
        if (eventIds != null) {
            for (int id : eventIds) {
                EventData.remove(id);
            }
        }
        return "redirect:";
    }

    @GetMapping(path = "edit/{eventId}")
    public String displayEditForm(Model model, @PathVariable int eventId) {
        model.addAttribute("event", EventData.getById(eventId));
        model.addAttribute("eventId", eventId);
        model.addAttribute("formType", "Update Event Info");
        model.addAttribute("target", "/events/edit");
        model.addAttribute("types", EventType.values());
        return "events/form";
    }

    @PostMapping("edit")
    public String processEditForm(@ModelAttribute @Valid Event event, Errors errors, @RequestParam int eventId, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("eventId", eventId);
            model.addAttribute("formType", "Update Event Info");
            return "events/form";
        }
        EventData.update(event, eventId);
        return "redirect:";
    }
}
