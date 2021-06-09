package io.github.monsteel.petition.domain.entity.petition

import io.github.monsteel.petition.domain.entity.User
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity(name = "answer")
class Answer() {
    // 답변 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    @ManyToOne(targetEntity = Petition::class, fetch = FetchType.EAGER)
    @JoinColumn(columnDefinition = "idx")
    var petition: Petition? = null

    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(columnDefinition = "userID")
    var user: User? = null

    // 답변 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var createdAt: Date? = null

    // 답변 내용
    @Column(nullable = false, unique = true)
    var content:String? = null

    constructor(petition: Petition?, user: User?, content: String?): this() {
        this.petition = petition
        this.user = user
        this.content = content

        this.createdAt = Date()
    }
}