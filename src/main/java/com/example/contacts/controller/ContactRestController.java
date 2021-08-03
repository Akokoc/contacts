package com.example.contacts.controller;


import com.example.contacts.dto.ContactToAddDto;
import com.example.contacts.dto.ContactToDisplayDto;
import com.example.contacts.dto.SearchDto;
import com.example.contacts.entity.Contact;
import com.example.contacts.exeption.ContactNotFoundException;
import com.example.contacts.mapper.ContactMapper;
import com.example.contacts.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rest")
public class ContactRestController {

    private static final String ENTITY_NOT_FOUND_MSG = "Entity not found";

    private final ContactService service;
    private final ContactMapper contactMapper;

    public ContactRestController(ContactService service, ContactMapper contactMapper) {
        this.service = service;
        this.contactMapper = contactMapper;
    }

    @GetMapping("/contacts")
    @ResponseStatus(HttpStatus.OK)
    public List<ContactToDisplayDto> contacts() {

        List<ContactToDisplayDto> contactToDisplayDtos = service
                .getAllContacts()
                .stream()
                .map(contact -> contactMapper.toDto(contact))
                .collect(Collectors.toList());

        return contactToDisplayDtos;
    }

    //Add contact
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void addContact(@RequestBody ContactToAddDto contactToAddDto) {
        service.addContact(contactToAddDto.firstName, contactToAddDto.lastName, contactToAddDto.age);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContactById(@PathVariable(name = "id") int contactId) {
        service.deleteById(contactId);

    }

    @GetMapping("/contacts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactToDisplayDto contactById(@PathVariable(name = "id") int contactId) {
        Contact contact= service.getAllContacts()
                .stream().
                filter(c -> c.getId()==contactId)
                .findFirst().orElseThrow(() -> new ContactNotFoundException(ENTITY_NOT_FOUND_MSG));
        return contactMapper.toDto(contact);
    }


}

