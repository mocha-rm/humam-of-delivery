package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.dto.ReviewRequestDto;
import com.teamnine.humanofdelivery.dto.ReviewResponseDto;
import com.teamnine.humanofdelivery.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewResponseDto createReview(@RequestParam Long userId, @RequestBody ReviewRequestDto reviewRequestDto) {
        return reviewService.createReview(userId, reviewRequestDto);
    }

    @GetMapping("/{storeId}")
    public List<ReviewResponseDto> getStoreReviews(@PathVariable Long storeId, @RequestParam int minRate, @RequestParam int maxRate) {
        return reviewService.getStoreReviews(storeId, minRate, maxRate);
    }

    @PatchMapping("/{reviewId}")
    public ReviewResponseDto updateReview(@PathVariable Long reviewId, @RequestParam String content, @RequestParam int rate) {
        return reviewService.updateReview(reviewId, content, rate);
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}

