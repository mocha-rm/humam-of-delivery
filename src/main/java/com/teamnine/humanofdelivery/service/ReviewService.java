package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.OrderStatus;
import com.teamnine.humanofdelivery.dto.ReviewRequestDto;
import com.teamnine.humanofdelivery.dto.ReviewResponseDto;
import com.teamnine.humanofdelivery.entity.Member;
import com.teamnine.humanofdelivery.entity.Order;
import com.teamnine.humanofdelivery.entity.Review;
import com.teamnine.humanofdelivery.entity.Store;
import com.teamnine.humanofdelivery.repository.MemberRepository;
import com.teamnine.humanofdelivery.repository.OrderRepository;
import com.teamnine.humanofdelivery.repository.ReviewRepository;
import com.teamnine.humanofdelivery.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
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
    private final MemberRepository memberRepository;

    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, HttpSession session) {
        // 로그인한 사용자 확인
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        // 주문 조회
        Order order = orderRepository.findById(reviewRequestDto.getOrderId()).orElseThrow(()->new EntityNotFoundException("주문을 찾을 수 없습니다"));

        // 로그인한 사용자가 주문의 주인인지 확인
        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 주문에 대해서만 리뷰를 작성할 수 있습니다.");
        }

        if(!order.getOrderStatus().equals(OrderStatus.DELIVERY_COMPLETED)){
                throw new IllegalStateException("배달 완료된 주문만 리뷰 작성이 가능합니다.");
        }

        if(reviewRepository.existsByOrder_OrderId(reviewRequestDto.getOrderId())){
                throw new IllegalStateException(("해당 주문에 대한 리뷰는 이미 작성되었습니다."));
        }

        Store store = order.getStore();
        Member member = memberRepository.findById(userId).orElseThrow(()->new EntityNotFoundException(("해당 사용자를 찾을 수 없습니다")));

        Review review = Review.builder()
                .store(store)
                .member(member)
                .order(order)
                .content(reviewRequestDto.getContent())
                .rate(reviewRequestDto.getRate())
                .build();

        return new ReviewResponseDto(reviewRepository.save(review));
    }

    public List<ReviewResponseDto> getStoreReviews(Long storeId, Integer minRate, Integer maxRate) {
        List<Review> reviews;

        if (minRate != null && maxRate != null) {
            // 별점 범위가 지정된 경우
            reviews = reviewRepository.findAllByStore_StoreIdAndRateBetweenOrderByCreatedAtDesc(storeId, minRate, maxRate);
        } else {
            // 별점 범위가 없는 경우 모든 리뷰 조회
            reviews = reviewRepository.findAllByStore_StoreIdOrderByCreatedAtDesc(storeId);
        }

        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());

    }

    public ReviewResponseDto updateReview(Long reviewId, String content, int rate, HttpSession session) {
        // 로그인한 사용자 확인
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        //리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다"));

        //로그인한 사용자가 리뷰 작성자인지 확인
        if (!review.getMember().getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 리뷰만 수정할 수 있습니다.");
        }

        //리뷰 수정
        review.updateReview(content, rate);
        return new ReviewResponseDto(reviewRepository.save(review));
    }

    public String deleteReview(Long reviewId, HttpSession session) {
        // 로그인한 사용자 확인
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }


        //리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new EntityNotFoundException("해당 리뷰를 찾을 수 없습니다"));

        // 로그인한 사용자가 리뷰 작성자인지 확인
        if (!review.getMember().getUserId().equals(userId)) {
            throw new IllegalStateException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);
        return "리뷰가 삭제되었습니다.";
    }

}
