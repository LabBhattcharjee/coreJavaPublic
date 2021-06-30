package com.example.demo.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;


@Table
@Data
public class Book {

    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private  UUID id;

    @PrimaryKeyColumn(name = "title", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private  String title;

    @PrimaryKeyColumn(name = "publisher", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private   String publisher;

    @Column
    private Set<String> tags = new HashSet<>();

    public Book(final UUID id, final String title, final String publisher, final Set<String> tags) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.tags.addAll(tags);
    }

   

}
