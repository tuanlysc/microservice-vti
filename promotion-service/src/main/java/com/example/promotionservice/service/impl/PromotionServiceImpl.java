package com.example.promotionservice.service.impl;

import com.example.promotionservice.common.DiscountType;
import com.example.promotionservice.common.PromotionStatus;
import com.example.promotionservice.dto.request.ApplyPromotionRequest;
import com.example.promotionservice.dto.request.CreatePromotionRequest;
import com.example.promotionservice.dto.response.ApplyPromotionResponse;
import com.example.promotionservice.dto.response.PromotionResponse;
import com.example.promotionservice.entity.Promotion;
import com.example.promotionservice.entity.PromotionUsage;
import com.example.promotionservice.exception.ApplicationException;
import com.example.promotionservice.exception.ErrorCode;
import com.example.promotionservice.mapper.PromotionMapper;
import com.example.promotionservice.repository.PromotionRepository;
import com.example.promotionservice.repository.PromotionUsageRepository;
import com.example.promotionservice.service.PromotionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionServiceImpl implements PromotionService {

    PromotionRepository promotionRepository;
    PromotionMapper promotionMapper;
    PromotionUsageRepository promotionUsageRepository;

    @Override
    public PromotionResponse create(CreatePromotionRequest request) {
        if(promotionRepository.findByCode(request.getCode()).isPresent()){
            throw new ApplicationException(ErrorCode.PROMOTION_CODE_EXITED);
        }
        if (request.getStartedAt() != null && request.getEndedAt() != null) {
            if (request.getStartedAt().isAfter(request.getEndedAt())) {
                throw new ApplicationException(ErrorCode.PROMOTION_INVALID_DATE_RANGE);
            }

            // Optional: Kiểm tra endedAt phải trong tương lai
            if (request.getEndedAt().isBefore(LocalDateTime.now())) {
                throw new ApplicationException(ErrorCode.PROMOTION_END_DATE_MUST_BE_FUTURE);
            }
        }

        var exitingPromotion = promotionRepository.save(promotionMapper.toEntity(request));

        return promotionMapper.toResponse(exitingPromotion);
    }

    @Override
    public List<PromotionResponse> findAll() {
        return promotionRepository.findAll().stream()
                .map(promotionMapper::toResponse)
                .toList();
    }

    @Override
    public PromotionResponse findByCode(String code) {
        return promotionMapper.toResponse(promotionRepository.findByCode(code).orElseThrow(
                () -> new ApplicationException(ErrorCode.PROMOTION_NOT_FOUND)
        ));
    }

    @Override
    public ApplyPromotionResponse apply(ApplyPromotionRequest request) {
        String code = request.getCode().trim().toUpperCase();

        Promotion promotion = promotionRepository.findByCode(code).orElseThrow(
                ()-> new ApplicationException(ErrorCode.PROMOTION_NOT_FOUND)
        );

        if(promotion.getStatus() != PromotionStatus.ACTIVE){
            throw new ApplicationException(ErrorCode.PROMOTION_INACTIVE);
        }

        LocalDateTime now = LocalDateTime.now();

        if(now.isBefore(promotion.getStartedAt())){
            throw new ApplicationException(ErrorCode.PROMOTION_NOT_STARTED);
        }

        if(now.isAfter(promotion.getEndedAt())){
            throw new ApplicationException(ErrorCode.PROMOTION_EXPIRED);
        }

        if(request.getOrderAmount().compareTo(promotion.getMinOrderValue())<0){
            throw new ApplicationException(ErrorCode.ORDER_VALUE_TOO_LOW);
        }

        if(promotion.getUsageLimit() != null && promotion.getUsedCount() >= promotion.getUsageLimit()){
            throw new ApplicationException(ErrorCode.PROMOTION_USAGE_LIMIT_REACHED);
        }

        if (Boolean.TRUE.equals(promotionUsageRepository.existsByOrderId(request.getOrderId()))){
            throw new ApplicationException(ErrorCode.PROMOTION_ALREADY_USED_FOR_ORDER);
        }

        BigDecimal discount = computeDiscount(promotion, request.getOrderAmount());

        PromotionUsage usage = promotionMapper.toUsage(request, promotion, discount);
        promotionUsageRepository.save(usage);

        promotion.setUsedCount(promotion.getUsedCount() + 1);
        promotionRepository.save(promotion);

        BigDecimal finalAmount = request.getOrderAmount().subtract(discount);



        return ApplyPromotionResponse.builder()
                .id(usage.getId())
                .code(code)
                .discountType(promotion.getDiscountType())
                .orderAmount(request.getOrderAmount())
                .discountApplied(discount)
                .finalAmount(finalAmount)
                .userId(request.getUserId())
                .orderId(request.getOrderId())
                .build();
    }

    @Override
    public Void revoke(String orderId) {
        PromotionUsage usage = promotionUsageRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.PROMOTION_USAGE_NOT_FOUND));

        Promotion promotion = promotionRepository.findById(usage.getPromotion().getId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.PROMOTION_NOT_FOUND));

        promotionUsageRepository.delete(usage);

        promotion.setUsedCount(Math.max(0, promotion.getUsedCount() - 1));
        promotionRepository.save(promotion);

        return null;
    }

    private BigDecimal computeDiscount(Promotion promotion, BigDecimal orderAmount){
        if(promotion.getDiscountType()== DiscountType.PERCENTAGE) {
            BigDecimal discount = orderAmount
                    .multiply(promotion.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            if (promotion.getMaxDiscountAmount() != null) {
                discount = discount.min(promotion.getMaxDiscountAmount());
            }
            return discount;
        }

        return promotion.getDiscountValue();
    }
}
