package com.hackathon.gatepass.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "passes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pass {

    @Id
    private String id;

    @Field("team_id")
    private String teamId;

    @Field("team_name")
    private String teamName;

    private String name;

    private String email;

    private String mobile;

    private String gender;

    private String location;

    @Field("user_type")
    private String userType;

    private String domain;

    private String course;

    private String specialization;

    @Field("year_of_graduation")
    private Integer yearOfGraduation;

    private String college;

    @Field("unstop_report_url")
    private String unstopReportUrl;

    @Field("ppt_url")
    private String pptUrl;

    @Indexed(unique = true)
    @Field("pass_code")
    private String passCode;

    @Builder.Default
    @Field("entry_verified")
    private Boolean entryVerified = false;

    @Builder.Default
    @Field("goodies_given")
    private Boolean goodiesGiven = false;

    @Field("verified_by")
    private String verifiedBy;

    @Field("goodies_given_by")
    private String goodiesGivenBy;

    @Field("entry_verified_at")
    private Instant entryVerifiedAt;

    @Field("goodies_given_at")
    private Instant goodiesGivenAt;

    @Builder.Default
    @Field("created_at")
    private Instant createdAt = Instant.now();
}
