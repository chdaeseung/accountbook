package chdaeseung.accountbook.category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class CategoryResponseDto {
    private final Long id;
    private final String name;

    public CategoryResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
