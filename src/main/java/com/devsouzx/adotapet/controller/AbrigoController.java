package com.devsouzx.adotapet.controller;

import com.devsouzx.adotapet.service.AbrigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/abrigo")
@RequiredArgsConstructor
public class AbrigoController {
    private final AbrigoService abrigoService;

    @GetMapping
    public ResponseEntity<Void> getAbrigos() {
        return null;
    }
}
