package com.gable.testapi.registrationservice.controller;

import com.gable.testapi.registrationservice.dto.RegistrationRequestDto;
import com.gable.testapi.registrationservice.service.RegistrationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping("")
  public ResponseEntity<Object> registerNewUser(
      @Valid @RequestBody RegistrationRequestDto requestDto
  ) {

    registrationService.sendRegistrationData(requestDto);
    return ResponseEntity.ok(requestDto);
  }

}
