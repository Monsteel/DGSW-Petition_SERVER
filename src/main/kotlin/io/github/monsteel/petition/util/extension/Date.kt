package io.github.monsteel.petition.util.extension

import io.github.monsteel.petition.util.Constant.PETITION_VALIDITY_DAY
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.isValidPetiton():Boolean {
    return this.before(getPetitionValidityDate())
}

fun Date.getPetitionValidityDate(): Date {
    val cal = Calendar.getInstance()

    cal.time = this
    cal.add(Calendar.DATE, PETITION_VALIDITY_DAY)

    return Date(cal.timeInMillis)
}

