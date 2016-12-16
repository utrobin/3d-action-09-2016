package ru.javajava.main;

import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.method.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.javajava.exceptions.AlreadyExistsException;
import ru.javajava.mechanics.GameMechanics;
import ru.javajava.mechanics.GameMechanicsImpl;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.MechanicsExecutor;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.mechanics.base.Coords;
import ru.javajava.mechanics.base.MyVector;
import ru.javajava.mechanics.base.ServerPlayerSnap;
import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.mechanics.internal.ClientSnapService;
import ru.javajava.mechanics.internal.GameSessionService;
import ru.javajava.mechanics.internal.ServerSnapService;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;
import ru.javajava.websocket.RemotePointService;

import java.util.Random;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


/**
 * Created by ivan on 15.12.16.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@Transactional
public class GameMechanicsTest {
    @MockBean
    private RemotePointService remotePointService;
    @Autowired
    private AccountService accountService;

    @Autowired
    private ServerSnapService serverSnapService;
    @Autowired
    private ClientSnapService clientSnapService;

    private UserProfile user1;
    private UserProfile user2;

    @Before
    public void setUp () throws AlreadyExistsException{
        when(remotePointService.isConnected(any())).thenReturn(true);
        user1 = accountService.addUser("user1", "mailmail@yandex.ru", "lkjhgfdsasdfgh");
        user2 = accountService.addUser("user2", "yandex@mailmail.ru", "asdgfasdfda");
    }

    @Test
    public void gameStartedTest () {
        final GameMechanics gameMechanics =
                new GameMechanicsImpl(accountService, serverSnapService, remotePointService, clientSnapService);
        startGame(user1.getId(), user2.getId(), gameMechanics);
    }

    @Test
    public void killingTest() {
        final GameMechanics gameMechanics =
                new GameMechanicsImpl(accountService, serverSnapService, remotePointService, clientSnapService);
        final GameSession gameSession = startGame(user1.getId(), user2.getId(), gameMechanics);

        final UserSnap snapFirstPlayer = new UserSnap();
        snapFirstPlayer.setId(user1.getId());
        final Random random = new Random();
        final Coords firstPlayerPos =
                new Coords(random.nextDouble(), random.nextDouble(), random.nextDouble());
        snapFirstPlayer.setPosition(firstPlayerPos);
        snapFirstPlayer.setCamera(new Coords(random.nextDouble(), random.nextDouble(), random.nextDouble()));

        final UserSnap snapSecondPlayer = new UserSnap();
        snapFirstPlayer.setId(user2.getId());
        final Coords secondPlayerPos =
                new Coords(random.nextDouble(), random.nextDouble(), random.nextDouble());
        snapSecondPlayer.setPosition(secondPlayerPos);

        // Вектор выстрела второго игрока по первому направляем в центр первого игрока
        final Coords secondPlayerShot = firstPlayerPos.subtract(secondPlayerPos);
        snapSecondPlayer.setCamera(secondPlayerShot);
        snapSecondPlayer.setFiring(true);

        // Один выстрел по первому игроку
        gameMechanics.addClientSnapshot(user1.getId(), snapFirstPlayer);
        gameMechanics.addClientSnapshot(user2.getId(), snapSecondPlayer);
        gameMechanics.gmStep(5);

        final GameUser player2 = gameSession.getPlayers().stream()
                .filter(x -> x.getId() == user2.getId()).findFirst().get();

        Assert.assertTrue("There should be no kills yet!", player2.getScores() == 0);

        // Еще два выстрела
        gameMechanics.addClientSnapshot(user2.getId(), snapSecondPlayer);
        gameMechanics.addClientSnapshot(user2.getId(), snapSecondPlayer);
        gameMechanics.gmStep(5);

        Assert.assertTrue("Player 1 should be killed by player 2!", player2.getScores() > 0);
    }


    private GameSession startGame(long player1, long player2, GameMechanics gameMechanics) {
        gameMechanics.addUser(player1);
        gameMechanics.addUser(player2);
        gameMechanics.gmStep(5);
        final GameSession gameSession = gameMechanics.getSessionForUser(player1);
        Assert.assertNotNull("Game session should be started on closest tick, but it didn't", gameSession);
        return gameSession;
    }

    @Test
    public void sessionsTest() throws AlreadyExistsException {
        final GameMechanics gameMechanics =
                new GameMechanicsImpl(accountService, serverSnapService, remotePointService, clientSnapService);
        final GameSession gameSession = startGame(user1.getId(), user2.getId(), gameMechanics);

        int usersTotal = 2;
        while (!gameSession.isFull()) {
            final UserProfile newUser = accountService.addUser(
                    "extraUser" + usersTotal, "lkjhgfdsasdfgh", "mailmail@yandex.ru" + usersTotal);
            gameMechanics.addUser(newUser.getId());
            gameMechanics.gmStep(5);
            usersTotal++;
        }

        Assert.assertTrue("Should be still one room", gameMechanics.getSessionsNum() == 1);
        Assert.assertTrue("Everybody should play in this session", gameSession.getPlayers().size() == usersTotal);

        UserProfile newUser = accountService.addUser(
                "extraUser" + usersTotal, "lkjhgfdsasdfgh", "mailmail@yandex.ru" + usersTotal);
        gameMechanics.addUser(newUser.getId());
        gameMechanics.gmStep(5);
        usersTotal++;

        Assert.assertTrue("Should be created second session", gameMechanics.getSessionsNum() == 2);

        newUser = accountService.addUser(
                "extraUser" + usersTotal, "lkjhgfdsasdfgh", "mailmail@yandex.ru" + usersTotal);
        gameMechanics.addUser(newUser.getId());
        gameMechanics.gmStep(5);

        final GameSession newSession = gameMechanics.getSessionForUser(newUser.getId());
        Assert.assertTrue("New room should contain two players", newSession.getPlayers().size() == 2);
    }
}


