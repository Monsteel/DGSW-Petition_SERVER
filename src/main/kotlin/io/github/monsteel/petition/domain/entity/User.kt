package io.github.monsteel.petition.domain.entity

import io.github.monsteel.petition.util.enum.PermissionType
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity(name = "user")
class User() {
    // 순서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    // 아이디
    @Column(nullable = false, unique = true)
    var userID: String? = null

    // 권한
    @Column(nullable = false)
    var permissionType: PermissionType? = null

    // 가입 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var createdAt: Date = Date()

    constructor(userID: String?, permissionType: PermissionType?) {
        this.userID = userID
        this.permissionType = permissionType
    }
}