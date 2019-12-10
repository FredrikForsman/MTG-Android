package se.forsman.deckbuilder.core.exception

sealed class Failure {
    object DatabaseError : Failure()
    abstract class SpecificError: Failure()
}