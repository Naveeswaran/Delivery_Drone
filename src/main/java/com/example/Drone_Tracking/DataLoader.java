package com.example.Drone_Tracking;

import com.example.Drone_Tracking.model.*;
import com.example.Drone_Tracking.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initData(DronePadRepository padRepo, OperatorRepository opRepo) {
        return args -> {
            if (padRepo.count() == 0) {
                padRepo.save(new DronePad("PAD-ALPHA-01", "Air Corridor Sector 1", 60.0, "AVAILABLE"));
                padRepo.save(new DronePad("PAD-BETA-02", "Air Corridor Sector 2", 80.0, "AVAILABLE"));
                padRepo.save(new DronePad("PAD-GAMMA-03", "Air Corridor Sector 1", 50.0, "AVAILABLE"));
            }

            if (opRepo.count() == 0) {
                opRepo.save(new Operator("SkyWings Cargo", "CUSTOMER", "ops@skywings.com"));
                opRepo.save(new Operator("Metro Drone Express", "CUSTOMER", "contact@metrone.com"));
                opRepo.save(new Operator("Vertiport Radar & Pad Services", "VENDOR", "billing@radartech.com"));
            }
        };
    }
}