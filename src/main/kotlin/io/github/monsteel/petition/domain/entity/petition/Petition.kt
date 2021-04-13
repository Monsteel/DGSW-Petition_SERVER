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
    @Column(name = "idx",unique = true)
    var idx: Long? = null

    // 청원인 아이디
    @Column(nullable = false)
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
    @Column(nullable = false)
    var category: String? = null

    // 청원 제목
    @Column(nullable = false)
    var title:String? = null

    // 청원 내용
    @Column(nullable = false)
    var content:String? = null


    // 검색태그
    @Column(nullable = true)
    var firstKeyword:String? = null

    // 검색태그
    @Column(nullable = true)
    var secondKeyword:String? = null

    // 검색태그
    @Column(nullable = true)
    var thirdKeyword:String? = null


    fun init(
        writerID: String?,
        category: String?,
        title: String?,
        content: String?,
        firstKeyword: String?,
        secondKeyword: String?,
        thirdKeyword: String?
    ) {
        this.writerID = writerID
        this.category = category
        this.title = title
        this.content = content
        this.firstKeyword = firstKeyword
        this.secondKeyword = secondKeyword
        this.thirdKeyword = thirdKeyword
    }

    fun mod(
        category: String?,
        title: String?,
        content: String?,
        firstKeyword: String?,
        secondKeyword: String?,
        thirdKeyword: String?
    ) {
        this.category = category
        this.title = title
        this.content = content
        this.firstKeyword = firstKeyword
        this.secondKeyword = secondKeyword
        this.thirdKeyword = thirdKeyword
    }


}