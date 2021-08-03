package com.example.contacts.controller;

import com.example.contacts.dto.ContactToAddDto;
import com.example.contacts.dto.ContactToDisplayDto;
import com.example.contacts.dto.SearchDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.mapper.ContactMapper;
import com.example.contacts.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ContactController {

    private final ContactService service;
    private final ContactMapper contactMapper;

    public ContactController(ContactService service, ContactMapper contactMapper) {
        this.service = service;
        this.contactMapper = contactMapper;
    }

    @RequestMapping(value = "/contacts", method = RequestMethod.GET)
//    @GetMapping("contacts")
    public String contacts(Model model) {

        List<ContactToDisplayDto> contactToDisplayDtos = service
                .getAllContacts()
                .stream()
                .map(contact -> contactMapper.toDto(contact))
                .collect(Collectors.toList());

        model.addAttribute("contacts", contactToDisplayDtos);

        return "contacts";
    }


//    TODO создать новый эндпоинт, который будет обрабатывать поисковой запрос
//     и возвращать темплейт с отфильтрованым списком
//     @RequestMapping(value = "contacts/search", method = RequestMethod.POST)

    @PostMapping("/contacts/search")
    public String seachContacts(@ModelAttribute SearchDto searchDto, Model model){
        List<ContactToDisplayDto> contactToDisplayDtos=service.searchByName(searchDto.searchName)
                .stream()
                .map(contact -> contactMapper.toDto(contact))
                .collect(Collectors.toList());

        model.addAttribute("contacts",contactToDisplayDtos);
        return "contacts";
    }


    @RequestMapping(value = "/contact-info/{id}", method = RequestMethod.GET)
    public String contactDetail(@PathVariable(name = "id") int contactId, Model model) {

        ContactToDisplayDto contactToDisplayDto = contactMapper.toDto(service.getById(contactId));
        model.addAttribute("contact", contactToDisplayDto);

        return "contact-details";
    }

    @RequestMapping(value = "/contacts/form", method = RequestMethod.GET)
    public String contactForm(Model model) {

        model.addAttribute("contact", new Contact("","",0));

        return "contact-form";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editContact(@PathVariable(name = "id") int contactId, Model model) {

        Contact contact = service.getById(contactId);
        model.addAttribute("contact", contact);

        return "contact-form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveContact(@ModelAttribute Contact contact) {

        if (contact.getId() > 0)
            service.editContact(contact.getFirstName(), contact.getLastName(), contact.getAge(), contact.getId());
        else
            service.addContact(contact.getFirstName(), contact.getLastName(), contact.getAge());

        return "redirect:/contacts";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteContact(@PathVariable(name = "id") int contactId) {
        service.deleteById(contactId);
        return "redirect:/contacts";
    }

}
