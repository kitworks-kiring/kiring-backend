package io.dodn.springboot.storage.db.matzip.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "place")
public class Place extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "kiring_category")
    private String category;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "place_category", // 중간 테이블 이름
            joinColumns = @JoinColumn(name = "place_id"), // Place 테이블을 참조하는 외래 키
            inverseJoinColumns = @JoinColumn(name = "category_id") // Category 테이블을 참조하는 외래 키
    )
    private Set<Category> categories = new HashSet<>(); // Set을 사용하여 중복 카테고리 방지


    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "location", nullable = false, columnDefinition = "POINT SRID 4326")
    private Point location;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Menu> menus = new ArrayList<>();

    protected Place() {
    }

    public Place(final String name, final String address, final String category, final String imageUrl, final String phoneNumber, final String description, final int likeCount, final Point location, final List<Menu> menus) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategories(final Set<Category> categories) {
        this.categories = categories;
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

    public void addCategory(Category category) {
        this.categories.add(category);
    }


    @Override
    public String toString() {
        return "Place{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", categories=" + categories +
                ", imageUrl='" + imageUrl + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", likeCount=" + likeCount +
                ", location=" + location +
                ", menus=" + menus +
                '}';
    }
}
