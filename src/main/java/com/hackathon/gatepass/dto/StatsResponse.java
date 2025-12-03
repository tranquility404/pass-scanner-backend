package com.hackathon.gatepass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {

    private Long totalPasses;
    private Long totalEntriesVerified;
    private Long totalGoodiesGiven;
    private List<CollegeStats> colleges;
    private Map<String, Long> entriesVerifiedBy;
    private Map<String, Long> goodiesGivenBy;
    private String mostEntriesVerifiedBy;
    private String mostGoodiesGivenBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CollegeStats {
        private String collegeName;
        private Long count;
    }
}
