package com.example.contacts.repo;

import com.example.contacts.entity.Contact;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
//Дописать все методы
public class ContactRepo implements IContactRepo{
    private List<Contact> contacts = new ArrayList<>();

    @Override
    public void add(Contact contact) {
        contact.setId(generateId());
        contacts.add(contact);
    }

    @Override
    public List<Contact> getAll() {
        return contacts;
    }

    @Override
    public Optional<Contact> getById(int id) {
        return contacts
                .stream()
                .filter(contact -> contact.getId() == id)
                .findFirst();
    }

    @Override
    public void delete(Contact contact) {
        contacts.remove(contact);
    }

    private int generateId() {
        int id;
        if (contacts.size() > 0) {
            int lastId = contacts.get(contacts.size() - 1).getId();
            id = ++lastId;
        } else {
            id = 1;
        }
        return id;
    }
}
