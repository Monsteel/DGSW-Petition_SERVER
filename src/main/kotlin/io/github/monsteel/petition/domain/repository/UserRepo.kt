package io.github.monsteel.petition.domain.repository

import io.github.monsteel.petition.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepo : JpaRepository<User, String> {
    fun findAllByUserID(userID: String): List<User>
    fun findByIdx(idx: Long): User?
}