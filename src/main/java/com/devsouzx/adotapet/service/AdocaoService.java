package com.devsouzx.adotapet.service;

import com.devsouzx.adotapet.repository.AdocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdocaoService {
    private final AdocaoRepository adocaoRepository;
}
