package com.example.Drone_Tracking.repository;

import com.example.Drone_Tracking.model.DockingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DockingSessionRepository extends JpaRepository<DockingSession, Long> {}