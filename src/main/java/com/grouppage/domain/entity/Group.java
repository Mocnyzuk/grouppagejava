package com.grouppage.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "group_table")
@AllArgsConstructor
@NoArgsConstructor
public class Group extends AbstractEntityDate{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String category;

    @NotNull
    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean isPrivate;

    @ManyToOne(targetEntity = Reaction.class, fetch = FetchType.LAZY)
    private Reaction reaction;

    @Column(columnDefinition = "boolean default false")
    private boolean isAccept;

    @Column(columnDefinition = "boolean default false")
    private boolean isForm;

    @Nullable
    private String inviteCode;

    private int participantCount;

    private String imageId;

    private long creatorId;

    public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors)
    {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();

        return t ->
        {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());

            return ((ConcurrentHashMap) seen).putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }
}
