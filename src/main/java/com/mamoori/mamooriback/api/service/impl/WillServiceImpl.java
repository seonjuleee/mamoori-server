package com.mamoori.mamooriback.api.service.impl;

import com.mamoori.mamooriback.api.dto.WillPageResponse;
import com.mamoori.mamooriback.api.dto.WillRequest;
import com.mamoori.mamooriback.api.dto.WillResponse;
import com.mamoori.mamooriback.api.entity.User;
import com.mamoori.mamooriback.api.entity.Will;
import com.mamoori.mamooriback.api.repository.UserRepository;
import com.mamoori.mamooriback.api.repository.WillRepository;
import com.mamoori.mamooriback.api.service.WillService;
import com.mamoori.mamooriback.exception.BusinessException;
import com.mamoori.mamooriback.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WillServiceImpl implements WillService {

    private final WillRepository willRepository;
    private final UserRepository userRepository;

    @Override
    public WillPageResponse getWillListByEmail(String email, String title, Pageable pageable) {
        Page<WillResponse> search = willRepository.search(email, title, pageable);

        return WillPageResponse.builder()
                .wills(search.getContent())
                .totalWillCount(search.getTotalElements())
                .size(search.getSize())
                .page(search.getNumber())
                .build();
    }

    @Override
    public WillResponse getWillById(String email, Long id) {
        Will will = willRepository.findByUser_EmailAndWillId(email, id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        return new WillResponse(will);
    }

    @Override
    public void postWill(String email, WillRequest willRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        willRepository.save(willRequest.toEntity(user));
    }

    @Override
    public void putWill(String email, Long id, WillRequest willRequest) {
        log.debug("putWill -> willId : {}", id);
        Will will = willRepository.findByWillId(id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));

        if (!will.getUser().getEmail().equals(email)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage());
        }
        will.setTitle(willRequest.getTitle());
        will.setContent(willRequest.getContent());
        willRepository.save(will);
    }

    @Override
    public void deleteWill(String email, Long willId) {
        Will will = willRepository.findByWillId(willId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        if (!will.getUser().getEmail().equals(email)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage());
        }
        willRepository.delete(will);
    }
}
