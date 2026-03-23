package chdaeseung.accountbook.category.dto;

import chdaeseung.accountbook.transaction.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateDto {

    private String name;
}
