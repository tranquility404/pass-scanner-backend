package com.hackathon.gatepass.controller;

import com.hackathon.gatepass.dto.CreatePassRequest;
import com.hackathon.gatepass.dto.PassResponse;
import com.hackathon.gatepass.dto.VerifyRequest;
import com.hackathon.gatepass.service.PassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passes")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PassResponse> createPass(@Valid @RequestBody CreatePassRequest request) {
        PassResponse response = passService.createPass(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/scan")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'SCANNER')")
    public ResponseEntity<PassResponse> scanPass(@RequestParam String code) {
        PassResponse response = passService.getPassByCode(code);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/verify-entry")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PassResponse> verifyEntry(
            @PathVariable String id,
            @Valid @RequestBody VerifyRequest request) {
        PassResponse response = passService.verifyEntry(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/give-goodies")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PassResponse> giveGoodies(
            @PathVariable String id,
            @Valid @RequestBody VerifyRequest request) {
        PassResponse response = passService.giveGoodies(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PassResponse>> getAllPasses() {
        List<PassResponse> responses = passService.getAllPasses();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/filter")
//    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<PassResponse>> getFilteredPasses(
            @RequestParam(required = false) Boolean entryVerified,
            @RequestParam(required = false) Boolean goodiesGiven,
            @RequestParam(required = false) String verifiedBy,
            @RequestParam(required = false) String goodiesGivenBy) {
        List<PassResponse> responses = passService.getFilteredPasses(
                entryVerified, goodiesGiven, verifiedBy, goodiesGivenBy);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePass(@PathVariable String id) {
        passService.deletePass(id);
        return ResponseEntity.noContent().build();
    }
}
