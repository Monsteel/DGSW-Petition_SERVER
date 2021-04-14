package io.github.monsteel.petition.service.petition.bulletin

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.util.enum.PetitionFetchType
import reactor.core.publisher.Mono

interface PetitionService {
    fun fetchPetitions(page:Int, size:Int, type: PetitionFetchType = PetitionFetchType.ALL): Mono<List<Petition>>

    fun searchPetition(page:Int, size:Int, keyword:String): Mono<List<Petition>>

    fun fetchTopTenPetition(page:Int, size:Int): List<Petition>

    fun writePetition(petitionDto: PetitionDto, userID:String): Mono<Unit>

    fun editPetition(idx:Long, petitionDto: PetitionDto, userID:String): Mono<Unit>

    fun deletePetition(idx:Long, userID:String): Mono<Unit>
}