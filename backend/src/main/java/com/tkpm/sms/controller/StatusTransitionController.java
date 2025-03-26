package com.tkpm.sms.controller;

import com.tkpm.sms.dto.StatusTransitionDto;
import com.tkpm.sms.service.StatusTransitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status/change")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class StatusTransitionController {
    private final StatusTransitionService statusTransitionService;

    @GetMapping
    public ResponseEntity<List<StatusTransitionDto>> getAllTransitions() {
        return ResponseEntity.ok(statusTransitionService.getAllTransitions());
    }

    @GetMapping("/from-status/{statusId}")
    public ResponseEntity<List<StatusTransitionDto>> getTransitionsFromStatus(@PathVariable Integer statusId) {
        return ResponseEntity.ok(statusTransitionService.getTransitionsFromStatus(statusId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusTransitionDto> getTransition(@PathVariable Integer id) {
        return ResponseEntity.ok(statusTransitionService.getTransition(id));
    }

    @PostMapping
    public ResponseEntity<StatusTransitionDto> createTransition(@Valid @RequestBody StatusTransitionDto transitionDTO) {
        StatusTransitionDto createdTransition = statusTransitionService.createTransition(transitionDTO);
        return new ResponseEntity<>(createdTransition, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransition(@PathVariable Integer id) {
        statusTransitionService.deleteTransition(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkTransitionAllowed(
            @RequestParam Integer fromStatusId, 
            @RequestParam Integer toStatusId) {
        boolean allowed = statusTransitionService.isTransitionAllowed(fromStatusId, toStatusId);
        return ResponseEntity.ok(allowed);
    }
}