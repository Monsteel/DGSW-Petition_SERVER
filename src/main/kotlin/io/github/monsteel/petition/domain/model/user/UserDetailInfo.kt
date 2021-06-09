package io.github.monsteel.petition.domain.model.user

import io.github.monsteel.petition.util.enum.PermissionType
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

class UserDetailInfo(var idx: Long, var userID: String, var permissionType: PermissionType, var createdAt: String) {
}
