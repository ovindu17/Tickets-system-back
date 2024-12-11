package com.example.tickets_b;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;




@Entity
@Table(name = "buyer")

  class Buyer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

interface BuyerRepository extends JpaRepository<Buyer, Long> {
}

@Service
class BuyerService {
    @Autowired
    private BuyerRepository buyerRepository;

    public List<Buyer> findAll() {
        return buyerRepository.findAll();
    }

    public Optional<Buyer> findById(Long id) {
        return buyerRepository.findById(id);
    }

    public Buyer save(Buyer buyer) {
        return buyerRepository.save(buyer);
    }

    public void deleteById(Long id) {
        buyerRepository.deleteById(id);
    }
}

@RestController
@RequestMapping("/buyers")
 class BuyerController {
    @Autowired
    private BuyerService buyerService;

    @GetMapping
    public List<Buyer> getAllBuyers() {
        return buyerService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Buyer> getBuyerById(@PathVariable Long id) {
        return buyerService.findById(id);
    }

    @PostMapping
    public Buyer createBuyer(@RequestBody Buyer buyer) {
        return buyerService.save(buyer);
    }

    @PutMapping("/{id}")
    public Buyer updateBuyer(@PathVariable Long id, @RequestBody Buyer buyer) {
        buyer.setId(id);
        return buyerService.save(buyer);
    }

    @DeleteMapping("/{id}")
    public void deleteBuyer(@PathVariable Long id) {
        buyerService.deleteById(id);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Long>> login(@RequestBody Buyer buyer) {
        Optional<Buyer> existingBuyer = buyerService.findAll().stream()
                .filter(b -> b.getUsername().equals(buyer.getUsername()) && b.getPassword().equals(buyer.getPassword()))
                .findFirst();

        if (existingBuyer.isPresent()) {
            Map<String, Long> response = new HashMap<>();
            response.put("id", existingBuyer.get().getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}