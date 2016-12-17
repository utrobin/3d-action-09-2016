package ru.javajava.mechanics;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.mechanics.internal.ClientSnapService;
import ru.javajava.mechanics.internal.GameSessionService;
import ru.javajava.mechanics.internal.ServerSnapService;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;
import ru.javajava.websocket.Message;
import ru.javajava.websocket.RemotePointService;

import java.io.IOException;
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
        private ConcurrentLinkedQueue<Long> deleted = new ConcurrentLinkedQueue<>();

        private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<>();


    public GameMechanicsImpl(AccountService accountService, ServerSnapService serverSnapshotService,
                             RemotePointService remotePointService) {
        this.accountService = accountService;
        this.serverSnapshotService = serverSnapshotService;
        this.remotePointService = remotePointService;
        this.gameSessionService = new GameSessionService(remotePointService);
        this.clientSnapshotsService = new ClientSnapService();
    }



    @Override
    public void addClientSnapshot(long userId, UserSnap userSnap) {
        tasks.add(() -> clientSnapshotsService.pushClientSnap(userId, userSnap));
    }

    @Override
    public int getSessionsNum() {
        return gameSessionService.getSessions().size();
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
        if (!gameSessionService.isPlaying(user)) {
            return;
        }
        deleted.add(user);
    }


    @Override
    public GameSession getSessionForUser(long user) {
        return gameSessionService.getSessionForUser(user);
    }

    private boolean insureCandidate(long candidate) {
        return remotePointService.isConnected(candidate) &&
                accountService.getUserById(candidate) != null;
    }

    @Override
    public void gmStep(long frameTime) {
        while (!tasks.isEmpty()) {
            final Runnable nextTask = tasks.poll();
            try {
                nextTask.run();
            } catch (RuntimeException ex) {
                LOGGER.error("Cant handle game task", ex);
            }
        }

        for (GameSession session : gameSessionService.getSessions()) {
            clientSnapshotsService.processSnapshotsFor(session);
        }


        // Отправка снапшотов
        final Iterator<GameSession> iterator = gameSessionService.getSessions().iterator();
        final Collection<GameSession> sessionsToTerminate = new ArrayList<>();
        while (iterator.hasNext()) {
            final GameSession session = iterator.next();
            try {
                serverSnapshotService.sendSnapshotsFor(session);
            } catch (RuntimeException ex) {
                sessionsToTerminate.add(session);
                LOGGER.error("Session was terminated!");
            }
            sessionsToTerminate.forEach(gameSessionService::notifyGameIsOver);
        }


        // Удаление вышедших пользователей
        removeLeftUsers();


        // Добавление новых игроков в игру
        while (!waiters.isEmpty()) {
            final long candidate = waiters.poll();
            if (!insureCandidate(candidate)) {
                continue;
            }
            final UserProfile newPlayer = accountService.getUserById(candidate);
            gameSessionService.addNewPlayer(newPlayer);
        }

        clientSnapshotsService.clear();
    }


    private void removeLeftUsers() {
        final Map<GameSession, List<Long>> sessionLeftPlayers = new HashMap<>();
        while (!deleted.isEmpty()) {
            final long removedPlayer = deleted.poll();

            final GameSession session = gameSessionService.getSessionForUser(removedPlayer);
            gameSessionService.removePlayer(session, removedPlayer);

            sessionLeftPlayers.putIfAbsent(session, new ArrayList<>());
            final List<Long> leftUsers = sessionLeftPlayers.get(session);
            leftUsers.add(removedPlayer);
        }

        // Отправка сведений о вышедших юзерах игрокам в каждой связанной сессии
        for (GameSession session : sessionLeftPlayers.keySet()) {
            final List<Long> playersLeft = sessionLeftPlayers.get(session);
            final JSONArray jsonArray = new JSONArray(playersLeft);
            final Message message = new Message(Message.REMOVE_USER, jsonArray.toString());
            for (GameUser user : session.getPlayers()) {
                try {
                    remotePointService.sendMessageToUser(user.getId(), message);
                } catch (IOException e) {
                    LOGGER.error("Error sending info about removing user(-s) to user {}", user.getId());
                }
            }
        }
    }

    @Override
    public void reset() {

    }
}
