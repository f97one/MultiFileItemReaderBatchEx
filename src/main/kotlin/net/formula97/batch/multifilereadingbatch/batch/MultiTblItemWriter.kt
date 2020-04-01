package net.formula97.batch.multifilereadingbatch.batch

import net.formula97.batch.multifilereadingbatch.domain.OrgFile
import net.formula97.batch.multifilereadingbatch.domain.OutputTbl1
import net.formula97.batch.multifilereadingbatch.domain.OutputTbl2
import net.formula97.batch.multifilereadingbatch.repository.OutputTbl1Repository
import net.formula97.batch.multifilereadingbatch.repository.OutputTbl2Repository
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class MultiTblItemWriter(private var outputTbl1Repository: OutputTbl1Repository, private var outputTbl2Repository: OutputTbl2Repository) : ItemWriter<OrgFile> {

    override fun write(orgFiles: MutableList<out OrgFile>) {
        for (of: OrgFile in orgFiles) {
            when (of.itemType) {
                1 -> {
                    val o1 = OutputTbl1(subject = of.subject, id = null)
                    outputTbl1Repository.save(o1)
                }
                2 -> {
                    val o2 = OutputTbl2(subject = of.subject, id = null)
                    outputTbl2Repository.save(o2)
                }
            }
        }

        outputTbl1Repository.flush()
        outputTbl2Repository.flush()
    }
}