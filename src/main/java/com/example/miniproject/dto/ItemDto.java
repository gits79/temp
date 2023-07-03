package com.example.miniproject.dto;

import com.example.miniproject.entity.ItemEntity;
import lombok.Data;

@Data
public class ItemDto {
    private Long id;
    private String title;
    private String description;
    private Integer min_price_wanted;
    private String writer;
    private String password;
//    private String status;


    public static ItemDto fromEntity(ItemEntity entity) {
        ItemDto dto = new ItemDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setMin_price_wanted(entity.getMin_price_wanted());
        dto.setWriter(entity.getWriter());
        dto.setPassword(entity.getPassword());
        return dto;
    }

}
