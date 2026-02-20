package com.fitness.controller;

import com.fitness.domain.model.Bioimpedance;
import com.fitness.repository.BioimpedanceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bioimpedance")
@CrossOrigin(origins = "*")
public class BioimpedanceController {

    private final BioimpedanceRepository repository;

    public BioimpedanceController(BioimpedanceRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Bioimpedance> save(@RequestBody Bioimpedance bio) {
        return ResponseEntity.ok(repository.save(bio));
    }

    @GetMapping("/student/{email}")
    public ResponseEntity<List<Bioimpedance>> getHistory(@PathVariable String email) {
        return ResponseEntity.ok(repository.findByStudentEmailOrderByDateDesc(email));
    }
}
