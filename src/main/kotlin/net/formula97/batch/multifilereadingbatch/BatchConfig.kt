package net.formula97.batch.multifilereadingbatch

import net.formula97.batch.multifilereadingbatch.batch.MultiTblItemWriter
import net.formula97.batch.multifilereadingbatch.domain.OrgFile
import net.formula97.batch.multifilereadingbatch.repository.OutputTbl1Repository
import net.formula97.batch.multifilereadingbatch.repository.OutputTbl2Repository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.MultiResourceItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.batch.item.file.mapping.DefaultLineMapper
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

@Configuration
@EnableBatchProcessing
class BatchConfig {
    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory
    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Bean
    fun directoryOrgReader(orgReader: FlatFileItemReader<OrgFile>): MultiResourceItemReader<OrgFile> {
        val dir = Path.of("C:", "work", "readtest")
        val dirFiles = Files.list(dir).collect(Collectors.toList())
        val csvResList = mutableListOf<Resource>()
        for (p in dirFiles) {
            csvResList.add(FileSystemResource(p))
        }

        val reader = MultiResourceItemReader<OrgFile>()
        reader.setResources(csvResList.toTypedArray())
        reader.setDelegate(orgReader)
        return reader
    }

    @Bean
    fun orgReader(): FlatFileItemReader<OrgFile> {
        val mapper = DefaultLineMapper<OrgFile>()

        val delimitedLineTokenizer = DelimitedLineTokenizer()
        delimitedLineTokenizer.setNames("id", "subject", "itemType")
        mapper.setLineTokenizer(delimitedLineTokenizer)
        val fieldSetMapper = BeanWrapperFieldSetMapper<OrgFile>()
        fieldSetMapper.setTargetType(OrgFile::class.java)
        mapper.setFieldSetMapper(fieldSetMapper)

        return FlatFileItemReaderBuilder<OrgFile>()
                .name("orgFileReader")
                .linesToSkip(1)
                .encoding("windows-31j")
                .lineMapper(mapper)
                .build()
    }
    @Bean
    fun orgItemWriter(outputTbl1Repository: OutputTbl1Repository,  outputTbl2Repository: OutputTbl2Repository): MultiTblItemWriter {
        return MultiTblItemWriter(outputTbl1Repository, outputTbl2Repository)
    }

    @Bean
    fun step1(directoryOrgReader: MultiResourceItemReader<OrgFile>, orgItemWriter: MultiTblItemWriter): Step {
        return stepBuilderFactory.get("step1")
                .chunk<OrgFile, OrgFile>(10)
                .reader(directoryOrgReader)
                .writer(orgItemWriter)
                .build()
    }
    @Bean
    fun job1(step1: Step): Job {
        return jobBuilderFactory.get("job1")
                .flow(step1)
                .end()
                .build()
    }
}