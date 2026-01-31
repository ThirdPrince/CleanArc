package com.sample.clean.domain.usecase

import com.sample.clean.domain.repo.UserRepository
import com.sample.clean.domain.repo.model.User

class GetUsersUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
    }
}