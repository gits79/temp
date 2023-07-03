package com.example.miniproject.controller;

import com.example.miniproject.dto.ItemDto;
import com.example.miniproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping("/post")
    public ItemDto created(
            @RequestBody
            ItemDto dto
    ) {
        return service.postItem(dto);
    }

    @PostMapping("/items")
    public ResponseEntity<Map<String, String>> create(
            @RequestBody
            ItemDto dto
    ) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message","등록이 완료되었습니다.");
        service.postItem(dto);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/items/{id}")
    public ItemDto read(
            @PathVariable("id")
            Long id,
            ItemDto dto
    ) {
        return service.readOne(id);
    }

    @GetMapping("/items")
    public Page<ItemDto> readP(
            @RequestParam(value = "page",defaultValue = "0") Integer page,
            @RequestParam(value = "limit",defaultValue = "20") Integer limit
    ) {
        return service.readPage(page, limit);
    }


}
