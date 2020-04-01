package net.formula97.batch.multifilereadingbatch.repository

import net.formula97.batch.multifilereadingbatch.domain.OutputTbl2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OutputTbl2Repository: JpaRepository<OutputTbl2, Int> {
}