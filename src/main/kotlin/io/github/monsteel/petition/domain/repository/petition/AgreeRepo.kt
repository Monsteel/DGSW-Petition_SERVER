package io.github.monsteel.petition.domain.repository.petition

import io.github.monsteel.petition.domain.entity.petition.Agree
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AgreeRepo:JpaRepository<Agree, Long> {
    fun findAllByPetitionIdx(petitionIdx: Long, pageRequest: PageRequest): List<Agree>
    fun findAllByPetitionIdx(petitionIdx: Long): List<Agree>
}