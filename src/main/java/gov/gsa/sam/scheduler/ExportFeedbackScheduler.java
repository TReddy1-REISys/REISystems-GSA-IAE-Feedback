package gov.gsa.sam.scheduler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import gov.gsa.sam.domain.Feedback;
import gov.gsa.sam.service.FeedbackService;
import gov.gsa.sam.service.MailSenderService;
import gov.gsa.sam.utilities.ExportXLSUtil;

@Component
public class ExportFeedbackScheduler {

	private static final Logger log = LoggerFactory.getLogger(ExportFeedbackScheduler.class);
	public static final String CURR_PATH = System.getProperty("user.dir");

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private MailSenderService mailSenderService;

	@Autowired
	private ExportXLSUtil exportXLSUtil;

	@Resource
	Environment environment;

	// Scheduled at 9:55 pm
	@Scheduled(cron = "0 55 21 * * ?", zone = "America/New_York")
	public void exportFeedbackAndSendEmail() {
		SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
		log.info("Export feedback job started for " + FORMATTER.format(new Date()));

		String FILE_NAME = CURR_PATH + environment.getProperty("excel.path") + FORMATTER.format(new Date()) + ".XLSX";

		try {
			// Get All feedbacks from DB
			List<Feedback> feedbackList = getAllFeedbacks();

			// Export data to XLS
			String completeFileName = exportXLSUtil.exportXLS(feedbackList);
			String toAddressArray = environment.getProperty("mailing.to.list");
			String mailSubject = environment.getProperty("mailing.subject") + " | " + System.getenv("DEPLOY_ENV");
			String mailDetails = environment.getProperty("mailing.text") + FORMATTER.format(new Date()) + "\n "
					+ environment.getProperty("mailing.text1") + feedbackList.size() + "\n"
					+ environment.getProperty("mailing.footer.message");

			// Send Email Notification
			if (!completeFileName.isEmpty()) {
				String[] toAddress = toAddressArray.split(",");
				for (String to : toAddress) {
					mailSenderService.sendMailMessage(to, mailSubject, mailDetails, completeFileName);
				}
			}

			File file = new File(FILE_NAME);

			if (file.delete()) {
				log.info(file.getName() + " is deleted!");
			} else {
				log.info("Delete operation is failed.");
			}
		} catch (Exception e) {
			log.warn("Following Exception Occured::" + e);
		}

	}

	public List<Feedback> getAllFeedbacks() {
		// Getting all the list of Feedbacks from database
		List<Feedback> listOfFeedbacks = new ArrayList<>();
		try {
			listOfFeedbacks = feedbackService.getAllFeedbackReport();
		} catch (Exception e) {
			log.warn("Following Exception Occured::" + e);
		}
		return listOfFeedbacks;
	}

}
