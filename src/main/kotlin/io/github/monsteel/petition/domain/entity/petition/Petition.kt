package io.github.monsteel.petition.domain.entity.petition

import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.util.extension.getPetitionValidityDate
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity(name = "petition")
class Petition() {
    // 청원 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    @ManyToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(columnDefinition = "userID")
    var user: User? = null

    // 청원 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var createdAt: Date? = null

    // 청원 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var expirationDate: Date? = null

    // 카테고리
    @ManyToOne(targetEntity = Category::class, fetch = FetchType.EAGER)
    @JoinColumn(columnDefinition = "idx")
    var category: Category? = null

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

    // 답변 내용
    @Column(nullable = false)
    var isAnswer:Boolean? = null

    constructor(user: User?, category: Category?, title: String?, content: String?, firstKeyword: String?, secondKeyword: String?, thirdKeyword: String?): this() {
        this.user = user
        this.category = category
        this.title = title
        this.content = content
        this.firstKeyword = firstKeyword
        this.secondKeyword = secondKeyword
        this.thirdKeyword = thirdKeyword

        this.createdAt = Date()
        this.expirationDate = createdAt!!.getPetitionValidityDate()
        this.isAnswer = false
    }

    fun mod(
        category: Category?,
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