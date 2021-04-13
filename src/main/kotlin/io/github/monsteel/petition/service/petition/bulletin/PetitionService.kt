package io.github.monsteel.petition.service.petition.bulletin

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.util.enum.PetitionFetchType

interface PetitionService {
    fun fetchPetitions(page:Int, size:Int, type: PetitionFetchType = PetitionFetchType.ALL): List<Petition>

    fun searchPetition(page:Int, size:Int, keyword:String): List<Petition>

    fun fetchTopTenPetition(page:Int, size:Int): List<Petition>

    fun writePetition(petitionDto: PetitionDto, userID:String)

    fun editPetition(idx:Long, petitionDto: PetitionDto, userID:String)

    fun deletePetition(idx:Long, userID:String)
}