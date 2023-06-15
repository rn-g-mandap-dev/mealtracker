package com.example.meal2.thoughtrecord.repository;

import com.example.meal2.thoughtrecord.entity.Thought;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThoughtRepository extends JpaRepository<Thought, Long> {
}
