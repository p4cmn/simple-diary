package com.artem.records.model.repository

import com.artem.records.model.dao.UserDao
import com.artem.records.model.entity.User

class UserRepository private constructor(
    private val userDao: UserDao
) {

    suspend fun registerUser(user: User): Long {
        return userDao.insert(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    companion object {
        @Volatile private var instance: UserRepository? = null

        fun getInstance(userDao: UserDao): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(userDao).also { instance = it }
            }
        }
    }

}
