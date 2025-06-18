package io.dodn.springboot.storage.db.matzip.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_signature")
    private Boolean isSignature = false;

    // 여러 메뉴(N)가 하나의 맛집(1)에 속함을 나타냅니다.
    // LAZY 로딩은 Menu 엔티티 조회 시 Place 정보를 즉시 로딩하지 않고,
    // 실제로 place 필드를 사용할 때 로딩하여 성능을 최적화합니다. (권장)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false) // 외래 키 컬럼 지정
    private Place place;


    protected Menu() {
    }

    public Menu(final String name, final Integer price, final String description, final String imageUrl, final Boolean isSignature, final Place place) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isSignature = isSignature;
        this.place = place;
    }

    // == 연관관계 편의 메서드 == //
    // 양방향 관계에서 사용
    public void setPlace(Place place) {
        this.place = place;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getSignature() {
        return isSignature;
    }

    public Place getPlace() {
        return place;
    }
}
