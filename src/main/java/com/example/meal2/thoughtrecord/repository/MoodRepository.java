package com.example.meal2.thoughtrecord.repository;

import com.example.meal2.thoughtrecord.entity.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodRepository extends JpaRepository<Mood, Long> {
}
