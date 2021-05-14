package io.github.monsteel.petition.domain.repository.petition

import io.github.monsteel.petition.domain.entity.petition.Agree
import io.github.monsteel.petition.domain.entity.petition.Petition
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AgreeRepo:JpaRepository<Agree, Long> {
    fun findAllByPetitionIdx(petitionIdx: Long, pageable: Pageable): List<Agree>
    fun findAllByPetitionIdx(petitionIdx: Long): List<Agree>
    fun findAllByPetitionIdxAndUserUserID(petition_idx: Long, user_userID: String): List<Agree>

    @Query("SELECT new io.github.monsteel.petition.domain.repository.petition.Rank(petition.idx, COUNT(petition)) FROM agree GROUP BY petition.idx ORDER BY COUNT(petition) DESC")
    fun findRanking(pageable: Pageable): List<Rank>
}

class Rank {
    var petitionIdx: Long
    var count:Long

    constructor(petitionIdx: Long, count: Long) {
        this.petitionIdx = petitionIdx
        this.count = count
    }
}