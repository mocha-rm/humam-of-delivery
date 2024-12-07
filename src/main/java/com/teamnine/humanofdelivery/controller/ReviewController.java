package com.teamnine.humanofdelivery.controller;

import com.teamnine.humanofdelivery.dto.review.ReviewRequestDto;
import com.teamnine.humanofdelivery.dto.review.ReviewResponseDto;
import com.teamnine.humanofdelivery.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/user/reviews")
    public ReviewResponseDto createReview( @RequestBody ReviewRequestDto reviewRequestDto, HttpSession session) {
        return reviewService.createReview(reviewRequestDto, session);
    }

    @GetMapping("reviews/{storeId}")
    public List<ReviewResponseDto> getStoreReviews(@PathVariable Long storeId, @RequestParam(required = false) Integer minRate, @RequestParam(required = false) Integer maxRate) {
        return reviewService.getStoreReviews(storeId, minRate, maxRate);
    }

    @PatchMapping("/user/reviews/{reviewId}")
    public ReviewResponseDto updateReview(@PathVariable Long reviewId, @RequestBody ReviewRequestDto reviewRequestDto, HttpSession session) {
        return reviewService.updateReview(reviewId, reviewRequestDto.getContent(), reviewRequestDto.getRate(), session);
    }

    @DeleteMapping("/user/reviews/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId, HttpSession session) {
        return reviewService.deleteReview(reviewId, session);
    }
}

