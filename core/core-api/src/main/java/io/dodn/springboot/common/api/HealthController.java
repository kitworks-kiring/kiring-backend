package io.dodn.springboot.common.api;

import io.dodn.springboot.common.swagger.HealthDocs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController implements HealthDocs {

    @GetMapping("/health")
    public ResponseEntity<Void> health() {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
