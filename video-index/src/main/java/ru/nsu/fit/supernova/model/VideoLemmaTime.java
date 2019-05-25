package ru.nsu.fit.supernova.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "video_lemma_time")
public class VideoLemmaTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="multi_media_file_id", nullable=false, updatable=false)
    private MultiMediaFile multiMediaFile;

    @Column(name = "lemma")
    private String lemma;

    @Column(name = "start_time")
    private BigDecimal startTime;
}
