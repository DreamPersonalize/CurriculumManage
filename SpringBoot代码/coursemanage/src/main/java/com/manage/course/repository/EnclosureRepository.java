package com.manage.course.repository;

import com.manage.course.entity.Enclosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnclosureRepository extends JpaRepository<Enclosure, Integer> {
}
