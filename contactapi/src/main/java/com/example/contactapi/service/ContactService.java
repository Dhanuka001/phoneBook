package com.example.contactapi.service;

import com.example.contactapi.domain.Contact;
import com.example.contactapi.repository.ContactRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public Page<Contact> getAllContacts(int page , int size){
        return contactRepository.findAll(PageRequest.of(page, size , Sort.by("name")));

    }

    public Contact getContact(String id){
        return contactRepository.findById(id).orElseThrow(() -> new RuntimeException("contact not found"));
    }

    public Contact createContact(Contact contact){
        return contactRepository.save(contact);
    }

    public void deleteContact(Contact contact){
        contactRepository.delete(contact);
    }

    public String uploadPhoto(String id , MultipartFile file){
        Contact contact = getContact(id);
        String photoUrl = null;
        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;

    }

    private final BiFunction<String, MultipartFile, String> photoFunction = (id,image)->{
        try{
            Path fileStorageLocation = Paths.get("").toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(id + ""), REPLACE_EXISTING);
        }catch(Exception exception){
            throw new RuntimeException("unable to save image");
        }
    };
}
