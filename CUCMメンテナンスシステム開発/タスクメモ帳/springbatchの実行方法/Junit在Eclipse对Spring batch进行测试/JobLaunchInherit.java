/**
 * 
 */
package test.com.juxtapose.example.ch04;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.test.JobLauncherTestUtils;

//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:ch04/job/job-inherit.xml"})


public class JobLaunchInherit {
	
//	@Autowired
//    private JobLauncherTestUtils jobLauncherTestUtils;
	
	
	/**
	 * .<br>
	 * @param jobPath	
	 * @param jobName	
	 * @param builder	
	 */
	@Test
	public  void executeJob() {
		ApplicationContext context = new ClassPathXmlApplicationContext("ch04/job/job-inherit.xml");
		JobLauncherTestUtils jobLauncherTestUtils = (JobLauncherTestUtils) context.getBean("jobLauncherTestUtils");
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		JobRepository jobRepository = (JobRepository) context.getBean("jobRepository");
		Job job = (Job) context.getBean("billJob");
		JobParametersBuilder builder = new JobParametersBuilder().addDate("date", new Date());
		try {
			jobLauncherTestUtils.setJob(job);
			jobLauncherTestUtils.setJobLauncher(jobLauncher);
			jobLauncherTestUtils.setJobRepository(jobRepository);
			JobExecution result = jobLauncherTestUtils.launchJob(builder.toJobParameters());			
			System.out.println(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

