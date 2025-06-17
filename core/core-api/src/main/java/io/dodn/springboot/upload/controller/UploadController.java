package io.dodn.springboot.upload.controller;

import io.dodn.springboot.common.support.error.ErrorType;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.upload.domain.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    private final UploadService uploadService;

    public UploadController(final UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(value = "/places/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> uploadPlacesFromExcel(
            @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(ErrorType.ERR_1007, "파일이 비어있습니다."));
        }

        try {
            int count = uploadService.savePlaceDataFromExcel(file);
            String message = String.format("총 %d개의 맛집 데이터가 성공적으로 등록되었습니다.", count);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(message));
        } catch (Exception e) {
            // 구체적인 예외 처리를 추가하는 것이 좋습니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(ErrorType.ERR_1008,"엑셀 파일 처리 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}
