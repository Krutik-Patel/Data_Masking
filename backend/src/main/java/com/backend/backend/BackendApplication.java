package com.backend.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import com.backend.backend.engine.Engine;

import java.io.File;
import java.nio.file.Files;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BackendApplication.class);

		// Allow web server to run only if no CLI args provided
		if (args.length > 0) {
			app.setWebApplicationType(WebApplicationType.NONE);
		}

		app.run(args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			String file1Path = null;
			String file2Path = null;
			String outputPath = "masked_output.json"; // default output file

			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("--data"))
					file1Path = args[++i];
				else if (args[i].equals("--config"))
					file2Path = args[++i];
				else if (args[i].equals("--out"))
					outputPath = args[++i];
			}

			if (file1Path == null || file2Path == null) {
				System.out.println(
						"Usage: java -jar app.jar --data path/to/data.json --config path/to/config.json [--out path/to/output.json]");
				return;
			}

			// Instantiate engine directly (just like your controller)
			Engine engine = Engine.getInstance();

			// Load data file
			File file1 = new File(file1Path);
			String file1Content = Files.readString(file1.toPath());
			engine.putDataFileFromText(file1Content, file1.getName()); // You'll add this method

			// Load config file
			File file2 = new File(file2Path);
			String file2Content = Files.readString(file2.toPath());
			engine.putConfigFromText(file2Content, file2.getName()); // You'll add this too

			// Perform masking
			if (!engine.isReadyForMasking()) {
				System.out.println("Masking failed: Please check if both files are valid.");
				return;
			}

			String output = engine.maskData();
			Files.writeString(new File(outputPath).toPath(), output);

			System.out.println("✅ Masked output written to: " + outputPath);
		};
	}
}
