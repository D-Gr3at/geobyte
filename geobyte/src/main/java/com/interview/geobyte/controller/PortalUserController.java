package com.interview.geobyte.controller;

import com.interview.geobyte.dto.PortalUserResponse;
import com.interview.geobyte.service.PortalUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class PortalUserController {

    private final PortalUserService portalUserService;

    @GetMapping
    public ResponseEntity<PortalUserResponse> findUserByEmail(@RequestParam(value = "email") String email){
        return ResponseEntity.status(HttpStatus.OK)
                .body(portalUserService.getPortalUserByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortalUserResponse> findUserById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(portalUserService.getPortalUserById(id));
    }

}
