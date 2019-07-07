package ru.nsu.fit.supernova.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@Entity
@Table(name = "multi_media_file")
public class MultiMediaFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column
    private String title;

    @Column(name = "external_video_url")
    private String externalVideoUrl;

    @Column(name = "internal_audio_url")
    private String internalAudioUrl;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusType status;

    @Column(name = "message")
    private String message;
}
