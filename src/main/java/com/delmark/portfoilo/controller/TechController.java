package com.delmark.portfoilo.controller;

import com.delmark.portfoilo.models.Techs;
import com.delmark.portfoilo.service.interfaces.TechService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tech")
public class TechController {

    TechService techService;

    @GetMapping
    public ResponseEntity<Page<Techs>> getAllPages(@RequestParam("page") int page) {
        return ResponseEntity.ok(techService.getAllTechs(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Techs> getTechById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(techService.getTechsById(id));
    }

    @PostMapping
    ResponseEntity<Techs> createTech(@RequestBody Techs techs) {
        return ResponseEntity.ok(techService.createTech(techs));
    }

    @PutMapping ("/{id}")
    ResponseEntity<Techs> editTech(@PathVariable("id") Long id, @RequestBody Techs techs) {
        return ResponseEntity.ok(techService.editTech(id, techs));
    }

    @DeleteMapping ("/{id}")
    ResponseEntity<Void> deleteTech(@PathVariable("id") Long id) {
        techService.deleteTech(id);
        return ResponseEntity.ok().build();
    }
}
