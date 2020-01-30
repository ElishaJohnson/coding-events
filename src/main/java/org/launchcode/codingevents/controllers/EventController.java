package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.EventRepository;
import org.launchcode.codingevents.data.EventCategoryRepository;
import org.launchcode.codingevents.data.TagRepository;
import org.launchcode.codingevents.models.Event;
import org.launchcode.codingevents.models.EventCategory;
import org.launchcode.codingevents.models.Tag;
import org.launchcode.codingevents.models.dto.EventTagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping
    public String displayAllEvents(@RequestParam(required = false) Integer categoryId,
                                   @RequestParam(required = false) Integer tagId,
                                   Model model) {
        if (categoryId == null && tagId == null) {
            model.addAttribute("title", "All Events");
            model.addAttribute("events", eventRepository.findAll());
        } else if (categoryId != null) {
            Optional<EventCategory> result = eventCategoryRepository.findById(categoryId);
            if (result.isEmpty()) {
                model.addAttribute("title", "Invalid Category ID: " + categoryId);
            } else {
                EventCategory category = result.get();
                model.addAttribute("title", "Events in category: " + category.getName());
                model.addAttribute("events", category.getEvents());
            }
        } else if (tagId != null) {
            Optional<Tag> result = tagRepository.findById(tagId);
            if (result.isEmpty()) {
                model.addAttribute("title", "Invalid Tag ID: " + tagId);
            } else {
                Tag tag = result.get();
                model.addAttribute("title", "Events with tag: " + tag.getDisplayName());
                model.addAttribute("events", tag.getEvents());
            }
        }
        return "events/index";
    }

    @GetMapping("details")
    public String displayEventDetails(@RequestParam Integer eventId, Model model) {
//        if (errors.hasErrors()) {
//            return "events/index";
//        }
        Optional<Event> result = eventRepository.findById(eventId);
        if (result.isEmpty()) {
            return "events/index";
        }
        Event event = result.get();
        model.addAttribute("title", "Details for event: " + event.getName());
        model.addAttribute("event", event);
        model.addAttribute("tags", event.getTags());
        return "events/details";
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
            model.addAttribute("categories", eventCategoryRepository.findAll());
            return "events/form";
        }
        eventRepository.save(event);
        eventRepository.deleteById(eventId);
        return "redirect:";
    }

    @GetMapping("add-tag")
    public String displayAddTagForm(@RequestParam Integer eventId, Model model) {
        Optional<Event> result = eventRepository.findById(eventId);
        Event event = result.get();
        EventTagDTO eventTag = new EventTagDTO();
        eventTag.setEvent(event);
        model.addAttribute("eventTag", eventTag);
        model.addAttribute("title", "Add Tag to: " + event.getName());
        model.addAttribute("tags", tagRepository.findAll());
        model.addAttribute("event", event);
        return "events/add-tag";
    }

    @PostMapping("add-tag")
    public String processAddTagForm(@ModelAttribute @Valid EventTagDTO eventTag, Errors errors, Model model) {
        if (!errors.hasErrors()) {
            Event event = eventTag.getEvent();
            Tag tag = eventTag.getTag();
            if (!event.getTags().contains(tag)) {
                event.addTag(tag);
                eventRepository.save(event);
            }
            return "redirect:/events/details/?eventId=" + event.getId();
        }
        return "redirect:add-tag";
    }
}
