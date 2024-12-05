package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.OrderStatus;
import com.teamnine.humanofdelivery.dto.ReviewRequestDto;
import com.teamnine.humanofdelivery.dto.ReviewResponseDto;
import com.teamnine.humanofdelivery.entity.Order;
import com.teamnine.humanofdelivery.entity.Review;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.entity.User;
import com.teamnine.humanofdelivery.repository.OrderRepository;
import com.teamnine.humanofdelivery.repository.ReviewRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import com.teamnine.humanofdelivery.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public ReviewResponseDto createReview( Long userId, ReviewRequestDto reviewRequestDto) {
            Order order = orderRepository.findById(reviewRequestDto.getOrderId()).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다"));

            if(!order.getOrderStatus().equals(OrderStatus.DELIVERY_COMPLETED)){
                throw new IllegalStateException("배달 완료된 주문만 리뷰 작성이 가능합니다.");
            }

            if(reviewRepository.existsByOrder_OrderId(reviewRequestDto.getOrderId())){
                throw new IllegalStateException(("해당 주문에 대한 리뷰는 이미 작성되었습니다."));
            }

            Store store = order.getStore();
            User user = userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException(("해당 사용자를 찾을 수 없습니다")));

        Review review = Review.builder()
                .store(store)
                .user(user)
                .order(order)
                .content(reviewRequestDto.getContent())
                .rate(reviewRequestDto.getRate())
                .build();

        return new ReviewResponseDto(reviewRepository.save(review));
    }

    public List<ReviewResponseDto> getStoreReviews(Long storeId, int minRate, int maxRate) {
        List<Review> reviews = reviewRepository.findAllByStore_StoreIdAndRateBetweenOrderByCreatedAtDesc(storeId, minRate, maxRate);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());
    }

    public ReviewResponseDto updateReview(Long reviewId, String content, int rate) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다"));
        review.updateReview(content, rate);
        return new ReviewResponseDto(reviewRepository.save(review));
    }

    public String deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다"));
        reviewRepository.delete(review);
        return "리뷰가 삭제되었습니다.";
    }


}
