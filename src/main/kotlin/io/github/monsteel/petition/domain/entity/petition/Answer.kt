package io.github.monsteel.petition.domain.entity.petition

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity(name = "answer")
class Answer {
    // 답변 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    // 청원 번호
    @Column(nullable = false, unique = true)
    var petitionIdx: Long? = null

    // 답변자 아이디
    @Column(nullable = false, unique = true)
    var writerID: String? = null

    // 답변 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var createdAt: Date = Date()

    // 답변 내용
    @Column(nullable = false, unique = true)
    var content:String? = null
}