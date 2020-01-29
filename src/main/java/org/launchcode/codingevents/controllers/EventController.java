package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventData;
import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @GetMapping
    public String displayAllEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "events/index";
    }

    // lives at /events/create
    @GetMapping("create")
    public String renderCreateEventForm(Model model) {
        model.addAttribute(new Event());
        model.addAttribute("formType", "Create Event");
        model.addAttribute("target", "/events/create");
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "events/form";
    }

    // lives at /events/create
    @PostMapping("create")
    public String createEvent(@ModelAttribute @Valid Event newEvent, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("formType", "Create Event");
            model.addAttribute("categories", eventCategoryRepository.findAll());
            return "events/form";
        }
        eventRepository.save(newEvent);
        return "redirect:";
    }

    @GetMapping("delete")
    public String displayDeleteEventForm(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events/delete";
    }

    @PostMapping("delete")
    public String processDeleteEventsForm(@RequestParam(required = false) int[] eventIds) {
        if (eventIds != null) {
            for (int id : eventIds) {
                eventRepository.deleteById(id);
            }
        }
        return "redirect:";
    }

    @GetMapping(path = "edit/{eventId}")
    public String displayEditForm(Model model, @PathVariable int eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        model.addAttribute("event", event.get());
        model.addAttribute("eventId", eventId);
        model.addAttribute("formType", "Update Event Info");
        model.addAttribute("target", "/events/edit");
        model.addAttribute("categories", eventCategoryRepository.findAll());
        return "events/form";
    }

    @PostMapping("edit")
    public String processEditForm(@ModelAttribute @Valid Event event, Errors errors, @RequestParam int eventId, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("eventId", eventId);
            model.addAttribute("formType", "Update Event Info");
            return "events/form";
        }
        eventRepository.save(event);
        eventRepository.deleteById(eventId);
        return "redirect:";
    }
}
