package net.formula97.batch.multifilereadingbatch.domain

import javax.persistence.*

@Entity(name = "output_tbl2")
data class OutputTbl2(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, columnDefinition = "int")
        var id: Int?,
        @Column(name = "subject", length = 128)
        var subject: String?
) {
}