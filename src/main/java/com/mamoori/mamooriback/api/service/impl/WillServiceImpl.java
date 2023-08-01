package com.mamoori.mamooriback.api.service.impl;

import com.mamoori.mamooriback.api.dto.PageResponse;
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
    public PageResponse<WillResponse> getWillListByEmail(String email, String title, Pageable pageable) {
        Page<WillResponse> search = willRepository.search(email, title, pageable);
        return new PageResponse<>(search.getContent(), (int) search.getTotalElements(), search.getSize(), search.getNumber());
    }

    @Override
    public WillResponse getWillById(String email, Long id) {
        Will will = willRepository.findByUser_EmailAndWillId(email, id)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        return new WillResponse(will);
    }

    @Override
    public void putWill(String email, WillRequest willRequest) {
        if (willRequest.getWillId() == null) {
            // create
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
            willRepository.save(willRequest.toEntity(user));
        } else {
            log.debug("willService.putWill -> willId : {}", willRequest.getWillId());
            // update
            Will will = willRepository.findByWillId(willRequest.getWillId())
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));

            log.debug("willlService.putWill -> email : {}", email);
            log.debug("willlService.putWill -> email : {}", will.getUser().getEmail());
            if (!will.getUser().getEmail().equals(email)) {
                throw new BusinessException(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage());
            }
            will.setTitle(willRequest.getTitle());
            will.setContent(willRequest.getContent());
            willRepository.save(will);
        }
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
