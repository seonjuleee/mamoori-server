package com.mamoori.mamooriback.api.service.impl;

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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WillServiceImpl implements WillService {

    private final WillRepository willRepository;
    private final UserRepository userRepository;

    @Override
    public WillResponse getWillByEmail(String email) {
        Will will = willRepository.findByUser_Email(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        return new WillResponse(will);
    }

    @Override
    public void putWill(String email, WillRequest willRequest) {
        Optional<Will> optionalWill = willRepository.findByUser_Email(email);
        log.debug("putWill -> isPresent : {}", optionalWill.isPresent());

        if (!optionalWill.isPresent()) {
            // create
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.USER_NOT_FOUND, ErrorCode.USER_NOT_FOUND.getMessage()));
            Will will = willRequest.toEntity(user);
            will.setTitle(willRequest.getTitle());
            will.setContent(willRequest.getContent());
            willRepository.save(will);
        } else {
            // update
            Will will = optionalWill.get();
            if (!will.getUser().getEmail().equals(email)) {
                throw new BusinessException(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage());
            }
            will.setTitle(willRequest.getTitle());
            will.setContent(willRequest.getContent());
            willRepository.save(will);
        }
    }

    @Override
    public void deleteWill(String email) {
        Will will = willRepository.findByUser_Email(email)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage()));
        if (!will.getUser().getEmail().equals(email)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.getMessage());
        }
        willRepository.delete(will);
    }
}
