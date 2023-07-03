package com.example.miniproject.service;

import com.example.miniproject.dto.ItemDto;
import com.example.miniproject.entity.ItemEntity;
import com.example.miniproject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
@Slf4j
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
        entity.setStatus("판매중");
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
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id").ascending());
        Page<ItemEntity> articleEntityPage = repository.findAll(pageable);
        // map: 전달받은 함수를 각 원소에 인자로 전달한 결과를
        // 다시 모아서 Stream 으로
        // Page,map: 전달받은 함수를 각 원소에 인자로 전달한 결과를
        // 다시 모아서 Page 로
        Page<ItemDto> itemDtoPage = articleEntityPage.map(ItemDto::fromEntity);
        return itemDtoPage;
    }

    public ItemDto update(Long id, ItemDto dto) {
        Optional<ItemEntity> optionalItemEntity = repository.findById(id);
        if (optionalItemEntity.isPresent()) {
            ItemEntity entity = optionalItemEntity.get();
            log.info(entity.getPassword()+"  ==  "+dto.getPassword());
            if (entity.getPassword().equals(dto.getPassword())) {
                entity.setTitle(dto.getTitle());
                entity.setDescription(dto.getDescription());
                entity.setMin_price_wanted(dto.getMin_price_wanted());
                entity.setWriter(dto.getWriter());
                entity.setPassword(dto.getPassword());
                log.info("비번마증");
                return ItemDto.fromEntity(repository.save(entity));
            }
            else{
                log.info("비번틀림");

            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    }

    public ItemDto updateImg(Long id, MultipartFile image, String password) {
        // 사용자가 프로필 이미지를 업로드 한다.

        // 1. 유저 존재 확인
        Optional<ItemEntity> optionalEntity
                = repository.findById(id);
        if (optionalEntity.isEmpty())
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        ItemEntity entity = optionalEntity.get();
        // media/filename.png
        // media/<업로드 시각>.png
        // 2. 파일을 어디에 업로드 할건지
        // media/{userId}/profile.{파일 확장자}
        try{
            Files.createDirectories(Path.of(String.format("images/%d",id)));
        }
        catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Image 파일 등록에 실패하였습니다. 1");
        }




        // 2-2. 확장자를 포함한 이미지 이름 만들기 (profile.{확장자})
        String originalFilename = image.getOriginalFilename();
        String imageDirection = String.format("images/%d/",id);

        // 2-3. 폴더와 파일 경로를 포함한 이름 만들기
        String profilePath = imageDirection + originalFilename;
        log.info(profilePath);

        // 3. MultipartFile 을 저장하기
        try {
            image.transferTo(Path.of(profilePath));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 4. UserEntity 업데이트 (정적 프로필 이미지를 회수할 수 있는 URL)
        // http://localhost:8080/static/1/profile.png

        log.info(profilePath);
        entity.setImage_url(profilePath);
        return ItemDto.fromEntity(repository.save(entity));
    }

    public void deleteItem(Long id, ItemDto dto) {
        Optional<ItemEntity> optionalItemEntity = repository.findById(id);
        if (optionalItemEntity.isPresent()){
            if (optionalItemEntity.get().getPassword().equals(dto.getPassword())) {
                repository.deleteById(id);
            }
            else{
                // 비번틀림
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    }

}
