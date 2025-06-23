package io.dodn.springboot.plane.controller;

import io.dodn.springboot.common.annotation.LoginUser;
import io.dodn.springboot.common.support.response.ApiResponse;
import io.dodn.springboot.common.swagger.PlaneDocs;
import io.dodn.springboot.plane.controller.request.SendMessageRequest;
import io.dodn.springboot.plane.controller.response.MessageResponse;
import io.dodn.springboot.plane.controller.response.PlaneStatusResponse;
import io.dodn.springboot.plane.controller.response.SendMessageResponse;
import io.dodn.springboot.plane.domain.PlaneInfo;
import io.dodn.springboot.plane.domain.PlaneService;
import io.dodn.springboot.plane.domain.PlaneStatusInfo;
import io.dodn.springboot.storage.db.plane.entity.Plane;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plane")
public class PlaneController implements PlaneDocs {

    private final PlaneService planeService;

    public PlaneController(final PlaneService planeService) {
        this.planeService = planeService;
    }

    @PostMapping("/send-message")
    public ApiResponse<SendMessageResponse> sendMessage(
            @Valid @RequestBody SendMessageRequest request
    ) {
        Plane plane = planeService.sendMessage(request);
        return ApiResponse.success(SendMessageResponse.fromEntity(plane));
    }

    @GetMapping("/read")
    public ApiResponse<List<MessageResponse>> readMessage(
            @LoginUser Long readerId
    ) {
        final List<PlaneInfo> planeInfos = planeService.readMessage(readerId);

        return ApiResponse.success(
                planeInfos.stream()
                        .map(MessageResponse::fromEntity)
                        .toList()
        );
    }

    @GetMapping("/today/message")
    public ApiResponse<PlaneStatusResponse> planeStatus(
            @LoginUser final Long readerId
    ) {
        PlaneStatusInfo planeStatus = planeService.getPlaneStatus(readerId);
        return ApiResponse.success(PlaneStatusResponse.from(planeStatus));
    }

}
