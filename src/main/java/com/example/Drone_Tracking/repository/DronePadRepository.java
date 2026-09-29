package com.example.Drone_Tracking.repository;

import com.example.Drone_Tracking.model.DronePad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DronePadRepository extends JpaRepository<DronePad, Long> {}