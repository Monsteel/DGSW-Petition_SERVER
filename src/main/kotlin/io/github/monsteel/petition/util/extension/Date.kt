package io.github.monsteel.petition.util.extension

import io.github.monsteel.petition.util.Constant.PETITION_VALIDITY_DAY
import java.text.SimpleDateFormat
import java.util.*


fun Date.isValidPetiton():Boolean {
    return this.after(Date())
}

fun Date.getPetitionValidityDate(): Date {
    val cal = Calendar.getInstance()

    cal.time = this
    cal.add(Calendar.DATE, PETITION_VALIDITY_DAY)

    return Date(cal.timeInMillis)
}

fun Date.toISOString(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    return sdf.format(this)
}