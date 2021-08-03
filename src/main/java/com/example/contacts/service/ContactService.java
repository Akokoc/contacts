package com.example.contacts.service;

import com.example.contacts.entity.Contact;
import com.example.contacts.exeption.ContactNotFoundException;
import com.example.contacts.repo.IContactRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {
    private static final String CONTACT_NOT_FOUND = "Contact not exist";
    private final IContactRepo repo;

    public ContactService(IContactRepo repo) {
        this.repo = repo;
    }

    public List<Contact> getAllContacts() {
        return repo.getAll();
    }

    public Contact getById(int contactId) {
        return repo.getById(contactId)
                .orElseThrow(() -> new ContactNotFoundException(CONTACT_NOT_FOUND));
    }

    public void addContact(String firstName, String lastName, int age) {
        Contact contact = new Contact(firstName, lastName, age);
        repo.add(contact);
    }

    public void editContact(String firstName, String lastName, int age, int contactId) {
        Contact contact = repo.getById(contactId)
                .orElseThrow(() -> new ContactNotFoundException(CONTACT_NOT_FOUND));

        if (firstName != null)
            contact.setFirstName(firstName);
        if (lastName != null)
            contact.setLastName(lastName);
        if (age > 0)
            contact.setAge(age);
    }

    //    TODO дописать
    public List<Contact> searchByName(String name) {
        return repo
                .getAll()
                .stream()
                .filter(contact -> contact.getFirstName().toLowerCase().contains(name.toLowerCase().trim()))
                .collect(Collectors.toList());
    }


    public void deleteById(int contactId) {
        Contact contact = repo.getById(contactId)
                .orElseThrow(() -> new ContactNotFoundException(CONTACT_NOT_FOUND));

        repo.delete(contact);
    }
}
