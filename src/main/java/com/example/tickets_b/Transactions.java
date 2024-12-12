package com.example.tickets_b;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity
@Table(name = "transactions")
class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userType;
    private Integer ticketNo;
    private String action;
    private String username; // New field

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public Integer getTicketNo() {
        return ticketNo;
    }
    public void setTicketNo(Integer ticketNo) {
        this.ticketNo = ticketNo;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}

interface TransactionsRepository extends JpaRepository<Transactions, Long> {
    @Query("SELECT SUM(t.ticketNo) FROM Transactions t WHERE t.action = 'released'")
    Integer sumReleasedTickets();

    @Query("SELECT SUM(t.ticketNo) FROM Transactions t WHERE t.action = 'sold'")
    Integer sumSoldTickets();

    @Query("SELECT SUM(t.ticketNo) FROM Transactions t WHERE t.action = 'unsold'")
    Integer sumUnsoldTickets();
}

@Service
class TransactionsService {
    @Autowired
    private TransactionsRepository transactionsRepository;

    public List<Transactions> findAll() {
        return transactionsRepository.findAll();
    }

    public Optional<Transactions> findById(Long id) {
        return transactionsRepository.findById(id);
    }

    public synchronized Transactions save(Transactions transactions) {
        return transactionsRepository.save(transactions);
    }

    public synchronized void deleteById(Long id) {
        transactionsRepository.deleteById(id);
    }

    public Integer getTotalReleasedTickets() {
        return transactionsRepository.sumReleasedTickets();
    }

    public Integer getTotalSoldTickets() {
        return transactionsRepository.sumSoldTickets();
    }

    public Integer getTotalUnsoldTickets() {
        Integer released = getTotalReleasedTickets();
        Integer sold = getTotalSoldTickets();
        if (released != null && sold != null) {
            return released - sold;
        } else if (released != null) {
            return released;
        } else {
            return null;
        }
    }
}

@RestController
@RequestMapping("/transactions")
class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @GetMapping
    public List<Transactions> getAllTransactions() {
        return transactionsService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Transactions> getTransactionsById(@PathVariable Long id) {
        return transactionsService.findById(id);
    }

    @PostMapping
    public Transactions createTransactions(@RequestBody Transactions transactions) {
        return transactionsService.save(transactions);
    }

    @PutMapping("/{id}")
    public Transactions updateTransactions(@PathVariable Long id, @RequestBody Transactions transactions) {
        transactions.setId(id);
        return transactionsService.save(transactions);
    }

    @DeleteMapping("/{id}")
    public void deleteTransactions(@PathVariable Long id) {
        transactionsService.deleteById(id);
    }

    @GetMapping("/totals")
    public Map<String, Integer> getTotalTickets() {
        Map<String, Integer> totals = new HashMap<>();
        totals.put("released", transactionsService.getTotalReleasedTickets());
        totals.put("sold", transactionsService.getTotalSoldTickets());
        totals.put("unsold", transactionsService.getTotalUnsoldTickets());
        return totals;
    }
}