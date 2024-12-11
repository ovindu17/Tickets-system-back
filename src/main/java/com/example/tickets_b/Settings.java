package com.example.tickets_b;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "settings")
class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer maxTickets;
    private Double releaseRate;
    private Double buyingRate;
    private Double ticketPrice;
    private Long adminId;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getMaxTickets() {
        return maxTickets;
    }
    public void setMaxTickets(Integer maxTickets) {
        this.maxTickets = maxTickets;
    }
    public Double getReleaseRate() {
        return releaseRate;
    }
    public void setReleaseRate(Double releaseRate) {
        this.releaseRate = releaseRate;
    }
    public Double getBuyingRate() {
        return buyingRate;
    }
    public void setBuyingRate(Double buyingRate) {
        this.buyingRate = buyingRate;
    }
    public Double getTicketPrice() {
        return ticketPrice;
    }
    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
    public Long getAdminId() {
        return adminId;
    }
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}

interface SettingsRepository extends JpaRepository<Settings, Long> {
    Optional<Settings> findTopByOrderByIdDesc();
}

@Service
class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;

    public List<Settings> findAll() {
        return settingsRepository.findAll();
    }

    public Optional<Settings> findById(Long id) {
        return settingsRepository.findById(id);
    }

    public Settings save(Settings settings) {
        return settingsRepository.save(settings);
    }

    public void deleteById(Long id) {
        settingsRepository.deleteById(id);
    }

    public Optional<Settings> findLatest() {
        return settingsRepository.findTopByOrderByIdDesc();
    }
}

@RestController
@RequestMapping("/settings")
class SettingsController {
    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public List<Settings> getAllSettings() {
        return settingsService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Settings> getSettingsById(@PathVariable Long id) {
        return settingsService.findById(id);
    }

    @PostMapping
    public Settings createSettings(@RequestBody Settings settings) {
        return settingsService.save(settings);
    }

    @PutMapping("/{id}")
    public Settings updateSettings(@PathVariable Long id, @RequestBody Settings settings) {
        settings.setId(id);
        return settingsService.save(settings);
    }

    @DeleteMapping("/{id}")
    public void deleteSettings(@PathVariable Long id) {
        settingsService.deleteById(id);
    }

    @GetMapping("/latest")
    public Optional<Settings> getLatestSettings() {
        return settingsService.findLatest();
    }
}