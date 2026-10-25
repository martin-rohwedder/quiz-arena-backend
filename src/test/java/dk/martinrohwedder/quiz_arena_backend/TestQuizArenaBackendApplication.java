package dk.martinrohwedder.quiz_arena_backend;

import org.springframework.boot.SpringApplication;

public class TestQuizArenaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.from(QuizArenaBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
