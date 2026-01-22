package com.mickeytheq.hades.core.global;

import com.mickeytheq.hades.core.model.Card;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CardDatabaseImpl implements CardDatabase {
    // a map of card id to card entry
    // cards can be registered multiple times (e.g. open project and open editor) so we have to track individual
    // registrations
    // plus the register/unregister may happen in an interleaved fashion, e.g. open project A, open card B (in project A), close project A, close card B
    // so ordering cannot be used as a guide
    private final Map<String, CardEntry> cardEntryMapById = new HashMap<>();
    private final Map<Object, List<CardEntry>> registerKeyMap = new IdentityHashMap<>();

    // lock to protect the above from simultaneous update operations - unlikely but easier to do it than try and
    // reason about whether it might happen
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Override
    public Collection<Card> getCards() {
        readWriteLock.readLock().lock();
        try {
            return cardEntryMapById.values().stream().map(CardEntry::getCard).collect(Collectors.toList());
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Optional<Card> getCardWithId(String id) {
        CardEntry cardEntry = cardEntryMapById.get(id);

        if (cardEntry == null)
            return Optional.empty();

        return Optional.of(cardEntry.getCard());
    }

    @Override
    public Path getSourcePathForCard(Card card) {
        readWriteLock.readLock().lock();
        try {
            CardEntry cardEntry = cardEntryMapById.get(card.getId());
            return cardEntry.getSourcePath();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public void register(Consumer<CardDatabaseLoader> loader, Object registerKey) {
        CardDatabaseLoaderImpl impl = new CardDatabaseLoaderImpl(registerKey);
        loader.accept(impl);

        readWriteLock.writeLock().lock();
        try {
            List<CardEntry> cardEntries = new ArrayList<>();

            for (LoadingCardEntry loadingCardEntry : impl.getCardEntries()) {
                Card card = loadingCardEntry.getCard();
                Path sourcePath = loadingCardEntry.getSourcePath();

                CardEntry cardEntry = cardEntryMapById.computeIfAbsent(card.getId(), s -> new CardEntry(card, sourcePath));
                cardEntry.getRegisteredKeys().add(registerKey);

                cardEntries.add(cardEntry);

                registerKeyMap.put(registerKey, cardEntries);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void unregister(Object registerKey) {
        readWriteLock.writeLock().lock();
        try {
            // get the list of CardEntry recorded for this key
            List<CardEntry> cardEntries = registerKeyMap.remove(registerKey);

            if (cardEntries == null)
                return;

            // for each one, unregister from the list for that card entry and then
            // prune the overall entry if there are no more registered keys
            for (CardEntry cardEntry : cardEntries) {
                cardEntry.getRegisteredKeys().remove(registerKey);

                if (cardEntry.getRegisteredKeys().isEmpty()) {
                    cardEntryMapById.remove(cardEntry.getCard().getId());
                }
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void update(Card card) {
        readWriteLock.writeLock().lock();
        try {
            CardEntry cardEntry = cardEntryMapById.get(card.getId());

            if (cardEntry == null)
                return;

            cardEntry.setCard(card);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    private static class CardDatabaseLoaderImpl implements CardDatabaseLoader {
        private final Object registerKey;
        private final List<LoadingCardEntry> cardEntries = new ArrayList<>();

        public CardDatabaseLoaderImpl(Object registerKey) {
            this.registerKey = registerKey;
        }

        @Override
        public Object getRegisterKey() {
            return registerKey;
        }

        @Override
        public void registerCard(Card card, Path sourcePath) {
            cardEntries.add(new LoadingCardEntry(card, sourcePath));
        }

        public List<LoadingCardEntry> getCardEntries() {
            return cardEntries;
        }
    }

    static class LoadingCardEntry {
        private final Card card;
        private final Path sourcePath;

        public LoadingCardEntry(Card card, Path sourcePath) {
            this.card = card;
            this.sourcePath = sourcePath;
        }

        public Card getCard() {
            return card;
        }

        public Path getSourcePath() {
            return sourcePath;
        }
    }


    private static class CardEntry {
        private Card card;
        private final Path sourcePath;
        private final Set<Object> registeredKeys = new HashSet<>();

        public CardEntry(Card card, Path sourcePath) {
            this.card = card;
            this.sourcePath = sourcePath;
        }

        public Card getCard() {
            return card;
        }

        public void setCard(Card card) {
            this.card = card;
        }

        public Path getSourcePath() {
            return sourcePath;
        }

        public Set<Object> getRegisteredKeys() {
            return registeredKeys;
        }
    }
}
