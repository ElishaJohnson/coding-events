package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventData;
import org.launchcode.codingevents.models.Event;
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
    public String renderCreateEventForm() {
        return "events/create";
    }

    // lives at /events/create
    @PostMapping("create")
    public String createEvent(@ModelAttribute @Valid Event newEvent, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("errorMsg", "Bad data!");
            return "events/create";
        }
        EventData.add(newEvent);
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
        return "events/edit";
    }

    @PostMapping("edit")
    public String processEditForm(@RequestParam int eventId, @RequestParam String name, @RequestParam String description, @RequestParam String contactEmail) {
        EventData.update(eventId, name, description, contactEmail);
        return "redirect:";
    }
}
