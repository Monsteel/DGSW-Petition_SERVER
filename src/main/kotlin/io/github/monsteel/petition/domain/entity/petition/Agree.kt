package io.github.monsteel.petition.domain.entity.petition

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity(name = "agree")
class Agree {
    // 동의 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    // 청원 번호
    @Column(nullable = false, unique = true)
    var petitionIdx: Long? = null

    // 동의자 아이디
    @Column(nullable = false, unique = true)
    var writerID: String? = null

    // 동의 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var createdAt: Date = Date()

    // 동의 내용
    @Column(nullable = false, unique = true)
    var content:String? = null
}