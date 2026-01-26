package com.edufine.backend.service;

import com.edufine.backend.dto.NoticeRequestDto;
import com.edufine.backend.dto.NoticeResponseDto;
import com.edufine.backend.entity.Notice;
import com.edufine.backend.mapper.NoticeMapper;
import com.edufine.backend.repository.NoticeRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeMapper noticeMapper;

    public NoticeResponseDto createNotice(NoticeRequestDto requestDto) {
        Notice notice = noticeMapper.toEntity(requestDto);
        notice.setActive(true);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setUpdatedAt(LocalDateTime.now());
        Notice saved = noticeRepository.save(notice);
        return noticeMapper.toResponseDto(saved);
    }

    public List<NoticeResponseDto> getAllNotices() {
        return noticeRepository.findAll().stream()
                .map(noticeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<NoticeResponseDto> getActiveNotices() {
        return noticeRepository.findByActive(true).stream()
                .map(noticeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<NoticeResponseDto> getNoticeById(String id) {
        ObjectId objectId = new ObjectId(id);
        return noticeRepository.findById(objectId)
                .map(noticeMapper::toResponseDto);
    }

    public NoticeResponseDto updateNotice(String id, NoticeRequestDto requestDto) {
        ObjectId objectId = new ObjectId(id);
        Notice notice = noticeRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        noticeMapper.updateEntityFromDto(requestDto, notice);
        notice.setUpdatedAt(LocalDateTime.now());
        Notice updated = noticeRepository.save(notice);
        return noticeMapper.toResponseDto(updated);
    }

    public void deleteNotice(String id) {
        ObjectId objectId = new ObjectId(id);
        Notice notice = noticeRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        noticeRepository.delete(notice);
    }

    public void deactivateNotice(String id) {
        ObjectId objectId = new ObjectId(id);
        Notice notice = noticeRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        notice.setActive(false);
        notice.setUpdatedAt(LocalDateTime.now());
        noticeRepository.save(notice);
    }

    public void activateNotice(String id) {
        ObjectId objectId = new ObjectId(id);
        Notice notice = noticeRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        notice.setActive(true);
        notice.setUpdatedAt(LocalDateTime.now());
        noticeRepository.save(notice);
    }
}
