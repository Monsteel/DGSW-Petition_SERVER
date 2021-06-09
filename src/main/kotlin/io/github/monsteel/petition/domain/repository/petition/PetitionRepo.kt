package io.github.monsteel.petition.domain.repository.petition

import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.util.extension.getPetitionValidityDate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PetitionRepo: JpaRepository<Petition, Long> {

    fun findAllByTitleContainsOrFirstKeywordContainsOrSecondKeywordContainsOrThirdKeywordContains(
        title: String,
        firstKeyword: String,
        secondKeyword: String,
        thirdKeyword: String,
        pageable: Pageable
    ):List<Petition>

    fun findByIdx(idx: Long): Petition

    /**
     * 유효한(만료기간이 지나지 않은) 청원 조회
     */
    fun findAllByExpirationDateAfter(pageable: Pageable, expirationDate: Date = Date()): List<Petition>

    /**
     * 만료된(만료기간이 지난) 청원 조회
     */
    fun findAllByExpirationDateBefore(pageable: Pageable, expirationDate: Date = Date()): List<Petition>
}


