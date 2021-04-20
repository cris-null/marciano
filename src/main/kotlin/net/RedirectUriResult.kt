package net

sealed class RedirectUriResult {

    data class Success(val code: String) : RedirectUriResult()
    data class Error(val message: String) : RedirectUriResult()
}