package com.example.redistask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language {
    @Id                                         // в редисе id надо своими руками реализовывать?
    private String id;
    private String name;
    private String author;
}
