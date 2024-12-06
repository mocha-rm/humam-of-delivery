package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.dto.ReviewRequestDto;
import com.teamnine.humanofdelivery.dto.ReviewResponseDto;
import com.teamnine.humanofdelivery.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewResponseDto createReview( @RequestBody ReviewRequestDto reviewRequestDto, HttpSession session) {
        return reviewService.createReview(reviewRequestDto, session);
    }

    @GetMapping("/{storeId}")
    public List<ReviewResponseDto> getStoreReviews(@PathVariable Long storeId, @RequestParam(required = false) Integer minRate, @RequestParam(required = false) Integer maxRate) {
        return reviewService.getStoreReviews(storeId, minRate, maxRate);
    }

    @PatchMapping("/{reviewId}")
    public ReviewResponseDto updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDto reviewRequestDto, HttpSession session) {
        return reviewService.updateReview(reviewId, reviewRequestDto.getContent(), reviewRequestDto.getRate(), session);
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId, HttpSession session) {
        return reviewService.deleteReview(reviewId, session);
    }
}

