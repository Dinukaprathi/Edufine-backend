package com.edufine.backend.controller.notices;

import com.edufine.backend.dto.NoticeRequestDto;
import com.edufine.backend.dto.NoticeResponseDto;
import com.edufine.backend.dto.NoticeStatusRequestDto;
import com.edufine.backend.service.NoticeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @GetMapping("/active")
    public ResponseEntity<List<NoticeResponseDto>> getActiveNotices() {
        return ResponseEntity.ok(noticeService.getActiveNotices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> getNoticeById(@PathVariable String id) {
        return noticeService.getNoticeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LIC','LECTURER','INSTRUCTOR','STAFF')")
    public ResponseEntity<NoticeResponseDto> createNotice(@Valid @RequestBody NoticeRequestDto requestDto) {
        return ResponseEntity.ok(noticeService.createNotice(requestDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LIC','LECTURER','INSTRUCTOR','STAFF')")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable String id,
                                                          @Valid @RequestBody NoticeRequestDto requestDto) {
        return ResponseEntity.ok(noticeService.updateNotice(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LIC','LECTURER','INSTRUCTOR','STAFF')")
    public ResponseEntity<Void> deleteNotice(@PathVariable String id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LIC','LECTURER','INSTRUCTOR','STAFF')")
    public ResponseEntity<Void> deactivate(@PathVariable String id) {
        noticeService.deactivateNotice(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LIC','LECTURER','INSTRUCTOR','STAFF')")
    public ResponseEntity<Void> activate(@PathVariable String id) {
        noticeService.activateNotice(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','LIC','LECTURER','INSTRUCTOR','STAFF')")
    public ResponseEntity<Void> updateStatus(@PathVariable String id,
                                             @Valid @RequestBody NoticeStatusRequestDto requestDto) {
        noticeService.updateNoticeStatus(id, requestDto.getActive());
        return ResponseEntity.ok().build();
    }
}
