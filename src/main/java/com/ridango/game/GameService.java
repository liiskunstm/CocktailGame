package com.ridango.game;

import lombok.Data;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Data
public class GameService {
    private int numberOfTries;
    private int score;
    private int highestScore;
    private boolean continuePlaying;
    private String hiddenWord;
    private Drink currentDrink;
    private final DrinkApiService drinkApiService;
    private final HashSet<String> playedDrinks = new HashSet<>();
    private final Scanner scanner;

    public GameService(DrinkApiService drinkApiService) {
        this.drinkApiService = drinkApiService;
        this.numberOfTries = 5;
        this.score = 0;
        this.highestScore = 0;
        this.continuePlaying = true;
        this.scanner = new Scanner(System.in);
    }

    public void startGame() {
        while (continuePlaying) {
            playRound();
        }
    }

    private void playRound() {
        numberOfTries = 5;
        boolean gameOver = false;
        while (!gameOver) {
            getNewDrink();
            hiddenWord = getHiddenDrinkName(currentDrink.getStrDrink());
            System.out.println();
            System.out.println("Guess the Cocktail! You have " + numberOfTries + " attempts left.");
            System.out.println("Instructions: " + currentDrink.getStrInstructions());
            System.out.println(currentDrink.strDrink);
            System.out.println("Cocktail name: " + hiddenWord);

            while (numberOfTries > 0) {
                String guess = scanner.nextLine();
                if (guess.equalsIgnoreCase(currentDrink.getStrDrink())) {
                    processWin();
                    return;
                } else {
                    processIncorrectGuess();
                }
            }
            processLoss();
            gameOver = true;
        }
    }

    public void getNewDrink() {
        currentDrink = drinkApiService.getRandomDrinkFromApi();
        while (playedDrinks.contains(currentDrink.strDrink)) {
            currentDrink = drinkApiService.getRandomDrinkFromApi();
        }
        playedDrinks.add(currentDrink.getStrDrink());
    }

     public String getHiddenDrinkName(String drinkName) {
        return drinkName.replaceAll("[A-Za-z0-9]", "_");
    }

    public void revealHints() {
        switch (numberOfTries) {
            case 4 -> System.out.println("Hint 1: Drink category - " + currentDrink.getStrCategory());
            case 3 -> System.out.println("Hint 2: Glass type - " + currentDrink.getStrGlass());
            case 2 -> System.out.println("Hint 3: Ingredients include - " + currentDrink.getIngredients());
            case 1 -> System.out.println("Hint 4: Picture of the drink - " + currentDrink.getStrDrinkThumb());
        }
    }

    public void revealALetter() {
        boolean letterRevealed = false;
        if (!hiddenWord.equals(currentDrink.strDrink)) {
            while (!letterRevealed) {
                int randomIndex = ThreadLocalRandom.current().nextInt(0, hiddenWord.length());
                StringBuilder sb = new StringBuilder(hiddenWord);
                char letterToReveal = hiddenWord.charAt(randomIndex);

                if (letterToReveal != ' ') {
                    if (hiddenWord.charAt(randomIndex) == '_') {
                        sb.setCharAt(randomIndex, currentDrink.strDrink.charAt(randomIndex));
                        hiddenWord = sb.toString();
                        letterRevealed = true;
                    }
                }
            }
            System.out.println(hiddenWord);
        }
    }

    public void processWin() {
        System.out.println("Congratulations! Your guess is correct! The correct drink is indeed " + currentDrink.getStrDrink() + ".");
        score += numberOfTries;
        System.out.println("Your score is: " + score);
    }

    public void processIncorrectGuess() {
        numberOfTries--;
        if (numberOfTries > 0) {
            System.out.println("Attempts left: " + numberOfTries);
            revealHints();
            revealALetter();
        }
    }

    public void processLoss() {
        System.out.println("Sorry, you lost! The correct answer was " + currentDrink.getStrDrink());
        System.out.println("Do you want to keep playing? (yes/no)");
        String answer = scanner.nextLine();
        while (!answer.equalsIgnoreCase("yes") && !answer.equalsIgnoreCase("no")) {
            System.out.println("Unrecognizable answer, please answer 'yes' or 'no'");
            answer = scanner.nextLine();
        }
        if (answer.equalsIgnoreCase("no")) {
            continuePlaying = false;
            System.out.println("Thanks for playing! Highest score achieved: " + score);
            if (score > highestScore) {
                highestScore = score;
                System.out.println("This is a new high score!");
            }
        }
    }
}
