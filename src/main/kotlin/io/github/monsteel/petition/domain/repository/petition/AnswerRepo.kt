package io.github.monsteel.petition.domain.repository.petition

import io.github.monsteel.petition.domain.entity.petition.Answer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AnswerRepo:JpaRepository<Answer,Long> {
    fun findAllByPetitionIdx(petitionIdx: Long): List<Answer>

    @Query("SELECT COUNT(answer) FROM answer answer")
    fun findAllAnswerCount(): Int
}