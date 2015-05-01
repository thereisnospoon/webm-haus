package my.thereisnospoon.webm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private enum OS {

		WINDOWS {

			@Override
			public String getProfile() {
				return "windows";
			}
		},

		LINUX {

			@Override
			public String getProfile() {
				return "linux";
			}
		};

		public abstract String getProfile();
	}

	private static final Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {

		log.debug("Initializing app");

		OS os = determineEnvironment();
		applicationContext.getEnvironment().addActiveProfile(os.getProfile());
	}

	private static OS determineEnvironment() {

		String osName = System.getProperty("os.name").toLowerCase();

		log.debug("OS name = {}", osName);

		if (osName.contains("windows")) {
			return OS.WINDOWS;
		}
		if (osName.contains("linux")) {
			return OS.LINUX;
		}
		return null;
	}
}
