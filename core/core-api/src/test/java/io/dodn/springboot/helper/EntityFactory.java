package io.dodn.springboot.helper;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import io.dodn.springboot.storage.db.matzip.entity.Place;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class EntityFactory {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Place createPlace(Long id, String name) {
        Point mockPoint = geometryFactory.createPoint(new Coordinate(127.0, 37.0));
        Place place = new Place(name, "테스트 주소", "한식", "url", "010-0000-0000", "설명", 0, mockPoint, new ArrayList<>());

        if (id != null) {
            Field idField = ReflectionUtils.findField(BaseEntity.class, "id");
            ReflectionUtils.makeAccessible(idField);
            ReflectionUtils.setField(idField, place, id);
        }
        return place;
    }
}
