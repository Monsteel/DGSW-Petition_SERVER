package io.github.monsteel.petition.domain.entity.petition

import io.github.monsteel.petition.util.extension.getPetitionValidityDate
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity(name = "petition")
class Petition {
    // 청원 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    // 청원인 아이디
    @Column(nullable = false, unique = true)
    var writerID: String? = null

    // 청원 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var createdAt: Date = Date()

    // 청원 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var expirationDate: Date = createdAt.getPetitionValidityDate()

    // 카테고리
    @Column(nullable = false, unique = true)
    var category: String? = null

    // 청원 제목
    @Column(nullable = false, unique = true)
    var title:String? = null

    // 청원 내용
    @Column(nullable = false, unique = true)
    var content:String? = null


    // 검색태그
    @Column(nullable = true, unique = true)
    var fKeyword:String? = null

    // 검색태그
    @Column(nullable = true, unique = true)
    var sKeyword:String? = null

    // 검색태그
    @Column(nullable = true, unique = true)
    var tKeyword:String? = null


}