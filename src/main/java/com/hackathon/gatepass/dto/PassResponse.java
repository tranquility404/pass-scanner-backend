package com.hackathon.gatepass.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassResponse {

    private String id;
    private String teamId;
    private String teamName;
    private String name;
    private String email;
    private String mobile;
    private String gender;
    private String location;
    private String userType;
    private String domain;
    private String course;
    private String specialization;
    private Integer yearOfGraduation;
    private String college;
    private String unstopReportUrl;
    private String pptUrl;
    private String passCode;
    private Boolean entryVerified;
    private Boolean goodiesGiven;
    private String verifiedBy;
    private String goodiesGivenBy;
    private Instant entryVerifiedAt;
    private Instant goodiesGivenAt;
    private Instant createdAt;
}
