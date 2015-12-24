package my.thereisnospoon.webm.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
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
