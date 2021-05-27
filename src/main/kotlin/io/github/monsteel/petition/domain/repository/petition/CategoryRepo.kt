package io.github.monsteel.petition.domain.repository.petition

import io.github.monsteel.petition.domain.entity.petition.Category
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
interface CategoryRepo: JpaRepository<Category, Long> {
    fun getByIdx(idx: Long): Category
}

