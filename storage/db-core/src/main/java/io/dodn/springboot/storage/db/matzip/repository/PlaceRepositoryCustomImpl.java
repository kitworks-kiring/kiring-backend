package io.dodn.springboot.storage.db.matzip.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.dodn.springboot.storage.db.matzip.dto.PlaceNearbyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.dodn.springboot.storage.db.matzip.entity.QPlace.place;
import static io.dodn.springboot.storage.db.matzip.entity.QCategory.category;




@Repository
public class PlaceRepositoryCustomImpl implements PlaceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PlaceRepositoryCustomImpl(final JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<PlaceNearbyDto> findNearbyPlaces(final double latitude, final double longitude, final int radius, final Long categoryId, final Pageable pageable) {
        String pointWkt = String.format("POINT(%f %f)", latitude, longitude);

        NumberTemplate<Double> distanceTemplate = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere({0}, ST_PointFromText({1}, 4326))",
                place.location,
                pointWkt
        );

        List<PlaceNearbyDto> content = queryFactory
                .select(Projections.constructor(PlaceNearbyDto.class,
                        place.id,
                        place.name,
                        place.address,
                        place.phoneNumber,
                        place.imageUrl,
                        place.category,
                        place.likeCount,
                        Expressions.numberTemplate(Double.class, "ST_X({0})", place.location), // longitude
                        Expressions.numberTemplate(Double.class, "ST_Y({0})", place.location), // latitude
                        distanceTemplate
                ))
                .from(place)
                .where(
                        distanceTemplate.loe(radius),
                        categoryIdEq(categoryId)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(createOrderSpecifiers(pageable, distanceTemplate))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(place.count())
                .from(place)
                .where(
                        distanceTemplate.loe(radius),
                        categoryIdEq(categoryId)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Map<Long, List<String>> findCategoryNamesMapByPlaceIds(final List<Long> placeIds) {
        if (placeIds == null || placeIds.isEmpty()) {
            return new HashMap<>();
        }

        List<Tuple> tuples = queryFactory
                .select(place.id, category.name)
                .from(place)
                .leftJoin(place.categories, category)
                .where(place.id.in(placeIds))
                .fetch();

        // 결과를 Map<Long, List<String>> 형태로 변환
        return tuples.stream().collect(
                Collectors.groupingBy(
                        tuple -> tuple.get(place.id),
                        Collectors.mapping(tuple -> tuple.get(category.name), Collectors.toList())
                )
        );
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? place.categories.any().id.eq(categoryId) : null;
    }

    private OrderSpecifier<?>[] createOrderSpecifiers(Pageable pageable, NumberTemplate<Double> distanceTemplate) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                String prop = order.getProperty();

                switch (prop) {
                    case "likeCount":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, place.likeCount));
                        break;
                    case "id": // '최신순' 정렬
                        orderSpecifiers.add(new OrderSpecifier<>(direction, place.id));
                        break;
                    case "distance":
                        orderSpecifiers.add(new OrderSpecifier<>(direction, distanceTemplate));
                        break;
                }
            }
        }

        // 정렬 조건이 없는 경우 기본값 (거리순)
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, distanceTemplate));
        }
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

}
