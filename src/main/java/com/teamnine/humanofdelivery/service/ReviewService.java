package com.teamnine.humanofdelivery.service;

import com.teamnine.humanofdelivery.exception.order.OrderErrorCode;
import com.teamnine.humanofdelivery.exception.order.OrderException;
import com.teamnine.humanofdelivery.exception.review.ReviewErrorCode;
import com.teamnine.humanofdelivery.exception.review.ReviewException;
import com.teamnine.humanofdelivery.status.OrderStatus;
import com.teamnine.humanofdelivery.common.SessionNames;
import com.teamnine.humanofdelivery.dto.review.ReviewRequestDto;
import com.teamnine.humanofdelivery.dto.review.ReviewResponseDto;
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

    /**
     * 로그인 세션에서 Member 정보를 가져옵니다.
     * @param session HttpSession
     * @return 로그인된 Member
     */
    private Member getMemberFromSession(HttpSession session) {
        String email = (String) session.getAttribute(SessionNames.USER_AUTH);
        if (email == null) {
            throw new IllegalStateException("로그인 정보가 유효하지 않습니다. 다시 로그인해주세요.");
        }
        Member member = memberRepository.findByEmailOrElseThrow(email);
        return member;
    }

    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, HttpSession session) {
        // 로그인한 사용자 확인
        Member member = getMemberFromSession(session);
        Long userId = member.getUserId();

        // 주문 조회
        Order order = orderRepository.findById(reviewRequestDto.getOrderId()).orElseThrow(()->new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        // 로그인한 사용자가 주문의 주인인지 확인
        if (!order.getMember().getUserId().equals(userId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_ERROR_AUTHORIZATION_01);
        }

        if(!order.getOrderStatus().equals(OrderStatus.DELIVERY_COMPLETED)){
                throw new ReviewException(ReviewErrorCode.REVIEW_ERROR_STATUS_01);
        }


        Store store = order.getStore();

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
        if (minRate == null) minRate = 0;  // 최소 기본값 설정
        if (maxRate == null) maxRate = 5;  // 최대 기본값 설정

        List<Review> reviews = reviewRepository.findAllByStore_StoreIdAndRateBetweenOrderByCreatedAtDesc(storeId, minRate, maxRate);
        return reviews.stream().map(ReviewResponseDto::new).collect(Collectors.toList());


    }

    public ReviewResponseDto updateReview(Long reviewId, String content, int rate, HttpSession session) {
        // 로그인한 사용자 확인
        Member member = getMemberFromSession(session);
        Long userId = member.getUserId();

        //리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        //로그인한 사용자가 리뷰 작성자인지 확인
        if (!review.getMember().getUserId().equals(userId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_ERROR_AUTHORIZATION_02);
        }

        //리뷰 수정
        review.updateReview(rate, content);
        return new ReviewResponseDto(reviewRepository.save(review));
    }

    public String deleteReview(Long reviewId, HttpSession session) {
        // 로그인한 사용자 확인
        Member member = getMemberFromSession(session);
        Long userId = member.getUserId();


        //리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(()->new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 로그인한 사용자가 리뷰 작성자인지 확인
        if (!review.getMember().getUserId().equals(userId)) {
            throw new ReviewException(ReviewErrorCode.REVIEW_ERROR_AUTHORIZATION_03);
        }

        reviewRepository.delete(review);
        return "리뷰가 삭제되었습니다.";
    }

}
