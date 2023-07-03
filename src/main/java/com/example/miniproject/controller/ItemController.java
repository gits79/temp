package com.example.miniproject.controller;

import com.example.miniproject.dto.ItemDto;
import com.example.miniproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Map<String, String>> put(
            @PathVariable("itemId")
            Long itemId,
            @RequestBody
            ItemDto dto
    ) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message","물품이 수정되었습니다.");
        service.update(itemId,dto);
        return ResponseEntity.ok(responseBody);
    }

    @PutMapping(value = "/items/{itemId}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ItemDto updateImg(
            @PathVariable("itemId")
            Long itemId,
            @RequestParam("image")MultipartFile image,
            @RequestParam("writer") String writer,
            @RequestParam("password") String password
            ) {
        return service.updateImg(itemId, image, password);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable("itemId")
            Long id,
            @RequestBody
            ItemDto dto
    ) {

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message","물품을 삭제했습니다.");
        service.deleteItem(id,dto);
        return ResponseEntity.ok(responseBody);
    }

}
