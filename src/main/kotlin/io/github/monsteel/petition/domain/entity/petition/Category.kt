package io.github.monsteel.petition.domain.entity.petition

import javax.persistence.*

@Entity(name = "category")
class Category() {
    // 카테고리 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    // 청원 날짜
    @Column(nullable = false)
    var categoryName: String? = null
}