package io.github.monsteel.petition.service.petition.bulletin

import io.github.monsteel.petition.domain.dto.petition.bulletin.PetitionDto
import io.github.monsteel.petition.domain.entity.User
import io.github.monsteel.petition.domain.entity.petition.Petition
import io.github.monsteel.petition.util.enum.PetitionFetchType
import reactor.core.publisher.Mono

interface PetitionService {
    fun fetchPetitionDetailInfo(idx: Long): Mono<Petition>

    fun fetchPetitions(page:Int, size:Int, type: PetitionFetchType = PetitionFetchType.ALL): Mono<List<Petition>>

    fun searchPetition(page:Int, size:Int, keyword:String): Mono<List<Petition>>

    fun fetchPetitionRanking(amount: Int): Mono<List<Petition>>

    fun fetchAwaitingPetition(): Mono<List<Petition>>

    fun fetchAwaitingPetitionCount(): Mono<Int>

    fun writePetition(petitionDto: PetitionDto, user: User): Mono<Unit>

    fun editPetition(idx:Long, petitionDto: PetitionDto, user: User): Mono<Unit>

    fun deletePetition(idx:Long, user: User): Mono<Unit>
}