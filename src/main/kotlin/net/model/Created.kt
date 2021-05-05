package net.model

import java.math.BigDecimal

interface Created {
    /**
     * The time of creation in local epoch-second format. ex: 1331042771.0
     *
     * Reddit says that the type is a [Long] but since it ends in ".0" you
     * must store it as a [Double].
     */
    val created: Double
}