package com.mininetflix.video.catalog.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "video_metadata")
public class VideoMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID videoId;
    private String title;
    private String description;
    private String url;
    private String thumbnailUrl;
    private String genre;
    private String author;
    private String publisher;
    private long createdTime;
    private long updatedTime;
    private long showingFromTime;
    private long showingToTime;
}
