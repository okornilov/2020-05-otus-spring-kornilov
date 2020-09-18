package ru.otus.okornilov.homework14.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Date;


@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final Job convertBookJob;
    private final JobLauncher jobLauncher;

    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = "sm")
    public void startMigration() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        final JobParameters jobParameters = new JobParametersBuilder()
                .addDate("startDate", new Date())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(convertBookJob, jobParameters);
        System.out.println(execution);
    }
}
