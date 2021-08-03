package com.example.contacts.repo;

import com.example.contacts.entity.Contact;

import java.util.List;
import java.util.Optional;

public interface IContactRepo {
    void add(Contact contact);

    List<Contact> getAll();

    Optional<Contact> getById(int id);

    void delete(Contact contact);

}
