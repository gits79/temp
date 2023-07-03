package com.example.miniproject.service;

import com.example.miniproject.dto.ItemDto;
import com.example.miniproject.entity.ItemEntity;
import com.example.miniproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository repository;

    public ItemDto postItem(ItemDto dto) {
        ItemEntity entity = new ItemEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setMin_price_wanted(dto.getMin_price_wanted());
        entity.setWriter(dto.getWriter());
        entity.setPassword(dto.getPassword());
        return ItemDto.fromEntity(repository.save(entity));
    }

    public ItemDto readOne(Long id) {
        Optional<ItemEntity> optionalItemEntity = repository.findById(id);
        if (optionalItemEntity.isPresent())
            return ItemDto.fromEntity(optionalItemEntity.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Page<ItemDto> readPage(Integer pageNumber, Integer pageSize) {
// PagingAndSortingRepository 메소드에 전달하는 용도
        // 조회하고 싶은 페이지의 정보를 담는 객체
        // 20개씩 데이터를 나눌때 0번 페이지를 달라고 요청하는 Pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").descending());
        Page<ItemEntity> articleEntityPage = repository.findAll(pageable);
        // map: 전달받은 함수를 각 원소에 인자로 전달한 결과를
        // 다시 모아서 Stream 으로
        // Page,map: 전달받은 함수를 각 원소에 인자로 전달한 결과를
        // 다시 모아서 Page 로
        Page<ItemDto> itemDtoPage = articleEntityPage.map(ItemDto::fromEntity);
        return itemDtoPage;
    }

}
