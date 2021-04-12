package io.github.monsteel.petition.domain.repository.petition

import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.util.extension.getPetitionValidityDate
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PetitionRepo: JpaRepository<Petition, Long> {
    fun findAllByTitleContainingOrFKeywordContainingOrSKeywordContainingOrTKeywordContaining(
        title: String,
        FKeyword: String,
        SKeyword: String,
        TKeyword: String,
        pageRequest: PageRequest
    ): List<Petition>

    fun findAllByIdx(idx: Long, pageRequest: PageRequest):Petition

    fun findAll(pageRequest: PageRequest):List<Petition>

    /**
     * 유효한(만료기간이 지나지 않은) 청원 조회
     */
    @Query("SELECT * FROM petition WHERE expiration_date < :today", nativeQuery = true)
    fun findValidPetition(pageRequest: PageRequest, @Param("today") today: Date = Date()):List<Petition>

    /**
     * 만료된(만료기간이 지난) 청원 조회
     */
    @Query("SELECT * FROM petition WHERE expiration_date < :today", nativeQuery = true)
    fun findInvalidPetition(pageRequest: PageRequest, @Param("today") today: Date = Date()):List<Petition>


    @Modifying
    @Query("UPDATE Petition p " +
            "SET p.category = petition.category, p.title = petition.title, p.content = petition.content, p.fKeyword = petition.fKeyword, p.sKeyword= petition.sKeyword, p.tKeyword = petition.tKeyword" +
            "WHERE p.idx = :idx")
    fun updatePetition(@Param("idx") idx: Long, petition: Petition)

    //진행중 청원

    //종료된 청원

    //완료된 청원
}


