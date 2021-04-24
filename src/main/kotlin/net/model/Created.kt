package net.model

interface Created {
    /**
     * The time of creation in local epoch-second format. ex: 1331042771.0
     *
     * Reddit says that the type is a [Long] but since it ends in ".0" you
     * must store it as a double. However, in some tests, the response did not
     * have a double, so maybe Long is fine? Todo: test if it's fine to use Long instead of Double.
     */
    val created: Long
}