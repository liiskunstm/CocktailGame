package com.ridango;

import com.ridango.game.Drink;
import com.ridango.game.DrinkApiService;
import com.ridango.game.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private DrinkApiService drinkApiService;

    @InjectMocks
    private GameService gameService;

    private Drink mockDrink;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize a mock drink object
        mockDrink = new Drink();
        mockDrink.setStrDrink("Mojito");
        mockDrink.setStrInstructions("Mix ingredients and serve.");
        mockDrink.setStrCategory("Cocktail");
        mockDrink.setStrGlass("Highball glass");
        mockDrink.setStrDrinkThumb("https://example.com/mojito.jpg");
        mockDrink.setStrIngredient1("Rum");
        mockDrink.setStrIngredient2("Mint");
        mockDrink.setStrIngredient3("Syrup");

        // Reset game state
        gameService.getPlayedDrinks().clear();
        gameService.setNumberOfTries(5);
        gameService.setScore(0);
        gameService.setHighestScore(0);
        gameService.setContinuePlaying(true);
    }

    @Test
    void testStartGameInitialization() {
        assertEquals(5, gameService.getNumberOfTries());
        assertEquals(0, gameService.getScore());
        assertEquals(0, gameService.getHighestScore());
        assertTrue(gameService.isContinuePlaying());
        assertNotNull(gameService.getPlayedDrinks());
    }

    @Test
    void testGetNewDrink() {
        when(drinkApiService.getRandomDrinkFromApi()).thenReturn(mockDrink);

        gameService.getNewDrink();

        assertEquals("Mojito", gameService.getCurrentDrink().getStrDrink());
        assertTrue(gameService.getPlayedDrinks().contains("Mojito"));
    }
    @Test
    void testGetHiddenDrinkName() {
        String drinkName = "Mojito";
        String hiddenName = gameService.getHiddenDrinkName(drinkName);
        assertEquals("______", hiddenName);
        String drinkName2 = "501 Blue";
        String hiddenName2 = gameService.getHiddenDrinkName(drinkName2);
        assertEquals("___ ____", hiddenName2);
    }
    @Test
    void testProcessWinFirstTry() {
        gameService.setCurrentDrink(mockDrink);
        gameService.processWin();
        assertEquals(5, gameService.getScore());
    }
    @Test
    void testProcessWinThirdTry() {
        gameService.setCurrentDrink(mockDrink);
        gameService.setNumberOfTries(3);
        gameService.processWin();
        assertEquals(3, gameService.getScore());
    }

    @Test
    void testProcessWinMultiple() {
        gameService.setCurrentDrink(mockDrink);
        gameService.setNumberOfTries(3);
        gameService.processWin();
        assertEquals(3, gameService.getScore());
        gameService.setNumberOfTries(5);
        gameService.processWin();
        assertEquals(8, gameService.getScore());
    }

    @Test
    void testProcessRevealLetter() {
        gameService.setCurrentDrink(mockDrink);
        gameService.setHiddenWord(gameService.getHiddenDrinkName(mockDrink.strDrink));
        gameService.revealALetter();
        assertTrue(gameService.getHiddenWord().contains("M") || gameService.getHiddenWord().contains("o")
                || gameService.getHiddenWord().contains("h") || gameService.getHiddenWord().contains("i")
                || gameService.getHiddenWord().contains("t"));
    }

    @Test
    void testProcessIncorrectGuess() {
        gameService.setCurrentDrink(mockDrink);
        gameService.setHiddenWord(gameService.getHiddenDrinkName(mockDrink.strDrink));
        gameService.processIncorrectGuess();
        assertEquals(4, gameService.getNumberOfTries());
    }

}
