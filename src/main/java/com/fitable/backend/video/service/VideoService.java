package com.fitable.backend.video.service;

import com.fitable.backend.video.entity.Video;
import com.fitable.backend.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    @Autowired
    private final VideoRepository videoRepository;
    private final YoutubeApiService youtubeApiService;

    public VideoService(VideoRepository videoRepository, YoutubeApiService youtubeApiService) {
        this.videoRepository = videoRepository;
        this.youtubeApiService = youtubeApiService;
    }

    public void saveAllVideos(List<Video> videos) {
        videoRepository.saveAll(videos);
    }

    public String getVideoUrlsByTitle(String title) {
        // Fetch video URLs by title from the local database
        List<String> urls = videoRepository.findUrlsByTitleContaining(title);

        // If no data found locally, fetch from YouTube API
        if (urls.isEmpty()) {
            urls = youtubeApiService.fetchVideosFromYouTube(title);
        }

        return urls.get(0);
    }

}
