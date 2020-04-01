package net.formula97.batch.multifilereadingbatch.repository

import net.formula97.batch.multifilereadingbatch.domain.OutputTbl1
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OutputTbl1Repository: JpaRepository<OutputTbl1, Int> {
}