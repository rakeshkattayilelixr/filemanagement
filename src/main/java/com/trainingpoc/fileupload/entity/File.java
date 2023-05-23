package com.trainingpoc.fileupload.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document("files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class File {

    @Id
    public UUID uuid;
    public String fileName;
    public String userName;
    public Date uploadTime;
}
