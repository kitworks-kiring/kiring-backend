package io.dodn.springboot.storage.db.plane.entity;


import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import io.dodn.springboot.storage.db.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PLANE")
public class Plane extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) // 일반적으로 지연 로딩 권장
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private Member sender;

    // 받는 사람 (User 참조)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "id", nullable = false)
    private Member receiver;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "is_printable")
    private Boolean isPrintable = false;

    public Plane() {
    }

    public Plane(final Member sender, final Member receiver, final String message, final Boolean isPrintable) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isPrintable = isPrintable;
    }


    public Member getSender() {
        return sender;
    }

    public Member getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getPrintable() {
        return isPrintable;
    }

    public static Plane create(Member sender, Member receiver, String messageContent) {
        Plane plane = new Plane();
        plane.sender = sender;
        plane.receiver = receiver;
        plane.message = messageContent;
        plane.isPrintable = false;
        return plane;
    }
    public void markAsRead() {
        // 이미 읽음 처리된 경우 변경하지 않거나, 특정 로직 추가 가능
        if (this.isPrintable == null || !this.isPrintable) {
            this.isPrintable = true;
        }
    }
}
