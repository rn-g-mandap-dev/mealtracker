package com.example.meal2.aftermealnote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AfterMealNoteRepository extends JpaRepository<AfterMealNote, Long> {
}
