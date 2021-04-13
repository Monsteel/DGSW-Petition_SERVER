package io.github.monsteel.petition.service.petition.bulletin

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionWriteDto
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.util.enum.PetitionFetchType

interface PetitionService {
    fun fetchPetitions(page:Int, size:Int, type: PetitionFetchType = PetitionFetchType.ALL): List<Petition>

    fun searchPetition(page:Int, size:Int, keyword:String): List<Petition>

    fun fetchTopTenPetition(page:Int, size:Int): List<Petition>

    fun writePetition(petitionWriteDto: PetitionWriteDto)

    fun editPetition(idx:Long, petitionWriteDto: PetitionWriteDto)

    fun deletePetition(idx:Long)
}