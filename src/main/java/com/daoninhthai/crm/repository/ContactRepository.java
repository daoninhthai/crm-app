package com.daoninhthai.crm.repository;

import com.daoninhthai.crm.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findByCompanyId(Long companyId);

    List<Contact> findByStatus(Contact.ContactStatus status);

    Page<Contact> findByStatus(Contact.ContactStatus status, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Contact> searchByNameOrEmail(@Param("keyword") String keyword, Pageable pageable);

    boolean existsByEmail(String email);

    long countByStatus(Contact.ContactStatus status);
}
