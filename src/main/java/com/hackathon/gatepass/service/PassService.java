package com.hackathon.gatepass.service;

import com.hackathon.gatepass.dto.CreatePassRequest;
import com.hackathon.gatepass.dto.PassResponse;
import com.hackathon.gatepass.dto.VerifyRequest;
import com.hackathon.gatepass.exception.DuplicatePassCodeException;
import com.hackathon.gatepass.exception.PassAlreadyVerifiedException;
import com.hackathon.gatepass.exception.PassNotFoundException;
import com.hackathon.gatepass.model.Pass;
import com.hackathon.gatepass.repository.PassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassService {

    private final PassRepository passRepository;
    private final MongoTemplate mongoTemplate;

    public PassResponse createPass(CreatePassRequest request) {
        if (passRepository.existsByPassCode(request.getPassCode())) {
            throw new DuplicatePassCodeException("Pass code already exists: " + request.getPassCode());
        }

        Pass pass = Pass.builder()
                .passCode(request.getPassCode())
                .teamName(request.getTeamName())
                .entryVerified(false)
                .goodiesGiven(false)
                .createdAt(Instant.now())
                .build();

        Pass savedPass = passRepository.save(pass);
        return mapToResponse(savedPass);
    }

    public PassResponse getPassByCode(String passCode) {
        Pass pass = passRepository.findByPassCode(passCode)
                .orElseThrow(() -> new PassNotFoundException("Pass not found with code: " + passCode));
        return mapToResponse(pass);
    }

    public PassResponse verifyEntry(String id, VerifyRequest request) {
        Pass pass = passRepository.findById(id)
                .orElseThrow(() -> new PassNotFoundException("Pass not found with id: " + id));

        if (Boolean.TRUE.equals(pass.getEntryVerified())) {
            throw new PassAlreadyVerifiedException("Entry already verified");
        }

        pass.setEntryVerified(true);
        pass.setVerifiedBy(request.getVerifiedBy());
        pass.setEntryVerifiedAt(Instant.now());

        Pass updatedPass = passRepository.save(pass);
        return mapToResponse(updatedPass);
    }

    public PassResponse giveGoodies(String id, VerifyRequest request) {
        Pass pass = passRepository.findById(id)
                .orElseThrow(() -> new PassNotFoundException("Pass not found with id: " + id));

        if (Boolean.TRUE.equals(pass.getGoodiesGiven())) {
            throw new PassAlreadyVerifiedException("Goodies already given");
        }

        pass.setGoodiesGiven(true);
        pass.setGoodiesGivenBy(request.getVerifiedBy());
        pass.setGoodiesGivenAt(Instant.now());

        Pass updatedPass = passRepository.save(pass);
        return mapToResponse(updatedPass);
    }

    public List<PassResponse> getAllPasses() {
        return passRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<PassResponse> getFilteredPasses(Boolean entryVerified, Boolean goodiesGiven, 
                                                  String verifiedBy, String goodiesGivenBy) {
        Query query = new Query();
        
        if (entryVerified != null) {
            query.addCriteria(Criteria.where("entry_verified").is(entryVerified));
        }
        
        if (goodiesGiven != null) {
            query.addCriteria(Criteria.where("goodies_given").is(goodiesGiven));
        }
        
        if (verifiedBy != null && !verifiedBy.isEmpty()) {
            query.addCriteria(Criteria.where("verified_by").is(verifiedBy));
        }
        
        if (goodiesGivenBy != null && !goodiesGivenBy.isEmpty()) {
            query.addCriteria(Criteria.where("goodies_given_by").is(goodiesGivenBy));
        }
        
        List<Pass> passes = mongoTemplate.find(query, Pass.class);
        
        return passes.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deletePass(String id) {
        if (!passRepository.existsById(id)) {
            throw new PassNotFoundException("Pass not found with id: " + id);
        }
        passRepository.deleteById(id);
    }

    private PassResponse mapToResponse(Pass pass) {
        return PassResponse.builder()
                .id(pass.getId())
                .teamId(pass.getTeamId())
                .teamName(pass.getTeamName())
                .name(pass.getName())
                .email(pass.getEmail())
                .mobile(pass.getMobile())
                .gender(pass.getGender())
                .location(pass.getLocation())
                .userType(pass.getUserType())
                .domain(pass.getDomain())
                .course(pass.getCourse())
                .specialization(pass.getSpecialization())
                .yearOfGraduation(pass.getYearOfGraduation())
                .college(pass.getCollege())
                .unstopReportUrl(pass.getUnstopReportUrl())
                .pptUrl(pass.getPptUrl())
                .passCode(pass.getPassCode())
                .entryVerified(pass.getEntryVerified())
                .goodiesGiven(pass.getGoodiesGiven())
                .verifiedBy(pass.getVerifiedBy())
                .goodiesGivenBy(pass.getGoodiesGivenBy())
                .entryVerifiedAt(pass.getEntryVerifiedAt())
                .goodiesGivenAt(pass.getGoodiesGivenAt())
                .createdAt(pass.getCreatedAt())
                .build();
    }
}
