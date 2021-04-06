package io.github.monsteel.petition.domain.repository

import io.github.monsteel.petition.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<User, String> {
    fun findByUserID(userID: String): User?
    fun findByIdx(idx: Long): User?
}