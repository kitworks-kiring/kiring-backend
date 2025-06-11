package io.dodn.springboot.storage.db.matzip.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "place")
public class Place extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "location", nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location;

    @Column(name = "longitude", precision = 11, scale = 8) // DB의 DECIMAL(11, 8)에 매핑
    private BigDecimal longitude;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<>();

    protected Place() {
    }

    public Place(final String name, final String address, final String phoneNumber, final String description, final int likeCount, final Point location, final List<Menu> menus) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.likeCount = likeCount;
        this.location = location;
        this.menus = menus;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public Point getLocation() {
        return location;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public int getLikeCount() {
        return likeCount;
    }

    // == 연관관계 편의 메서드 == //
    public void addMenu(Menu menu) {
        this.menus.add(menu);
        if (menu.getPlace() != this) {
            menu.setPlace(this);
        }
    }

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
