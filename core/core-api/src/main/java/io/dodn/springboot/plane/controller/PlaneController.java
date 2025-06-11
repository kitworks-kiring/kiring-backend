package io.dodn.springboot.plane.controller;

import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.common.swagger.PlaneDocs;
import io.dodn.springboot.plane.controller.request.ReadMessageRequest;
import io.dodn.springboot.plane.controller.request.SendMessageRequest;
import io.dodn.springboot.plane.controller.response.SendMessageResponse;
import io.dodn.springboot.plane.domain.PlaneService;
import io.dodn.springboot.storage.db.plane.entity.Plane;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plane")
public class PlaneController implements PlaneDocs {

    private final PlaneService planeService;

    public PlaneController(final PlaneService planeService) {
        this.planeService = planeService;
    }

    @PostMapping("/send-message")
    public ApiResponse<SendMessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        Plane plane = planeService.sendMessage(request);
        return ApiResponse.success(SendMessageResponse.fromEntity(plane));
    }

    @PatchMapping("/read")
    public ApiResponse<?> readMessage(@Valid @RequestBody ReadMessageRequest request) {
        planeService.readMessage(request);
        return ApiResponse.success();
    }

}
