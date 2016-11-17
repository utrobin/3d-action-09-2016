package ru.javajava.mechanics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.mechanics.internal.ClientSnapService;
import ru.javajava.mechanics.internal.GameSessionService;
import ru.javajava.mechanics.internal.ServerSnapService;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;
import ru.javajava.websocket.RemotePointService;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ivan on 15.11.16.
 */
@SuppressWarnings({"unused", "FieldMayBeFinal"})
@Service
public class GameMechanicsImpl implements GameMechanics {
        private static final Logger LOGGER = LoggerFactory.getLogger(GameMechanicsImpl.class);

        private AccountService accountService;

        private ClientSnapService clientSnapshotsService;

        private ServerSnapService serverSnapshotService;

        private RemotePointService remotePointService;

        private GameSessionService gameSessionService;

        private Set<Long> playingUsers = new HashSet<>();

        private ConcurrentLinkedQueue<Long> waiters = new ConcurrentLinkedQueue<>();

        private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();

        private int rooms = 0;
        GameSession mainSession;


    public GameMechanicsImpl(AccountService accountService, ServerSnapService serverSnapshotService,
                             RemotePointService remotePointService,
                             GameSessionService gameSessionService) {
        this.accountService = accountService;
        this.serverSnapshotService = serverSnapshotService;
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
        this.clientSnapshotsService = new ClientSnapService();
    }

    @Override
    public void addClientSnapshot(long userId, UserSnap userSnap) {
        tasks.add(() -> clientSnapshotsService.pushClientSnap(userId, userSnap));
    }

    @Override
    public void addUser(long user) {
        if (gameSessionService.isPlaying(user)) {
            return;
        }
        waiters.add(user);
    }

    @Override
    public void removeUser(long user) {
        if (gameSessionService.isPlaying(user)) {
            gameSessionService.removePlayer(mainSession, accountService.getUserById(user));
        }
    }

    private void tryStartGames() {
        final List<UserProfile> matchedPlayers = new LinkedList<>();

        while (waiters.size() >= 2 || waiters.size() >= 1 && matchedPlayers.size() >= 1) {
            final long candidate = waiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            matchedPlayers.add(accountService.getUserById(candidate));
            if(matchedPlayers.size() == 2) {
                mainSession = gameSessionService.startGame(matchedPlayers);
                matchedPlayers.clear();
                rooms++;
                LOGGER.info("Started game, total rooms: {}", rooms);
            }
        }
        matchedPlayers.stream().map(UserProfile::getId).forEach(waiters::add);
    }

    private boolean insureCandidate(long candidate) {
        return remotePointService.isConnected(candidate) &&
                accountService.getUserById(candidate) != null;
    }

    @Override
    public void gmStep(long frameTime) {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            if (nextTask != null) {
                try {
                    nextTask.run();
                } catch (RuntimeException ex) {
                    LOGGER.error("Cant handle game task", ex);
                }
            }
        }

        for (GameSession session : gameSessionService.getSessions()) {
            clientSnapshotsService.processSnapshotsFor(session);
        }


        final Iterator<GameSession> iterator = gameSessionService.getSessions().iterator();
        final Collection<GameSession> sessionsToTerminate = new ArrayList<>();
        while (iterator.hasNext()) {
            final GameSession session = iterator.next();
            try {
                serverSnapshotService.sendSnapshotsFor(session);
            } catch (RuntimeException ex) {
                LOGGER.error("No snapshots, terminating the session", ex);
                sessionsToTerminate.add(session);
                rooms--;
                mainSession = null;
            }
        }
        sessionsToTerminate.forEach(gameSessionService::notifyGameIsOver);

        // Пока только одна комната
        if (mainSession == null) {
            tryStartGames();
        }
        else {
            // Добавление к сущ. комнате
            while (!waiters.isEmpty()) {
                long playerId = waiters.poll();
                UserProfile user = accountService.getUserById(playerId);
                gameSessionService.addPlayer(mainSession, user);
            }
        }
        clientSnapshotsService.clear();
    }

    @Override
    public void reset() {

    }
}